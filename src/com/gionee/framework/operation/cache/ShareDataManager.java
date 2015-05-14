package com.gionee.framework.operation.cache;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareDataManager {

    public static final String NAME = "init";
    private static final String PREFNAME = "json";

    public static long getInterfaceDataVersionNumber(Context context, String key) {
        if (context == null) {
            return 1;
        }
        SharedPreferences preference = context.getSharedPreferences(NAME, 1);
        return preference.getLong(key, 1);
    }

    public static void setInterfaceDataVersionNumber(Context context, String key, long versionCode) {
        if (context == null) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(NAME, 1);
        preference.edit().putLong(key, versionCode).commit();
    }

    public static void saveDataAsInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();
    }

    public static int getDataAsInt(Context context, String key, int defValue) {
        int result = -1;
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        result = preferences.getInt(key, defValue);
        return result;
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        float result = preferences.getFloat(key, defaultValue);
        return result;
    }

    public static void putFloat(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        preferences.edit().putFloat(key, value).commit();
    }

    public static void saveDataAsLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        preferences.edit().putLong(key, value).commit();
    }

    public static long getDataAsLong(Context context, String key, long defValue) {
        long result = -1;
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        result = preferences.getLong(key, defValue);
        return result;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            preferences.edit().putBoolean(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {

        SharedPreferences preferences;
        try {
            preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            return preferences.getBoolean(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static void putString(Context context, String key, String value) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            preferences.edit().putString(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences preferences;
        try {
            preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            return preferences.getString(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void putJsonArray(Context context, String key, JSONArray js) {
        SharedPreferences settings = context.getSharedPreferences(PREFNAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, js.toString());
        editor.commit();
    }

    public static JSONArray getJSONArray(Context context, String key) throws JSONException {
        SharedPreferences settings = context.getSharedPreferences(PREFNAME, 0);
        return new JSONArray(settings.getString(key, "[]"));
    }

    public static void removeReferece(Context context, String key) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            preferences.edit().remove(key).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
