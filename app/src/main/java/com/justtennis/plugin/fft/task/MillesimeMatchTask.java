package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.model.MillesimeMatchResponse;
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
public abstract class MillesimeMatchTask extends AsyncTask<Void, String, List<MillesimeMatchResponse.MatchItem>> {

    private static final String TAG = MillesimeMatchTask.class.getName();

    private FFTService fftService;
    private String millesime;

    protected MillesimeMatchTask(Context context, String millesime) {
        fftService = newFFTService(context);
        this.millesime = millesime;
    }

    @Override
    protected List<MillesimeMatchResponse.MatchItem> doInBackground(Void... params) {
        List<MillesimeMatchResponse.MatchItem> ret = new ArrayList<>();
        try {
            ResponseHttp home = fftService.navigateToHomePage(null);
            PalmaresResponse palmaresResponse = fftService.getPalmares(home);

            if (palmaresResponse != null && palmaresResponse.action != null) {
                ResponseHttp palmares = fftService.navigateToPalmares(null, palmaresResponse);
                if (palmares.body != null) {
                    PalmaresMillesimeResponse palmaresMillesimeResponse = fftService.getPalmaresMillesime(palmares);
                    if (!palmaresMillesimeResponse.listMillesime.isEmpty()) {

                        findMillesime(palmaresMillesimeResponse);

                        if (palmaresMillesimeResponse.millesimeSelected != null) {
                            ResponseHttp submitForm = fftService.submitFormPalmaresMillesime(null, palmaresMillesimeResponse);
                            if (submitForm.body != null && !submitForm.body.isEmpty()) {
                                MillesimeMatchResponse palmaresMillesimeMatch = fftService.getPalmaresMillesimeMatch(submitForm);
                                if (palmaresMillesimeResponse != null && !palmaresMillesimeResponse.listMillesime.isEmpty()) {
                                    ret = palmaresMillesimeMatch.matchList;
                                } else {
                                    Log.w(TAG, "getPalmaresMillesimeMatch is empty");
                                }
                            } else {
                                Log.w(TAG, "submitFormPalmaresMillesime error");
                            }
                        } else {
                            Log.w(TAG, "millesime '"+millesime+"' not found");
                        }
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

    private void findMillesime(PalmaresMillesimeResponse palmaresMillesimeResponse) {
        palmaresMillesimeResponse.millesimeSelected = null;
        for(PalmaresMillesimeResponse.Millesime mill : palmaresMillesimeResponse.listMillesime) {
            if (mill.value.equals(millesime)) {
                palmaresMillesimeResponse.millesimeSelected = mill;
                break;
            }
        }
    }

    private FFTService newFFTService(Context context) {
        return FFTService.newInstance(context);
    }
}
