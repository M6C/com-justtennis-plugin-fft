package com.justtennis.plugin.yout.preference;

import android.content.Context;

import com.justtennis.plugin.shared.preference.CommonSharedPref;

public class YouTubeSharedPref {

    private static final String SHARED_PREF_VIDEO_PATH = "SHARED_PREF_VIDEO_PATH_";

    private YouTubeSharedPref() {
    }

    public static String getVideoPath(Context context, String videoId) {
        return CommonSharedPref.getString(context, "getVideoPath", SHARED_PREF_VIDEO_PATH+videoId);
    }

    public static void setVideoPath(Context context, String videoId, String path) {
        CommonSharedPref.setString(context, path, "setCookie", SHARED_PREF_VIDEO_PATH+videoId);
    }
}
