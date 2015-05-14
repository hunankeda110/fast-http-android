package com.gionee.framework.operation.utills;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gionee.framework.model.bean.Constants;

/**
 * com.gionee.app_framework.operation.utills.AndroidUtills
 * 
 * @author yuwei <br/>
 *         create at 2013-3-18 上午9:35:08 TODO 封装了android中一些常用到操作
 */
public class AndroidUtills {
    private static final String TAG = "AndroidUtills";

//	private static SharedPreferences mShareConfig;

    /**
     * 获取imei
     * 
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    public static void showShortToast(Context context, int id) {
        Toast.makeText(context, context.getText(id), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @return such as Constants.wifi_available
     * @author yuwei
     * @description TODO get the net work type
     */
    public static int getNetworkType(Activity context) {
        @SuppressWarnings("static-access")
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        // 获取当前的网络连接是否可用
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return Constants.NET_UNABLE;
        }
        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (State.CONNECTED == state || state == State.CONNECTING) {
            return Constants.WIFI_AVAILABLE;
        }
        // GPRS
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (State.CONNECTED == state) {
            return Constants.MOBILE_AVAILABLE;
        }
        return Constants.OTHERNET;
    }

    public interface LoadListener {
        void startLoad();
    }

    /**
     * @param context
     * @param dipValue
     * @return the px value
     * @description transform the dip value to px value
     * @author yuwei
     */
    public static int dip2px(Context context, float dipValue) {
        // scal 　密度。
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return dip value
     * @description transform the px value to dip value
     * @author yuwei
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }

    /**
     * @param context
     * @param pxValue
     * @return current screen px value
     * @description transform the hdpi px value to current screen px value
     * @author yuwei
     */
    public static int px2px(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / 1.5 * scale);

    }

    /**
     * @param context
     * @return current screen width
     * @description to call this method you can get the screen width of the display
     * @author yuwei
     */
    public static int getDisplayWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 打印获取的宽和高
        return dm.widthPixels;

    }

    /**
     * @param context
     * @return current screen height
     * @description to call this method you can get the screen height of the display
     * @author yuwei
     */
    public static int getDisplayHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 打印获取的宽和高
        return dm.heightPixels;

    }

    /**
     * @param context
     * @return the immei number
     * @description get the immei number of current devices
     * @author yuwei
     */
    public static String getIMEI(Context context) {
        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * @param context
     * @return current version code
     * @description to call this method you can get the current version code of the app,it may defined in the
     *              manifest.mxl
     * @author yuwei
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (Exception e) {
            LogUtils.loge(TAG, LogUtils.getThreadName(), e);

        }
        return versionCode;
    }

    /**
     * @param context
     * @return
     * @description to call this method you can get a string contained the immei number of the devices android
     *              the version name of the current app
     * @author yuwei
     */
    public static String getAppInfo(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("IMEI:");
        sb.append(getIMEI(context));
        sb.append(" Application versionName:");
        sb.append(getAppVersionName(context));
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param key
     * @param defalut
     * @return system properties
     * @description get the system properties
     * @author yuwei
     */
    public static String getSystemProperties(String key, String defalut) {
        String info = defalut;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Object obj = c.newInstance();
            Method method = c.getMethod("get", String.class, String.class);
            info = (String) method.invoke(obj, key, defalut);
        } catch (Exception e) {
            LogUtils.loge(TAG, LogUtils.getThreadName(), e);
            e.printStackTrace();
            info = defalut;
        }
        return info;
    }

    /**
     * @param str
     * @return
     * @description get the format time string
     * @author yuwei
     */
//    public static String getCurrentTimeStr(Context context) {
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
////        String time = context.getResources().getString(R.string.last_update_time) + format.format(date);
//        return time;
//    }

    /**
     * @param str
     * @return
     * @description get the format time string
     * @author yuwei
     */
    public static String getDataFormatStr(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * @param time
     * @return
     * @author yuwei
     * @description TODO is the day of time equal to current day
     */
    public static boolean isDateToday(long time) {
        long now = System.currentTimeMillis();
        long t1 = time / (1000 * 60 * 60 * 24);
        long t2 = now / (1000 * 60 * 60 * 24);
        return t1 == t2;
    }

    /**
     * @param context
     * @param view
     *            which the keybord bind to
     * @author yuwei
     * @description TODO make the keybord from shown to invisible
     */
    public static void hidenKeybord(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

//    public static String getSubNetwork(Context context) {
//        NetworkParser parser = new NetworkParser(context);
//        return parser.getNetworkType();
//    }
}
