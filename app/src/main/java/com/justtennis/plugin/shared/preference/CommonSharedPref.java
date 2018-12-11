package com.justtennis.plugin.shared.preference;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

class CommonSharedPref {

    private static final String TAG = CommonSharedPref.class.getName();
    private static final Map<String, Object> cacheNoContext = new HashMap<>();

    @Nullable
    static String getString(Context context, String method, String key) {
        if (context == null) {
            logMe(method + " context is null");
            return (String)cacheNoContext.get(key);
        } else {
            return SharedPrefUtils.getStringPreference(context, key);
        }
    }

    static void setString(Context context, String value, String method, String key) {
        if (context == null) {
            logMe(method + " context is null");
            cacheNoContext.put(key, value);
        } else {
            SharedPrefUtils.setStringPreference(context, key, value);
        }
    }

    public static boolean getBoolean(Context context, String method, String key) {
        if (context == null) {
            logMe(method + " context is null");
            return cacheNoContext.containsKey(key) && (boolean) cacheNoContext.get(key);
        } else {
            return SharedPrefUtils.getBooleanPreference(context, key, false);
        }
    }

    public static void setBoolean(Context context, boolean value, String method, String key) {
        if (context == null) {
            logMe(method + " context is null");
            cacheNoContext.put(key, value);
        } else {
            SharedPrefUtils.setBooleanPreference(context, key, value);
        }
    }

    public static int getInteger(Context context, String method, String key) {
        return getInteger(context, method, key, -1);
    }

    public static int getInteger(Context context, String method, String key, int defaultValue) {
        if (context == null) {
            logMe(method + " context is null");
            return cacheNoContext.containsKey(key) ? (int) cacheNoContext.get(key) : -1;
        } else {
            return SharedPrefUtils.getIntegerPreference(context, key, defaultValue);
        }
    }

    public static void setInteger(Context context, int value, String method, String key) {
        if (context == null) {
            logMe(method + " context is null");
            cacheNoContext.put(key, value);
        } else {
            SharedPrefUtils.setIntegerPreference(context, key, value);
        }
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
