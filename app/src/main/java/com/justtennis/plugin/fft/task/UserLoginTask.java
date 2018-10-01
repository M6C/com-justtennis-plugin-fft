package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.preference.FFTSharedPref;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.service.FFTService;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = UserLoginTask.class.getName();

    private final String mEmail;
    private final String mPassword;
    private FFTService fftService;

    protected UserLoginTask(Context context, String email, String password) {
        mEmail = email;
        mPassword = password;
        fftService = newFFTService(context);
        FFTSharedPref.setLogin(context, mEmail);
        FFTSharedPref.setPwd(context, mPassword);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean ret = Boolean.FALSE;
        Log.d(TAG, "getLoginForm login:" + mEmail + " pwd:" + mPassword);
        LoginFormResponse response = fftService.getLoginForm(mEmail, mPassword);
        if (response != null && response.action != null && !response.action.isEmpty()) {
            Log.d(TAG, "getLoginForm OK");
            ResponseHttp form = fftService.submitFormLogin(response);
            ret = isFormLoginConnected(form);
        } else {
            Log.w(TAG, "getLoginForm return empty");
        }
        return ret;
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

    protected FFTService newFFTService(Context context) {
        return FFTService.newInstance(context);
    }
}
