package com.gionee.framework.operation.utills;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

public class PictureUtil {
    public static final int IMAGE_MAX_HEIGHT = 480;
    public static final int IMAGE_MAX_WIDTH = 320;
    private static final String TAG = "PictureUtil";

    /**
     * 把bitmap转换成String
     * 
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 计算图片的缩放值
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        LogUtils.logd(LogUtils.getThreadName(), " width = " + width + " height = " + height);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public int calculateInSampleSizeOnFileSize(String imagePath) {
        File fileDir = new File(imagePath);
        if (fileDir.exists()) {
            long fileSize = fileDir.length();
            if (fileSize > 100) {
                return (int) (fileSize / 100) + 1;
            }
        }
        return 1;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * 
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        options.inSampleSize++;
        LogUtils.logd(TAG, " options.inSampleSize = " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            InputStream is = new FileInputStream(filePath);
            options.inTempStorage = new byte[16 * 1024];
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            Bitmap btp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return btp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * 
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        LogUtils.logd(TAG, " options.inSampleSize = " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            InputStream is = new FileInputStream(filePath);
            options.inTempStorage = new byte[16 * 1024];
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            Bitmap btp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return btp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * 
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height, long maxSize,
            CompressFormat format) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        LogUtils.logd(TAG, " options.inSampleSize = " + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        try {
            InputStream is = new FileInputStream(filePath);
            options.inTempStorage = new byte[16 * 1024];
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            Bitmap btp = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return compressBitmapSize(maxSize, options, btp, format);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap compressBitmapSize(float maxSize, final BitmapFactory.Options options, Bitmap btp,
            CompressFormat format) {
        Bitmap bitmap = btp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btp.compress(format, 100, baos);
        LogUtils.logd(TAG, " before lenth = " + baos.toByteArray().length / 1024);
        int compressPercent = 100;
        float orginalSize = baos.toByteArray().length / 1024;
        while (orginalSize > maxSize) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            LogUtils.logd(TAG, " orginalSize = " + orginalSize / 1024 + "M");
            LogUtils.logd(TAG, " targetSize = " + maxSize / 1024);
            Bitmap newBtp = null;
            if (format.equals(CompressFormat.PNG)) {
                newBtp = compressPngBmp(options, format, bitmap, baos);
            } else if (format.equals(CompressFormat.JPEG)) {
                compressPercent -= 10;
                if (compressPercent < 1) {
                    return bitmap;
                }
                LogUtils.logd(TAG, " compressPercent = " + compressPercent);
                newBtp = compressJpgBmp(options, format, bitmap, baos, compressPercent);

            }
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            newBtp.compress(format, 100, baos2);
            float newSize = baos2.toByteArray().length / 1024;
            if (newSize >= orginalSize) {
                return newBtp;
            } else {
                orginalSize = newSize;
                LogUtils.logd(TAG, " new lenth = " + orginalSize);
                baos2.reset();
                bitmap = newBtp;
            }
        }
        return bitmap;
    }

    public static Bitmap compressJpgBmp(final BitmapFactory.Options options, CompressFormat format,
            Bitmap bitmap, ByteArrayOutputStream baos, int compressPercent) {
        Bitmap newBtp;
        bitmap.compress(format, compressPercent, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        LogUtils.logd(TAG, " after lenth = " + baos.toByteArray().length / 1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        newBtp = BitmapFactory.decodeStream(isBm, null, options);
        bitmap.recycle();
        return newBtp;
    }

    public static Bitmap compressPngBmp(final BitmapFactory.Options options, CompressFormat format,
            Bitmap bitmap, ByteArrayOutputStream baos) {
        Bitmap newBtp;
        options.inSampleSize = 2;
        bitmap.compress(format, 100, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        LogUtils.logd(TAG, " after lenth = " + baos.toByteArray().length / 1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        newBtp = BitmapFactory.decodeStream(isBm, null, options);
        bitmap.recycle();
        return newBtp;
    }

    public static Options getDecodeOptions(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    /**
     * 根据路径删除图片
     * 
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     * 
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     * 
     * @return
     */
    public static String getAlbumName() {
        return "sheguantong";
    }

}
