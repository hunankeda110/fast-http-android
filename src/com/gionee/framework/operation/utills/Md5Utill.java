/*
 * Md5Utill.java
 * classes : cn.com.donson.naf.other.util.Md5Utill
 * @author 余炜
 * V 1.0.0
 * Create at 2012-5-7 下午02:14:28
 */
package com.gionee.framework.operation.utills;

import android.annotation.SuppressLint;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * com.gionee.app_framework.operation.utills.Md5Utill
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:50:08 TODO MD5 utills
 */
public class Md5Utill {
//    private static final String TAG = "Md5Utill";

    /**
     * 生成MD5校验码
     * 
     * @param srcContent
     * @return
     */
    @SuppressLint("NewApi")
    public static String makeMd5Sum(String srcContent) {
        if (StringUtils.isEmpty(srcContent)) {
            return null;
        }

        String strDes = null;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(srcContent.getBytes(Charset.forName("UTF-8")));
            strDes = bytes2Hex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 将字节流转换成字符串
     * 
     * @param byteArray
     * @return 转换后的字符串
     */
    public static String bytes2Hex(byte[] byteArray) {

        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (byteArray[i] >= 0 && byteArray[i] < 16) {
                strBuf.append("0");
            }
            strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
        }
        return strBuf.toString();
    }

}
