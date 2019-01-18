package com.justtennis.plugin.yout.service;

import android.content.Context;

import com.justtennis.plugin.shared.network.model.ResponseHttp;

public class YoutServiceHomePage extends AbstractYouTService {

    private static final String TAG = YoutServiceHomePage.class.getName();

    private YoutServiceHomePage(Context context) {
        super(context);
    }

    public static YoutServiceHomePage newInstance(Context context) {
        return new YoutServiceHomePage(context);
    }

    public ResponseHttp navigateToHomePage(ResponseHttp loginFormResponse) {
        logMethod("navigateToHomePage");
        return doGet(URL_ROOT);
    }
}
