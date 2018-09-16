
package com.xzwzz.orange.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xzwzz.orange.AppContext;


/**
 * sharedPrefence工具类
 *
 * @author YQ
 * @since 2016-08-01
 */
public class SharePrefUtil {
    // 公共的sharedPrefence文件名
    private static final String SHAREDPREFERENCES_NAME = "config";

    private static SharedPreferences sp = null;

    public static void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static void putLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    public static Long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public static void putFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static void clear() {
        sp.edit().clear();
    }

    public static void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public static boolean contains(String key) {
        return sp.contains(key);
    }

    public static void initSharedPref() {
        sp = AppContext.getInstance().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

}
