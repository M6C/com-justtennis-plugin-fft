package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.network.HttpPostProxy;

public class AbstractFBService extends AbstractService {

    private static final String TAG = AbstractFBService.class.getName();

    static final String URL_ROOT = "https://www.facebook.com/";
    private static final String LOGON_SITE = "www.facebook.com";
    private static final int    LOGON_PORT = 443;
    private static final String LOGON_METHOD = "https";

    AbstractFBService(Context context) {
        super(context);
    }

    @Override
    HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = super.newHttpPostProxy();
        instance.setSite(LOGON_SITE)
                .setPort(LOGON_PORT)
                .setMethod(LOGON_METHOD);
        return instance;
    }
}
