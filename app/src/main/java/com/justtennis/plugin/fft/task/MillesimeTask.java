package com.justtennis.plugin.fft.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.model.PalmaresResponse;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.service.FFTService;

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
            FFTService fftService = newFFTService();
            ResponseHttp home = fftService.navigateToHomePage(null);

            PalmaresResponse palmaresResponse = fftService.getPalmares(home);

            if (palmaresResponse != null && palmaresResponse.action != null) {
                ResponseHttp palmares = fftService.navigateToPalmares(null, palmaresResponse);
                if (palmares.body != null) {
                    PalmaresMillesimeResponse palmaresMillesimeResponse = fftService.getPalmaresMillesime(palmares);
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

    private FFTService newFFTService() {
        return FFTService.newInstance(context);
    }
}
