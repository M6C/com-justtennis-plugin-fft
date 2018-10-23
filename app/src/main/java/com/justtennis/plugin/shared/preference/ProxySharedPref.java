package com.justtennis.plugin.shared.preference;

import android.content.Context;

public class ProxySharedPref {

    private static final String SHARED_PREF_PROXY_USE = "SHARED_PREF_PROXY_USE";
    private static final String SHARED_PREF_PROXY_SITE = "SHARED_PREF_PROXY_SITE";
    private static final String SHARED_PREF_PROXY_PORT = "SHARED_PREF_PROXY_PORT";
    private static final String SHARED_PREF_PROXY_USER = "SHARED_PREF_PROXY_USER";
    private static final String SHARED_PREF_PROXY_PASWD = "SHARED_PREF_PROXY_PASWD";
    private static final int PROXY_PORT_DEFAULT = 8080;

    private ProxySharedPref() {
    }

    public static boolean getUseProxy(Context context) {
        return CommonSharedPref.getBoolean(context, "getUseProxy", SHARED_PREF_PROXY_USE);
    }

    public static void setUseProxy(Context context, boolean useProxy) {
        CommonSharedPref.setBoolean(context, useProxy, "setUseProxy", SHARED_PREF_PROXY_USE);
    }

    public static String getSite(Context context) {
        return CommonSharedPref.getString(context, "getSite", SHARED_PREF_PROXY_SITE);
    }

    public static void setSite(Context context, String site) {
        CommonSharedPref.setString(context, site, "setSite", SHARED_PREF_PROXY_SITE);
    }

    public static int getPort(Context context) {
        return CommonSharedPref.getInteger(context, "getPort", SHARED_PREF_PROXY_PORT, PROXY_PORT_DEFAULT);
    }

    public static void setPort(Context context, int port) {
        CommonSharedPref.setInteger(context, port, "setPort", SHARED_PREF_PROXY_PORT);
    }

    public static String getUser(Context context) {
        return CommonSharedPref.getString(context, "getUser", SHARED_PREF_PROXY_USER);
    }

    public static void setUser(Context context, String user) {
        CommonSharedPref.setString(context, user, "setUser", SHARED_PREF_PROXY_USER);
    }

    public static String getPwd(Context context) {
        return CommonSharedPref.getString(context, "getPwd", SHARED_PREF_PROXY_PASWD);
    }

    public static void setPwd(Context context, String pwd) {
        CommonSharedPref.setString(context, pwd, "setPwd", SHARED_PREF_PROXY_PASWD);
    }

    public static void clean(Context context) {
        setUseProxy(context, false);
        setSite(context, "");
        setPort(context, PROXY_PORT_DEFAULT);
        setUser(context, "");
        setPwd(context, "");
    }
}
