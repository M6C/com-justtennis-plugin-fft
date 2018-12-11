package com.justtennis.plugin.fb.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fb.dto.PublicationDto;
import com.justtennis.plugin.fb.enums.STATUS_PUBLICATION;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.fb.service.FBServiceHomePage;
import com.justtennis.plugin.fb.service.FBServicePublish;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yt.manager.YoutubeManager;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public abstract class FBPublishTask extends AsyncTask<PublicationDto, Serializable, Boolean> {

    private static final String TAG = FBPublishTask.class.getName();

    private final FBServiceHomePage fbServiceHomePage;
    private final FBServicePublish fbServicePublish;
    private FBPublishFormResponse publishFormResponse;

    protected FBPublishTask(Context context, FBPublishFormResponse publishFormResponse) {
        fbServiceHomePage = newFBServiceHomePage(context);
        fbServicePublish = newFBServicePublish(context);
        this.publishFormResponse = publishFormResponse;
    }

    @Override
    protected Boolean doInBackground(PublicationDto... dto) {
        if (dto == null || dto.length ==0) {
            return false;
        }
        int cntPublished = 0;
        try {
            ResponseHttp form = null;
            if (publishFormResponse == null) {
                this.publishProgress("Info - Navigate to HomePage");
                ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

                if (formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
                    this.publishProgress("Successfull - Navigate to HomePage so Parsing Publish Form");

                    publishFormResponse = fbServicePublish.getForm(formRedirect);
                }
            }

            if (publishFormResponse != null) {
                for (PublicationDto d : dto) {
                    d.statusPublication = STATUS_PUBLICATION.PENDING;
                    this.publishProgress(d);
                    if (publishFormResponse != null && publishFormResponse.message != null) {
                        this.publish(form, d, publishFormResponse);
                        cntPublished++;
                    } else {
                        d.statusPublication = STATUS_PUBLICATION.FAILED;
                        this.publishProgress("Failed - Parsing Publish Form");
                    }
                    this.publishProgress(d);
                }
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Publishing message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return cntPublished == dto.length;
    }

    private void publish(ResponseHttp form, PublicationDto d, FBPublishFormResponse publishFormResponse) throws NotConnectedException {
        this.publishProgress("Successfull - Parsing Publish Form so Submitting Form");
        ResponseHttp submitFormResponse;

        String message = d.message;
        Map<String, String> data = null;
        YoutubeManager youtubeManager = YoutubeManager.getInstance();
        String id = youtubeManager.getIdFromUrl(message);
        if (id != null) {
            message = youtubeManager.cleanUrl(message);
            data = youtubeManager.getData(id, message);
        }
        publishFormResponse.message.value = message;
        submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse, data);

        if (submitFormResponse.statusCode == 302) {
            d.publishDate = new Date();
            d.statusPublication = STATUS_PUBLICATION.PUBLISHED;
        } else {
            d.statusPublication = STATUS_PUBLICATION.FAILED;
        }
        this.publishProgress(d);
    }

    private FBServiceHomePage newFBServiceHomePage(Context context) {
        return FBServiceHomePage.newInstance(context);
    }

    private FBServicePublish newFBServicePublish(Context context) {
        return FBServicePublish.newInstance(context);
    }
}
