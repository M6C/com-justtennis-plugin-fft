package com.justtennis.plugin.fft.tool;

import android.content.Context;

public class FFTSharedPrefUtils {

    private static final String SHARED_PREF_FFT_COOKIE = "SHARED_PREF_FFT_COOKIE";

    private FFTSharedPrefUtils() {
    }

    public static String getCookie(Context context) {
        return SharedPrefUtils.getStringPreference(context, SHARED_PREF_FFT_COOKIE);
    }

    public static void setCookie(Context context, String cookie) {
        SharedPrefUtils.setStringPreference(context, SHARED_PREF_FFT_COOKIE, cookie);
    }
}
