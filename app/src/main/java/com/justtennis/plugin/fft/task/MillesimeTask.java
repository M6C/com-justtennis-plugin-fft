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
public abstract class MillesimeTask extends AsyncTask<Void, Void, List<PalmaresMillesimeResponse.Millesime>> {

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
            ResponseHttp home = fftServiceLogin.navigateToHomePage(null);

            PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

            if (palmaresResponse != null && palmaresResponse.action != null) {
                ResponseHttp palmares = fftServicePalmares.navigateToPalmares(null, palmaresResponse);
                if (palmares.body != null) {
                    PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
                    if (palmaresMillesimeResponse != null && !palmaresMillesimeResponse.listMillesime.isEmpty()) {
                        ret = palmaresMillesimeResponse.listMillesime;
                    } else {
                        Log.w(TAG, "getPalmaresMillesime is empty");
                    }
                } else {
                    Log.w(TAG, "navigateToPalmares  body is empty");
                }
            } else {
                Log.w(TAG, "getPalmares action is empty");
            }
        } catch (NotConnectedException e) {
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
