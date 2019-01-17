package com.justtennis.plugin.yout.service;

import android.content.Context;
import android.util.Log;

import com.justtennis.plugin.fft.common.FFTConfiguration;
import com.justtennis.plugin.fft.manager.InviteManager;
import com.justtennis.plugin.shared.network.HttpPostProxy;
import com.justtennis.plugin.shared.service.AbstractService;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class AbstractYouTService extends AbstractService {

    private static final String TAG = AbstractYouTService.class.getName();

    static final String URL_ROOT = "https://www.youtube.com/";
    private static final String LOGON_SITE = "www.youtube.com";
    private static final int    LOGON_PORT = 80;
    private static final String LOGON_METHOD = "https";

    AbstractYouTService(Context context) {
        super(context);
    }

    @Override
    protected HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = super.newHttpPostProxy();
        instance.setSite(LOGON_SITE)
                .setPort(LOGON_PORT)
                .setMethod(LOGON_METHOD);
        return instance;
    }
}
