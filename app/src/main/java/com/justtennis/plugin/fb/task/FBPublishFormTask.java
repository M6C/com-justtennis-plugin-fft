package com.justtennis.plugin.fb.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.fb.service.FBServiceHomePage;
import com.justtennis.plugin.fb.service.FBServicePublish;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

public abstract class FBPublishFormTask extends AsyncTask<Void, String, FBPublishFormResponse> {

    private static final String TAG = FBPublishFormTask.class.getName();

    private final FBServiceHomePage fbServiceHomePage;
    private final FBServicePublish fbServicePublish;

    protected FBPublishFormTask(Context context) {
        fbServiceHomePage = newFBServiceHomePage(context);
        fbServicePublish = newFBServicePublish(context);
    }

    @Override
    protected FBPublishFormResponse doInBackground(Void... params) {
        try {
            ResponseHttp form = null;
            this.publishProgress("Info - Navigate to HomePage");
            ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

            if(formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
                this.publishProgress("Successfull - Navigate to HomePage so Parsing Publish Form");

                return fbServicePublish.getForm(formRedirect);
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Publishing message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return null;
    }

    private FBServiceHomePage newFBServiceHomePage(Context context) {
        return FBServiceHomePage.newInstance(context);
    }

    private FBServicePublish newFBServicePublish(Context context) {
        return FBServicePublish.newInstance(context);
    }
}
