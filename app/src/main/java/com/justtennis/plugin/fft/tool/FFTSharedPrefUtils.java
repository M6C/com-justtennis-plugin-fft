package com.justtennis.plugin.fft.tool;

import android.content.Context;

public class FFTSharedPrefUtils {

    private static final String TAG = FFTSharedPrefUtils.class.getName();

    private static final String SHARED_PREF_FFT_COOKIE = "SHARED_PREF_FFT_COOKIE";
    private static final String SHARED_PREF_FFT_HOME_PAGE = "SHARED_PREF_FFT_HOME_PAGE";

    private FFTSharedPrefUtils() {
    }

    public static String getCookie(Context context) {
        if (context == null) {
            logMe("getCookie context is null");
            return null;
        } else {
            return SharedPrefUtils.getStringPreference(context, SHARED_PREF_FFT_COOKIE);
        }
    }

    public static void setCookie(Context context, String cookie) {
        if (context == null) {
            logMe("setCookie context is null");
        } else {
            SharedPrefUtils.setStringPreference(context, SHARED_PREF_FFT_COOKIE, cookie);
        }
    }

    public static String getHomePage(Context context) {
        if (context == null) {
            logMe("getHomePage context is null");
            return null;
        } else {
            return SharedPrefUtils.getStringPreference(context, SHARED_PREF_FFT_HOME_PAGE);
        }
    }

    public static void setHomePage(Context context, String homePage) {
        if (context == null) {
            logMe("setHomePage context is null");
        } else {
            SharedPrefUtils.setStringPreference(context, SHARED_PREF_FFT_HOME_PAGE, homePage);
        }
    }

    public static void clean(Context context) {
        setCookie(context, "");
        setHomePage(context, "");
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
