package com.gionee.framework.operation.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gionee.framework.event.SuperInjectFactory;
import com.gionee.framework.operation.page.FrameWorkInit;
import com.gionee.framework.operation.utills.FileUtil;

//Gionee <yuwei><2013-5-27> add for CR00821559 begin
/**
 * com.gionee.app_framework.operation.utills.Utills
 * 
 * @author yuwei <br/>
 *         create at 2013-5-27 上午9:44:42 TODO bimap文件缓存
 */

public class BitmapFileCache {

    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 字符串是否email格式
     * 
     * @param email
     * @return [参数说明]
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return email.matches("^(\\w|\\.|-|\\+)+@(\\w|-)+(\\.(\\w|-)+)+$");
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
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

    /**
     * 从文件获取图片
     * 
     * @param mContext
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getBmpFromFile(String fileName) throws FileNotFoundException {

        File file = new File(FrameWorkInit.imgPath + fileName);
        if (file.exists()) {
            Log.v("debug", "从文件获取图片" + file.getName());
            FileInputStream inStream = new FileInputStream(file);
            return inStream;

        } else
            return null;

    }

    /**
     * @param fileName
     * @return
     * @author yuwei
     * @description TODO delete bitmap from file cache
     */
    public static boolean deleteBitmap(String fileName) {

        File file = new File(FrameWorkInit.imgPath + fileName);
        if (file.exists() && file.delete()) {
            return true;
        } else
            return false;
    }

    /**
     * 保存图片
     * 
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static boolean saveBmpToPng(Context context, Bitmap bmp, String fileName) {
        if (bmp == null) {
            return false;
        }
        String picName = FrameWorkInit.imgPath + fileName;
        FileOutputStream fileOut = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {

                File file = new File(FrameWorkInit.imgPath);
                if (!file.mkdirs()) {
                    return false;
                }
                Log.v("debug", "保存图片到SD卡" + picName);
                try {
                    fileOut = new FileOutputStream(picName);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
                    return true;
                } finally {
                    if (fileOut != null) {
                        try {
                            fileOut.flush();
                            fileOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void cleanCache(Context context) {
        SuperInjectFactory.lookupNetCacheManage().clearAll(context);
        File file = new File(FrameWorkInit.SDCARD + "/mynapp/");
        FileUtil.deleteFile(file);
        String data = "/data/data/" + context.getPackageName().toString();
        File share = new File(data);
        FileUtil.deleteFile(share);
        Toast.makeText(context, "缓存已清除", Toast.LENGTH_SHORT).show();

    }
    // Gionee <yuwei><2013-6-18> add for CR00821559 end
}
