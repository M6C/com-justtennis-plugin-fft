package com.justtennis.plugin.yout.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.tool.FileUtil;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.service.YouTServiceFindVideo;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class YoutGotoUrlTask extends AsyncTask<Void, String, YoutFindVideoResponse> {

    private static final String TAG = YoutGotoUrlTask.class.getName();

    private final YouTServiceFindVideo serviceFindVideo;
    private String url;

    protected YoutGotoUrlTask(Context context, String url) {
        serviceFindVideo = newFBServiceProfil(context);
        this.url = url;
    }

    @Override
    protected YoutFindVideoResponse doInBackground(Void... params) {
        try {
            ResponseHttp form = null;
            this.publishProgress("Info - Go to Url '" + url + "'");
            ResponseHttp formRedirect = serviceFindVideo.gotoUrl(form, url);

            if (formRedirect != null && formRedirect.body != null && !formRedirect.body.isEmpty() && formRedirect.statusCode == 200) {
//                try {
//                    FileUtil.writeDownloadFile(getClass().getClassLoader(), formRedirect.body, "YoutGotoUrlTask.html");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                this.publishProgress("Successfull - Go to Url '" + url + "' so Parsing Video");

                return serviceFindVideo.getFindPlaylist(formRedirect);
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Find Video message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return null;
    }

    private YouTServiceFindVideo newFBServiceProfil(Context context) {
        return YouTServiceFindVideo.newInstance(context);
    }
}
