package com.justtennis.plugin.yout.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yout.query.request.YouTFindVideoFormRequest;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.service.YouTServiceFindVideo;
import com.justtennis.plugin.yout.service.YoutServiceHomePage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public abstract class YoutFindVideoTask extends AsyncTask<Void, String, YoutFindVideoResponse> {

    private static final String TAG = YoutFindVideoTask.class.getName();

    private final YoutServiceHomePage serviceHomePage;
    private final YouTServiceFindVideo serviceFindVideo;
    private Context context;
    private String query;

    protected YoutFindVideoTask(Context context, String query) {
        serviceHomePage = newFBServiceHomePage(context);
        serviceFindVideo = newFBServiceProfil(context);
        this.context = context;
        this.query = query;
    }

    @Override
    protected YoutFindVideoResponse doInBackground(Void... params) {
        try {
            ResponseHttp form = null;
            this.publishProgress("Info - Navigate to HomePage");
            ResponseHttp formRedirect = serviceHomePage.navigateToHomePage(form);

            if (formRedirect != null && formRedirect.body != null && formRedirect.statusCode == 200) {
                this.publishProgress("Successfull - Navigate to HomePage so Parsing HomePage");

                GenericFormResponse findFormResponse = serviceFindVideo.getFindForm(formRedirect, query);
                if (findFormResponse != null) {
                    String value = findFormResponse.fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value;

                    if (value != null && !value.isEmpty()) {
                        ResponseHttp submitForm = serviceFindVideo.submitFindForm(form, findFormResponse);

                        if (submitForm.body != null && !submitForm.body.isEmpty()) {
//                            write(submitForm.body);

                            this.publishProgress("Successfull - Navigate to Profile so Parsing Profile Publication");

                            return serviceFindVideo.getFind(submitForm);
                        } else {
                            this.publishProgress("Failed - Submit Form");
                        }
                    } else {
                        this.publishProgress("Failed - Find Form");
                    }

                } else {
                    this.publishProgress("Failed - Parsing HomePage");
                }

                return null;
            } else {
                this.publishProgress("Failed - Navigate to HomePage");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Find Video message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return null;
    }

    private void write(String body) {
        String path = "/storage/emulated/0/Download";
//        File path = context.getFilesDir();
        String filename = "YouTServiceFindVideo_getFind.json";

        try {
//            File expected = context.getFileStreamPath(filename);
            File expected = new File(path, filename);
            System.err.println("==========> writeResourceFile:" + expected.getAbsolutePath());
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(expected);
                pw.print(body);
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }


//            FileOutputStream outputStream;
//            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(body.getBytes());
//            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private YouTServiceFindVideo newFBServiceProfil(Context context) {
        return YouTServiceFindVideo.newInstance(context);
    }

    private YoutServiceHomePage newFBServiceHomePage(Context context) {
        return YoutServiceHomePage.newInstance(context);
    }
}
