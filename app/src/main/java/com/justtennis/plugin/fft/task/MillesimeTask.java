package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.model.PalmaresResponse;
import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.service.FFTService;

import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class MillesimeTask extends AsyncTask<Void, Void, List<PalmaresMillesimeResponse.Millesime>> {

    private static final String TAG = MillesimeTask.class.getName();

    private Context context;
    private final String mEmail;
    private final String mPassword;

    protected MillesimeTask(Context context, String email, String password) {
        this.context = context;
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected List<PalmaresMillesimeResponse.Millesime> doInBackground(Void... params) {
        try {
            FFTService fftService = newFFTService();
            Log.d(TAG, "getLoginForm login:" + mEmail + " pwd:" + mPassword);
            LoginFormResponse response = fftService.getLoginForm(mEmail, mPassword);
            if (response != null && response.action != null && !response.action.isEmpty()) {
                Log.d(TAG, "getLoginForm OK");
                ResponseHttp form = fftService.submitFormLogin(response);
                if (isFormLoginConnected(form)) {
                    Log.d(TAG, "submitFormLogin OK");

                    ResponseHttp home = fftService.navigateToFormRedirect(form);

                    PalmaresResponse palmaresResponse = fftService.getPalmares(home);

                    if (palmaresResponse != null && palmaresResponse.action != null) {
                        ResponseHttp palmares = fftService.navigateToPalmares(form, palmaresResponse);
                        if (palmares.body != null) {
                            PalmaresMillesimeResponse palmaresMillesimeResponse = fftService.getPalmaresMillesime(palmares);
                            if (palmaresMillesimeResponse != null && !palmaresMillesimeResponse.listMillesime.isEmpty()) {
                                return palmaresMillesimeResponse.listMillesime;
                            } else {
                                Log.w(TAG, "getPalmaresMillesime is empty");
                            }
                        } else {
                            Log.w(TAG, "navigateToPalmares  body is empty");
                        }
                    } else {
                        Log.w(TAG, "getPalmares action is empty");
                    }
                } else {
                    Log.w(TAG, "submitFormLogin return empty");
                }
            } else {
                Log.w(TAG, "getLoginForm return empty");
            }
        } catch (NotConnectedException e) {
            Log.e(TAG, "doInBackground", e);
        }
        return null;
    }

    private boolean isFormLoginConnected(ResponseHttp form) {
        boolean ret = false;
        if (form != null && form.header != null && !form.header.isEmpty()) {
            for(ResponseElement element : form.header) {
                if (element.name.equalsIgnoreCase("Set-Cookie")) {
                    ret = true;
                    break;
                }
            }
        }
        Log.d(TAG, "isFormLoginConnected:" + ret);
        return ret;
    }

    protected FFTService newFFTService() {
        return FFTService.newInstance(context);
    }
}
