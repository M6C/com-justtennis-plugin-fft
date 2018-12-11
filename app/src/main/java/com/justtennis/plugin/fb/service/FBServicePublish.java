package com.justtennis.plugin.fb.service;

import android.content.Context;

import com.justtennis.plugin.fb.converter.PublishFormResponseConverter;
import com.justtennis.plugin.fb.parser.FBPublishParser;
import com.justtennis.plugin.fb.query.request.FBPublishFormRequest;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FBServicePublish extends AbstractFBService {

    private static final String TAG = FBServicePublish.class.getName();

    private FBServicePublish(Context context) {
        super(context);
    }

    public static FBServicePublish newInstance(Context context) {
        return new FBServicePublish(context);
    }

    public FBPublishFormResponse getForm(ResponseHttp homePageResponse) {
        logMethod("getForm");
        return FBPublishParser.getInstance().parseForm(homePageResponse.body, new FBPublishFormRequest());
    }

    public ResponseHttp submitForm(ResponseHttp loginFormResponse, FBPublishFormResponse form) throws NotConnectedException {
        return submitForm(loginFormResponse, form);
    }

    public ResponseHttp submitForm(ResponseHttp loginFormResponse, FBPublishFormResponse form, Map<String, String> extraData) throws NotConnectedException {
        logMethod("submitForm");
        ResponseHttp ret = null;

        System.out.println("\n\n\n==============> FB Form Publish Action:" + form.action);

        Map<String, String> data = PublishFormResponseConverter.toDataMap(form);
        if (extraData != null && !extraData.isEmpty()) {
            data.putAll(extraData);
        }
        if (!StringUtil.isBlank(form.action)) {
            ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }
}
