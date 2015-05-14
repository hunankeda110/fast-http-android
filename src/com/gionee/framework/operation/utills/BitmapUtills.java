
package com.gionee.framework.operation.utills;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;

/**
 * com.gionee.app_framework.operation.utills.BitmapUtills
 * 
 * @author yuwei <br/>
 *         create at 2013-3-18 上午9:36:50 TODO 封装了常用到图片处理操作
 */
public class BitmapUtills {
    // Gionee <yuwei><2012-5-29> add for CR00821559 begin
    private static final String TAG = "BitmapUtills";

    /**
     * 计算相应隿要的大小，存在问頿
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

        int initialSize = 1;

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            initialSize = lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            initialSize = 1;
        } else if (minSideLength == -1) {
            initialSize = lowerBound;
        } else {
            initialSize = upperBound;
        }

        int roundedSize;

        if (initialSize <= 8) {

            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static Bitmap getRotatePic(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return rotation90(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts));
            else
                return rotation90(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        return null;
    }

    /**
     * 创建带有影子的图燿
     * 
     * @param originalImage
     *            原图燿
     * @param scale
     *            缩放比例
     * @return
     */
    public static Bitmap createReflectedImage(Bitmap originalImage, float reflectRatio, float scale) {

        int width = (int) (originalImage.getWidth() * scale);
        int height = (int) (originalImage.getHeight() * scale);

        final Rect srcRect = new Rect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        final Rect dstRect = new Rect(0, 0, width, height);

        final int reflectionGap = 1;

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (int) (height + height * reflectRatio),
                Config.ARGB_8888);
        Canvas canvasRef = new Canvas(bitmapWithReflection);

        canvasRef.drawBitmap(originalImage, srcRect, dstRect, null);

        Matrix matrix = new Matrix();
        matrix.setTranslate(0, height + height + reflectionGap);
        matrix.preScale(scale, -scale);

        canvasRef.drawBitmap(originalImage, matrix, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, height, 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x80ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvasRef.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        originalImage.recycle();
        return bitmapWithReflection;
    }

    /**
     * 得到缩小的图片，这里缩小的是图片质量
     * 
     * @param dataBytes
     * @param maxWidth
     * @return
     */
    public static Bitmap getCorrectBmp(byte dataBytes[], int inSampleSize, Bitmap.Config config) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = config;
        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        Bitmap originalImage = BitmapFactory.decodeByteArray(dataBytes, 0, dataBytes.length, opts);
        return originalImage;
    }

    /**
     * 得到圆角图片
     * 
     * @param bitmap
     *            原图偿
     * @param scale
     *            缩放比例
     * @param roundPx
     *            圆角像素
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float scale, float roundPx,
            Bitmap.Config config) {

        int width = (int) (bitmap.getWidth() * scale);
        int height = (int) (bitmap.getHeight() * scale);

        Bitmap output = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(output);

        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        // draw的方式缩撿
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        // Matrix的方式缩撿
        // Matrix matrix = new Matrix();
        // matrix.postScale(scale, scale);
        // canvas.drawBitmap(bitmap, matrix, paint);

        return output;
    }

    /**
     * 得到缩放后的图片
     * 
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap dstbmp = Bitmap
                .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return dstbmp;
    }

    public static Bitmap getScaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeigh) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeigh);
        Bitmap dstbmp = Bitmap
                .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return dstbmp;
    }

    public static Bitmap getDisplayWidthPic(Bitmap bitmap, Activity context) {
        Bitmap scaredBmp = null;

        int displayWidth = AndroidUtills.getDisplayWidth(context);
        float scale = displayWidth / (float) bitmap.getWidth();
        scaredBmp = getScaleBitmap(bitmap, scale);

        return scaredBmp;

    }

    public static Bitmap rotation90(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        if (bitmap.isRecycled())
            return null;
        Bitmap bmp = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap.recycle();
        return bmp;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    public static Bitmap compressFromBitmap(Bitmap image, int targetwidth, int targetHeigh, long maxSize) {
        LogUtils.log(TAG, LogUtils.getThreadName());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos䶿
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 庿始读入图片，此时把options.inJustDecodeBounds 设回true丿
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        bitmapRecycle(bitmap);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，懿以高和宽我们设置䶿
        float hh = targetHeigh;// 这里设置目标高度夿(800f)
        float ww = targetwidth;// 这里设置目标宽度夿(480f)
        // 缩放比ῂ由于是固定比例缩放，只用高或濅宽其中䶿个数据进行计算即卿
        int be = 1;// be=1表示不缩撿
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩撿
            float scale = (float) (newOpts.outWidth / ww);
            try {
                BigDecimal bigDecimal = new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP);
                be = new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            } catch (Exception e) {
                e.printStackTrace();
                be = (int) (newOpts.outWidth / ww);
            }
            LogUtils.logd(TAG, LogUtils.getThreadName() + " scale = " + scale + " real: width = " + w
                    + " heigth = " + h + " target: width = " + targetwidth + " height = " + targetHeigh);
        } else if (w <= h && h > hh) {// 如果高度高的话根据宽度固定大小缩撿
            float scale = (float) (newOpts.outHeight / hh);
            try {
                BigDecimal bigDecimal = new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP);
                be = new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            } catch (Exception e) {
                e.printStackTrace();
                be = (int) (newOpts.outHeight / hh);
            }
            LogUtils.logd(TAG, LogUtils.getThreadName() + " scale = " + scale + " real: width = " + w
                    + " heigth = " + h + " target: width = " + targetwidth + " height = " + targetHeigh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false丿
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        try {
            isBm.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return qualityCompress(bitmap, maxSize);// 压缩好比例大小后再进行质量压线
    }

    /**
     * 质量压缩方法
     * 
     * @param image
     * @return
     * @author yangxiong
     * @description TODO
     */
    public static Bitmap qualityCompress(Bitmap image, long maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这酿100表示不压缩，把压缩后的数据存放到baos䶿
        int options = 100;
        byte[] thumbData;
        Bitmap bitmap = null;

        do {
            if (options < 0) {
                break;
            }
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos䶿
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream䶿
            options -= 10;// 每次都减宿5
            bitmapRecycle(bitmap);
            bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
            thumbData = Util.bmpToByteArray(bitmap, false);
            LogUtils.logd(TAG, LogUtils.getThreadName() + "length = " + thumbData.length);
            try {
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (thumbData.length > maxSize);

        bitmapRecycle(image);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void bitmapRecycle(Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled() == false) {
            bitmap.recycle();
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属俧：旋转的角帿
     * 
     * @param path
     *            图片绝对路径
     * @return degree旋转的角帿
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.logd(TAG, LogUtils.getThreadName() + " degree = " + degree);
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        LogUtils.logd(TAG, LogUtils.getThreadName() + " angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return resizedBitmap;
    }

    // Gionee <yuwei><2012-6-2> add for CR00821559 end
}
