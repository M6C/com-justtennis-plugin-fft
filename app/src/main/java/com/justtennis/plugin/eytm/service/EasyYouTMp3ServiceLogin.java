package com.justtennis.plugin.eytm.service;

import android.content.Context;

import org.cameleon.android.shared.network.model.ResponseHttp;

public class EasyYouTMp3ServiceLogin extends AbstractEasyYouTMp3Service {

    private static final String TAG = EasyYouTMp3ServiceLogin.class.getName();

    private EasyYouTMp3ServiceLogin(Context context) {
        super(context);
    }

    public static EasyYouTMp3ServiceLogin newInstance(Context context) {
        return new EasyYouTMp3ServiceLogin(context);
    }

    public ResponseHttp navigateToVideo(String id) {
        logMethod("navigateToVideo");
        return doGet(URL_ROOT + "/download.php?v=" + id);
    }
}
