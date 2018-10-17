package com.justtennis.plugin.fft.service;

import android.content.Context;

public class AbstractFBService extends AbstractService {

    private static final String TAG = AbstractFBService.class.getName();

    static final String URL_ROOT = "https://www.facebook.com/";

    AbstractFBService(Context context) {
        super(context);
    }
}
