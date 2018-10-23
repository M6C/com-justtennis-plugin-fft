package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
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
public abstract class MillesimeMatchTask extends AsyncTask<Void, String, List<MillesimeMatchResponse.MatchItem>> {

    private static final String TAG = MillesimeMatchTask.class.getName();

    private FFTServiceLogin fftServiceLogin;
    private FFTServicePalmares fftServicePalmares;
    private String palmaresAction;
    private String millesime;

    protected MillesimeMatchTask(Context context, String palmaresAction, String millesime) {
        fftServiceLogin = newFFTService(context);
        fftServicePalmares = newFFTServicePalmares(context);
        this.palmaresAction = palmaresAction;
        this.millesime = millesime;
    }

    @Override
    protected List<MillesimeMatchResponse.MatchItem> doInBackground(Void... params) {
        List<MillesimeMatchResponse.MatchItem> ret = new ArrayList<>();
        try {
            PalmaresResponse palmaresResponse;
            if (palmaresAction != null) {
                palmaresResponse = new PalmaresResponse();
                palmaresResponse.action = palmaresAction;
            } else {
                ResponseHttp home = fftServiceLogin.navigateToHomePage(null);
                palmaresResponse = fftServicePalmares.getPalmares(home);
            }
            palmaresResponse.millesime = millesime;

            if (palmaresResponse.action != null) {
                ResponseHttp palmares = fftServicePalmares.navigateToPalmares(null, palmaresResponse);
                if (palmares.body != null) {
                    PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
                    if (!palmaresMillesimeResponse.listMillesime.isEmpty()) {

                        findMillesime(palmaresMillesimeResponse);

                        if (palmaresMillesimeResponse.millesimeSelected != null) {
                            ResponseHttp submitForm = fftServicePalmares.submitFormPalmaresMillesime(null, palmaresMillesimeResponse);
                            if (submitForm.body != null && !submitForm.body.isEmpty()) {
                                MillesimeMatchResponse palmaresMillesimeMatch = fftServicePalmares.getPalmaresMillesimeMatch(submitForm);
                                if (!palmaresMillesimeResponse.listMillesime.isEmpty()) {
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

    private FFTServiceLogin newFFTService(Context context) {
        return FFTServiceLogin.newInstance(context);
    }

    private FFTServicePalmares newFFTServicePalmares(Context context) {
        return FFTServicePalmares.newInstance(context);
    }
}
