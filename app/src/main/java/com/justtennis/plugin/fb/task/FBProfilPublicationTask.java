package com.justtennis.plugin.fb.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.fb.service.FBServiceHomePage;
import com.justtennis.plugin.fb.service.FBServiceProfil;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.tool.FileUtil;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

public abstract class FBProfilPublicationTask extends AsyncTask<Void, String, FBProfilPublicationResponse> {

    private static final String TAG = FBProfilPublicationTask.class.getName();

    private final FBServiceHomePage fbServiceHomePage;
    private final FBServiceProfil fbServiceProfil;

    protected FBProfilPublicationTask(Context context) {
        fbServiceHomePage = newFBServiceHomePage(context);
        fbServiceProfil = newFBServiceProfil(context);
    }

    @Override
    protected FBProfilPublicationResponse doInBackground(Void... params) {
        try {
            ResponseHttp form = null;
            this.publishProgress("Info - Navigate to HomePage");
            ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

            if(formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
                this.publishProgress("Successfull - Navigate to HomePage so Parsing HomePage");

                FBHomePageResponse homePageResponse = FBServiceHomePage.getHomePage(formRedirect);
                if (homePageResponse != null && homePageResponse.linkProfil != null && !homePageResponse.linkProfil.isEmpty()) {
                    ResponseHttp profileHttpResponse = fbServiceProfil.navigateToProfil(form, homePageResponse);

                    if(profileHttpResponse != null && profileHttpResponse.body != null && profileHttpResponse.statusCode == 200) {
                        this.publishProgress("Successfull - Navigate to Profile so Parsing Profile Publication");

                        return fbServiceProfil.getProfilPublication(profileHttpResponse);
                    } else {
                        this.publishProgress("Failed - Navigate to Profile");
                    }

                } else {
                    this.publishProgress("Failed - Parsing HomePage");
                }

                return null;
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Publishing message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return null;
    }

    private FBServiceProfil newFBServiceProfil(Context context) {
        return FBServiceProfil.newInstance(context);
    }

    private FBServiceHomePage newFBServiceHomePage(Context context) {
        return FBServiceHomePage.newInstance(context);
    }
}
