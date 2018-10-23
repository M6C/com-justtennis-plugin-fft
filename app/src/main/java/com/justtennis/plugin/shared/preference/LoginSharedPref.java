package com.justtennis.plugin.shared.preference;

import android.content.Context;

public class LoginSharedPref {

    private static final String SHARED_PREF_COOKIE = "SHARED_PREF_COOKIE";
    private static final String SHARED_PREF_LOGIN = "SHARED_PREF_LOGIN";
    private static final String SHARED_PREF_PASWD = "SHARED_PREF_PASWD";
    private static final String SHARED_PREF_HOME_PAGE = "SHARED_PREF_HOME_PAGE";

    private LoginSharedPref() {
    }

    public static String getCookie(Context context) {
        return CommonSharedPref.getString(context, "getCookie", SHARED_PREF_COOKIE);
    }

    public static void setCookie(Context context, String cookie) {
        CommonSharedPref.setString(context, cookie, "setCookie", SHARED_PREF_COOKIE);
    }

    public static String getLogin(Context context) {
        return CommonSharedPref.getString(context, "getLogin", SHARED_PREF_LOGIN);
    }

    public static void setLogin(Context context, String login) {
        CommonSharedPref.setString(context, login, "setLogin", SHARED_PREF_LOGIN);
    }

    public static String getPwd(Context context) {
        return CommonSharedPref.getString(context, "getCookie", SHARED_PREF_PASWD);
    }

    public static void setPwd(Context context, String cookie) {
        CommonSharedPref.setString(context, cookie, "setCookie", SHARED_PREF_PASWD);
    }

    public static String getHomePage(Context context) {
        return CommonSharedPref.getString(context, "getHomePage", SHARED_PREF_HOME_PAGE);
    }

    public static void setHomePage(Context context, String homePage) {
        CommonSharedPref.setString(context, homePage, "setHomePage", SHARED_PREF_HOME_PAGE);
    }

    public static void cleanSecurity(Context context) {
        setCookie(context, "");
        setHomePage(context, "");
    }
}
