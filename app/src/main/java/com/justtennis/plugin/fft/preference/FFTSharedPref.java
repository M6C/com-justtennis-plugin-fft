package com.justtennis.plugin.fft.preference;

import android.content.Context;

public class FFTSharedPref {

    private static final String SHARED_PREF_FFT_COOKIE = "SHARED_PREF_FFT_COOKIE";
    private static final String SHARED_PREF_FFT_LOGIN = "SHARED_PREF_FFT_LOGIN";
    private static final String SHARED_PREF_FFT_PASWD = "SHARED_PREF_FFT_PASWD";
    private static final String SHARED_PREF_FFT_HOME_PAGE = "SHARED_PREF_FFT_HOME_PAGE";

    private FFTSharedPref() {
    }

    public static String getCookie(Context context) {
        return CommonSharedPref.getString(context, "getCookie", SHARED_PREF_FFT_COOKIE);
    }

    public static void setCookie(Context context, String cookie) {
        CommonSharedPref.setString(context, cookie, "setCookie", SHARED_PREF_FFT_COOKIE);
    }

    public static String getLogin(Context context) {
        return CommonSharedPref.getString(context, "getLogin", SHARED_PREF_FFT_LOGIN);
    }

    public static void setLogin(Context context, String login) {
        CommonSharedPref.setString(context, login, "setLogin", SHARED_PREF_FFT_LOGIN);
    }

    public static String getPwd(Context context) {
        return CommonSharedPref.getString(context, "getCookie", SHARED_PREF_FFT_PASWD);
    }

    public static void setPwd(Context context, String cookie) {
        CommonSharedPref.setString(context, cookie, "setCookie", SHARED_PREF_FFT_PASWD);
    }

    public static String getHomePage(Context context) {
        return CommonSharedPref.getString(context, "getHomePage", SHARED_PREF_FFT_HOME_PAGE);
    }

    public static void setHomePage(Context context, String homePage) {
        CommonSharedPref.setString(context, homePage, "setHomePage", SHARED_PREF_FFT_HOME_PAGE);
    }

    public static void clean(Context context) {
        setCookie(context, "");
        setLogin(context, "");
        setPwd(context, "");
        setHomePage(context, "");
    }
}
