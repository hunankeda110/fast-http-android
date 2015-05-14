//Gionee <wangyy><2014-03-12> modify for CR00956169 begin
package com.gionee.framework.operation.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Build;
import android.provider.SyncStateContract.Constants;

import com.gionee.framework.operation.utills.AndroidUtills;
import com.gionee.framework.operation.utills.LogUtils;
import com.gionee.framework.operation.utills.Md5Utill;

/**
 * Protocol: data_format_version=数据格式协议版本号, uid=手机唯一识别码, encript_imei=已加密的imei, app_version=应用软件版本号 ,
 * event_count=统计事件的条数, event_content=统计事件的内容.
 */

public class PublicHeaderParamsManager {
    public static final String TAG = "PublicHeaderParamsManager";
    public static final String MD5_SIGN = "NTQzY2JmMzJhYTg2N2RvY3Mva2V5";
    /**
     * 以下命名表示其所占的字节数
     */
    public static final int UID_LENGTH = 32;

    // 数据协议格式版本号
//    private static final byte mDataFormatProtocolVerNum;
    private short mLogSizeSum;
    private Context mContext = null;
    private static String sUid = null;

    public PublicHeaderParamsManager(Context context) {
        mContext = context.getApplicationContext();
//        dataInit();
    }

//    public static String getSign(Context context) {

//        return Md5Utill.makeMd5Sum(getUid(context) + MD5_SIGN);

//    }

    public String getModel() {
        String model = Build.MODEL;
        String brand = Build.BRAND;
        LogUtils.logd(TAG, LogUtils.getThreadName() + " model = " + model + " , brand = " + brand);
        return brand + "￥" + model;
    }

    public String getSystemVersion() {
        LogUtils.logd(TAG, LogUtils.getThreadName() + " System version = " + android.os.Build.VERSION.RELEASE);
        return android.os.Build.VERSION.RELEASE;
    }

    public String getVersionNameAndLength() {
        String name = AndroidUtills.getAppVersionName(mContext);
        LogUtils.logd(TAG, LogUtils.getThreadName() + " app version name = " + name);
        return name;
    }

    public void setStatisticsDataSum(short sum) {
        LogUtils.logd(TAG, LogUtils.getThreadName() + "sum = " + sum);
        mLogSizeSum = sum;
    }

    /**
     * 事件数据头信息的封装
     */
    public List<NameValuePair> getEventDataHeaderParams() {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

//        /**
//         * 数据格式协议版本号
//         */
//        formparams.add(new BasicNameValuePair("data_format_version", String
//                .valueOf(mDataFormatProtocolVerNum)));
//        /**
//         * 手机唯一识别码
//         */
//        formparams.add(new BasicNameValuePair("uid", getUid(mContext)));
//        /**
//         * 加密的imei
//         */
//        formparams.add(new BasicNameValuePair("encript_imei", getIMEINumber()));
//        /**
//         * 应用软件版本号
//         */
//        formparams.add(new BasicNameValuePair("app_version", getVersionNameAndLength()));
//        /**
//         * 统计事件条数
//         */
//        formparams.add(new BasicNameValuePair("event_count", String.valueOf(mLogSizeSum)));

        return formparams;
    }

}
//Gionee <wangyy><2014-03-12> modify for CR00956169 end