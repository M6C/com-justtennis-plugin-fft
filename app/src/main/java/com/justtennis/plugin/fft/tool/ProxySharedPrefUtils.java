package com.justtennis.plugin.fft.tool;

import android.content.Context;

public class ProxySharedPrefUtils {

    private static final String TAG = ProxySharedPrefUtils.class.getName();

    private static final String SHARED_PREF_PROXY_USE = "SHARED_PREF_PROXY_USE";
    private static final String SHARED_PREF_PROXY_SITE = "SHARED_PREF_PROXY_SITE";
    private static final String SHARED_PREF_PROXY_PORT = "SHARED_PREF_PROXY_PORT";
    private static final String SHARED_PREF_PROXY_USER = "SHARED_PREF_PROXY_USER";
    private static final String SHARED_PREF_PROXY_PWD = "SHARED_PREF_PROXY_PWD";

    private ProxySharedPrefUtils() {
    }

    public static boolean getUseProxy(Context context) {
        if (context == null) {
            logMe("getUseProxy context is null");
            return false;
        } else {
            return SharedPrefUtils.getBooleanPreference(context, SHARED_PREF_PROXY_USE, false);
        }
    }

    public static void setUseProxy(Context context, boolean useProxy) {
        if (context == null) {
            logMe("setSite context is null");
        } else {
            SharedPrefUtils.setBooleanPreference(context, SHARED_PREF_PROXY_USE, useProxy);
        }
    }

    public static String getSite(Context context) {
        if (context == null) {
            logMe("getSite context is null");
            return null;
        } else {
            return SharedPrefUtils.getStringPreference(context, SHARED_PREF_PROXY_SITE);
        }
    }

    public static void setSite(Context context, String site) {
        if (context == null) {
            logMe("setSite context is null");
        } else {
            SharedPrefUtils.setStringPreference(context, SHARED_PREF_PROXY_SITE, site);
        }
    }

    public static int getPort(Context context) {
        if (context == null) {
            logMe("getPort context is null");
            return -1;
        } else {
            return SharedPrefUtils.getIntegerPreference(context, SHARED_PREF_PROXY_PORT, -1);
        }
    }

    public static void setPort(Context context, int port) {
        if (context == null) {
            logMe("setPort context is null");
        } else {
            SharedPrefUtils.setIntegerPreference(context, SHARED_PREF_PROXY_PORT, port);
        }
    }

    public static String getUser(Context context) {
        if (context == null) {
            logMe("getUser context is null");
            return null;
        } else {
            return SharedPrefUtils.getStringPreference(context, SHARED_PREF_PROXY_USER);
        }
    }

    public static void setUser(Context context, String method) {
        if (context == null) {
            logMe("setUser context is null");
        } else {
            SharedPrefUtils.setStringPreference(context, SHARED_PREF_PROXY_USER, method);
        }
    }

    public static String getPwd(Context context) {
        if (context == null) {
            logMe("getPwd context is null");
            return null;
        } else {
            return SharedPrefUtils.getStringPreference(context, SHARED_PREF_PROXY_PWD);
        }
    }

    public static void setPwd(Context context, String pwd) {
        if (context == null) {
            logMe("setPwd context is null");
        } else {
            SharedPrefUtils.setStringPreference(context, SHARED_PREF_PROXY_PWD, pwd);
        }
    }

    public static void clean(Context context) {
        setUseProxy(context, false);
        setSite(context, "");
        setPort(context, -1);
        setUser(context, "");
        setPwd(context, "");
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
