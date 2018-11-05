package com.justtennis.plugin.fb.task;

import android.content.Context;
import android.os.AsyncTask;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.fb.service.FBServiceHomePage;
import com.justtennis.plugin.fb.service.FBServicePublish;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

public abstract class FBPublishTask extends AsyncTask<String, Void, Boolean> {

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
            ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

            for (String str : strings) {
                FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
                if (publishFormResponse != null && publishFormResponse.message != null) {
                    publishFormResponse.message.value = str;

                    ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

                    System.out.println("testSubmitForm body:" + submitFormResponse.body);

                    ret = submitFormResponse.statusCode == 302;
                } else {
                    ret = false;
                }
                if (!ret) {
                    break;
                }
            }
        } catch (NotConnectedException e) {
            e.printStackTrace();
            ret = false;
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
