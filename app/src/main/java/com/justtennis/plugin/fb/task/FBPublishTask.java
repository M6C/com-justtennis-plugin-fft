package com.justtennis.plugin.fb.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.fb.service.FBServiceHomePage;
import com.justtennis.plugin.fb.service.FBServicePublish;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

public abstract class FBPublishTask extends AsyncTask<String, String, Boolean> {

    private static final String TAG = FBPublishTask.class.getName();

    private final FBServiceHomePage fbServiceHomePage;
    private final FBServicePublish fbServicePublish;

    protected FBPublishTask(Context context) {
        fbServiceHomePage = newFBServiceHomePage(context);
        fbServicePublish = newFBServicePublish(context);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (strings == null || strings.length ==0) {
            return false;
        }
        boolean ret = false;
        try {
            ResponseHttp form = null;
            this.publishProgress("Info - Navigate to HomePage");
            ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

            if(formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
                this.publishProgress("Successfull - Navigate to HomePage so Parsing Publish Form");

                for (String str : strings) {
                    FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
                    if (publishFormResponse != null && publishFormResponse.message != null) {
                        this.publishProgress("Successfull - Parsing Publish Form so Submitting Form");
                        publishFormResponse.message.value = str;

                        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

                        System.out.println("testSubmitForm body:" + submitFormResponse.body);

                        ret = submitFormResponse.statusCode == 302;
                    } else {
                        this.publishProgress("Failed - Parsing Publish Form");
                    }
                    if (!ret) {
                        break;
                    }
                }
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Publishing message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FBServiceHomePage newFBServiceHomePage(Context context) {
        return FBServiceHomePage.newInstance(context);
    }

    private FBServicePublish newFBServicePublish(Context context) {
        return FBServicePublish.newInstance(context);
    }
}
