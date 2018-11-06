package com.justtennis.plugin.fft.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.query.response.PalmaresResponse;
import com.justtennis.plugin.fft.service.FFTServiceLogin;
import com.justtennis.plugin.fft.service.FFTServicePalmares;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class MillesimeTask extends AsyncTask<Void, String, List<PalmaresMillesimeResponse.Millesime>> {

    private static final String TAG = MillesimeTask.class.getName();

    @SuppressLint("StaticFieldLeak")
    private Context context;

    protected MillesimeTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<PalmaresMillesimeResponse.Millesime> doInBackground(Void... params) {
        List<PalmaresMillesimeResponse.Millesime> ret = new ArrayList<>();
        try {
            FFTServiceLogin fftServiceLogin = newFFTService();
            FFTServicePalmares fftServicePalmares = newFFTServicePalmares();
            this.publishProgress("Info - Navigate to HomePage");
            ResponseHttp home = fftServiceLogin.navigateToHomePage(null);

            this.publishProgress("Info - Navigate to Parsing Palmares Form");
            PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

            if (palmaresResponse != null && palmaresResponse.action != null) {
                this.publishProgress("Successfull - Getting Palmares URL so Navigate to Palmares");
                ResponseHttp palmares = fftServicePalmares.navigateToPalmares(null, palmaresResponse);
                if (palmares.body != null) {
                    this.publishProgress("Successfull - Navigate to Palmares so Parsing Millesime");
                    PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
                    if (palmaresMillesimeResponse != null && !palmaresMillesimeResponse.listMillesime.isEmpty()) {
                        ret = palmaresMillesimeResponse.listMillesime;
                    } else {
                        Log.w(TAG, "getPalmaresMillesime is empty");
                    }
                } else {
                    this.publishProgress("Failed - Navigate to Palmares");
                    Log.w(TAG, "navigateToPalmares  body is empty");
                }
            } else {
                this.publishProgress("Failed - Getting Palmares URL");
                Log.w(TAG, "getPalmares action is empty");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Millesime message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FFTServiceLogin newFFTService() {
        return FFTServiceLogin.newInstance(context);
    }

    private FFTServicePalmares newFFTServicePalmares() {
        return FFTServicePalmares.newInstance(context);
    }
}
