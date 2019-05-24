package com.innovationai.pigweight.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @Author: Lucas.Cui
 * 时   间：2019/3/21
 * 简   述：<功能简述>
 */
public class SPUtils {
    private final static String spName = "pigInsurance";

    public static void putValue(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        }
        edit.apply();
    }

    public static Object getValue(Context context, String key, Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        }
        return null;
    }

    public static void clearSP(Context context) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void removeSP(Context context, String Key) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().remove(Key).apply();
    }

    public static Map<String, ?> getAllSP(Context context) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getAll();
    }
}


