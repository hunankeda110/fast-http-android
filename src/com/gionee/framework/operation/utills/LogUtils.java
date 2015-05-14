package com.gionee.framework.operation.utills;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

public class LogUtils {
    private static final String GLOBAL_TAG = "GN_GOU";
    private static final String SAVELOG_FILE_NAME = "GN_FunShopping_log.txt";
    private static final String ENABLE_SAVELOG_FLAG_FOLDER = "gngou1234567890savelog";
    private static final String TAG = ".LogUtils";
    private static boolean sEnableLog = true;
    private static boolean sIsSaveLog = false;

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");

    public static void loadInitConfigs() {
        Log.d(GLOBAL_TAG + TAG, "Load init configs ...");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = getExternalStorageDirectory();
            File saveLogFlagFile = new File(sdCardDir + ENABLE_SAVELOG_FLAG_FOLDER);
            if (saveLogFlagFile.exists()) {
                Log.d(GLOBAL_TAG + TAG, "My save log flag is true *-_-*");
                LogUtils.sIsSaveLog = true;
            }
        }

    }

    public static void log(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.i(GLOBAL_TAG + "." + tag, "" + msg);
            if (sIsSaveLog) {
                try {
                    saveToSDCard(formatLog(msg, tag, "i"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logv(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.v(GLOBAL_TAG + "." + tag, "" + msg);
            if (sIsSaveLog) {
                try {
                    saveToSDCard(formatLog(msg, tag, "V"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logd(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.d(GLOBAL_TAG + "." + tag, msg);
            if (sIsSaveLog) {
                try {
                    saveToSDCard(formatLog(msg, tag, "D"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loge(String tag, String msg) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.e(GLOBAL_TAG + "." + tag + ".E", "" + msg);
            if (sIsSaveLog) {
                try {
                    saveToSDCard(formatLog(msg, tag, "E"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loge(String tag, String msg, Throwable e) {
        if (sEnableLog) {
            if (null == msg) {
                msg = "";
            }
            Log.e(GLOBAL_TAG + "." + tag, "" + msg, e);
            if (sIsSaveLog) {
                try {
                    saveToSDCard(formatLog(msg, tag, "E"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public static void saveToSDCard(String content) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String sdCardDir = getExternalStorageDirectory();
                File file = new File(sdCardDir + File.separator + ENABLE_SAVELOG_FLAG_FOLDER,
                        SAVELOG_FILE_NAME);
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(content.getBytes(Charset.forName("UTF-8")));
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFunctionName() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
            sb.append("()");
            sb.append(" ");
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static String getThreadName() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(Thread.currentThread().getName());
            sb.append("-> ");
            sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
            sb.append("()");
            sb.append(" ");
        } catch (Exception e) {
            LogUtils.loge(GLOBAL_TAG + TAG, e.getMessage());
        }
        return sb.toString();
    }

    private static String formatLog(String log, String type, String level) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        synchronized (sFormatter) {
            builder.append(sFormatter.format(Calendar.getInstance().getTime()));
        }
        builder.append("][");
        builder.append(type);
        builder.append("][");
        builder.append(level);
        builder.append("]");
        builder.append(log);
        builder.append("\n");
        return builder.toString();
    }

    private static String getExternalStorageDirectory() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith(File.separator)) {
            rootpath += File.separator;
        }
        // Log.d(GLOBAL_TAG + TAG, "getExternalStorageDirectory() path = " +
        // rootpath);
        return rootpath;
    }

    public static String getClassName() {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("-> ");
            sb.append(Thread.currentThread().getStackTrace()[2].getClassName());
            sb.append(".");
        } catch (Exception e) {

        }
        return sb.toString();
    }
}
