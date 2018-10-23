package com.niuzj.corelibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    public static final String SP_NAME = "mini_brain";
    private static SharedPreferences sp;

    public static void saveString(Context context, String key, String value) {
        getSp(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSp(context).getString(key, defValue);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        getSp(context).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSp(context).getBoolean(key, defValue);
    }

    public static void saveInt(Context context, String key, int value) {
        getSp(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    public static void saveFloat(Context context, String key, float value) {
        getSp(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSp(context).getFloat(key, defValue);
    }

    public static void saveLong(Context context, String key, long value) {
        getSp(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSp(context).getLong(key, defValue);
    }


    public static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, 0);
        }
        return sp;
    }

}
