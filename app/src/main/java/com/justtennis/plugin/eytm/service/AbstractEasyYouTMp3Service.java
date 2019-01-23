package com.justtennis.plugin.eytm.service;

import android.content.Context;

import com.justtennis.plugin.shared.service.AbstractService;

public abstract class AbstractEasyYouTMp3Service extends AbstractService {

    private static final String TAG = AbstractEasyYouTMp3Service.class.getName();
    protected static final String URL_ROOT = "https://www.easy-youtube-mp3.com";

    protected AbstractEasyYouTMp3Service(Context context) {
        super(context);
    }
}