package com.justtennis.plugin.yout.task;

import android.content.Context;
import android.os.AsyncTask;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yout.query.request.YouTFindVideoFormRequest;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.service.YouTServiceFindVideo;
import com.justtennis.plugin.yout.service.YoutServiceHomePage;

public abstract class YoutFindVideoTask extends AsyncTask<Void, String, YoutFindVideoResponse> {

    private static final String TAG = YoutFindVideoTask.class.getName();

    private final YoutServiceHomePage serviceHomePage;
    private final YouTServiceFindVideo serviceFindVideo;
    private String query;

    protected YoutFindVideoTask(Context context, String query) {
        serviceHomePage = newFBServiceHomePage(context);
        serviceFindVideo = newFBServiceProfil(context);
        this.query = query;
    }

    @Override
    protected YoutFindVideoResponse doInBackground(Void... params) {
        ResponseHttp form = null;
        this.publishProgress("Info - Navigate to HomePage");
        ResponseHttp formRedirect = serviceHomePage.navigateToHomePage(form);

        if(formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
            this.publishProgress("Successfull - Navigate to HomePage so Parsing HomePage");

            GenericFormResponse findFormResponse = serviceFindVideo.getFindForm(formRedirect, query);
            if (findFormResponse != null) {
                String value = findFormResponse.fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value;

                if(value != null && !value.isEmpty()) {
                    this.publishProgress("Successfull - Navigate to Profile so Parsing Profile Publication");

                    return serviceFindVideo.getFind(formRedirect);
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
        return null;
    }

    private YouTServiceFindVideo newFBServiceProfil(Context context) {
        return YouTServiceFindVideo.newInstance(context);
    }

    private YoutServiceHomePage newFBServiceHomePage(Context context) {
        return YoutServiceHomePage.newInstance(context);
    }
}
