package com.justtennis.plugin.mytd.service;

import android.content.Context;

import com.justtennis.plugin.shared.network.model.ResponseHttp;

public class Mp3YouTDwnServiceLogin extends AbstractMp3YouTDwnService {

    private static final String TAG = Mp3YouTDwnServiceLogin.class.getName();

    private Mp3YouTDwnServiceLogin(Context context) {
        super(context);
    }

    public static Mp3YouTDwnServiceLogin newInstance(Context context) {
        return new Mp3YouTDwnServiceLogin(context);
    }

    public ResponseHttp navigateToHomePage() {
        logMethod("navigateToHomePage");
        return doGet(URL_ROOT);
    }
}
