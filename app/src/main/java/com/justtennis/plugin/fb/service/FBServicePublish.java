package com.justtennis.plugin.fb.service;

import android.content.Context;

import com.justtennis.plugin.fb.converter.PublishFormResponseConverter;
import com.justtennis.plugin.fb.manager.SharingImageManager;
import com.justtennis.plugin.fb.manager.SharingUrlManager;
import com.justtennis.plugin.fb.manager.SharingYoutubeManager;
import com.justtennis.plugin.fb.parser.FBPublishParser;
import com.justtennis.plugin.fb.query.request.FBPublishFormRequest;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.util.HashMap;
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
        logMethod("submitForm");
        ResponseHttp ret = null;

        System.out.println("\n\n\n==============> FB Form Publish Action:" + form.action);

        Map<String, String> data = getData(form);

        if (!StringUtil.isBlank(form.action)) {
            ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }

    private Map<String, String> getData(FBPublishFormResponse publishFormResponse) {
        Map<String, String> data = new HashMap<>();
        String message = publishFormResponse.message.value;
        SharingUrlManager urlManager = SharingUrlManager.getInstance();
        if (urlManager.check(message)) {
            SharingYoutubeManager youtubeManager = SharingYoutubeManager.getInstance();
            String id = youtubeManager.getIdFromUrl(message);
            if (id != null) {
                message = youtubeManager.cleanUrl(message);
                data.putAll(getData(publishFormResponse, id));
            } else {
                SharingImageManager imageManager = SharingImageManager.getInstance();
            }
        }
        publishFormResponse.message.value = message;
        data.putAll(PublishFormResponseConverter.toDataMap(publishFormResponse));
        return data;
    }

    private Map<String, String> getData(FBPublishFormResponse publishFormResponse, String id) {
        SharingYoutubeManager youtubeManager = SharingYoutubeManager.getInstance();
        if (id.equals(publishFormResponse.publishId)) {
            if (publishFormResponse.publishTitle != null && !publishFormResponse.publishTitle.isEmpty()) {
                return youtubeManager.getData(id, publishFormResponse.publishTitle);
            }
        }
        return youtubeManager.getData(id, "");
    }
}
