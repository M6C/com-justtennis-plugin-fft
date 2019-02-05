package com.justtennis.plugin.mytd.service;

import android.content.Context;

import org.cameleon.android.shared.service.AbstractService;

public abstract class AbstractMp3YouTDwnService extends AbstractService {

    private static final String TAG = AbstractMp3YouTDwnService.class.getName();
    protected static final String URL_ROOT = "https://savetomp3.cc/fr/";

    protected AbstractMp3YouTDwnService(Context context) {
        super(context);
    }
}