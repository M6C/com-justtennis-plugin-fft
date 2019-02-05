package com.justtennis.plugin.yout.service;

import android.content.Context;

import org.cameleon.android.shared.network.model.ResponseHttp;

public class YoutServiceLogin extends AbstractYouTService {

    private static final String TAG = YoutServiceLogin.class.getName();

    private YoutServiceLogin(Context context) {
        super(context);
    }

    public static YoutServiceLogin newInstance(Context context) {
        return new YoutServiceLogin(context);
    }

    public ResponseHttp navigateToHomePage() {
        logMethod("navigateToHomePage");
        return doGet(URL_ROOT);
    }
}
