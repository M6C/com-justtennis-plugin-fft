package com.justtennis.plugin.fft.service;

import android.content.Context;
import android.util.Log;

import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.manager.InviteManager;
import com.justtennis.plugin.shared.network.HttpPostProxy;
import com.justtennis.plugin.shared.service.AbstractService;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class AbstractFFTService extends AbstractService {

    private static final String TAG = AbstractFFTService.class.getName();

    static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;
    private static final String LOGON_METHOD = "https";

    AbstractFFTService(Context context) {
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

    public static Date getDateFromFFT(String date) {
        Date ret = new Date();
        try {
            ret = MatchContent.sdfFFT.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, MessageFormat.format("Formatting match.ret:{0}", date), e);
        }
        return ret;
    }

    public static InviteManager.SCORE_RESULT getScoreResultFromFFT(String vicDef) {
        return vicDef.startsWith("D") ? InviteManager.SCORE_RESULT.DEFEAT :  InviteManager.SCORE_RESULT.VICTORY;
    }
}
