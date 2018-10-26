package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.shared.network.model.ResponseElement;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.preference.LoginSharedPref;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.IServiceLogin;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = UserLoginTask.class.getName();

    private final String mEmail;
    private final String mPassword;
    private IServiceLogin fftServiceLogin;

    protected abstract IServiceLogin newLoginService(Context context);

    protected UserLoginTask(Context context, String email, String password) {
        mEmail = email;
        mPassword = password;
        fftServiceLogin = newLoginService(context);
        saveData(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean ret = Boolean.FALSE;
        Log.d(TAG, "getLoginForm login:" + mEmail + " pwd:" + mPassword);
        LoginFormResponse response = fftServiceLogin.getLoginForm(mEmail, mPassword);
        if (response != null && response.action != null && !response.action.isEmpty()) {
            Log.d(TAG, "getLoginForm OK");
            ResponseHttp form = fftServiceLogin.submitFormLogin(response);
            ret = isFormLoginConnected(form);
        } else {
            Log.w(TAG, "getLoginForm return empty");
        }
        return ret;
    }

    protected void saveData(Context context) {
        LoginSharedPref.setLogin(context, mEmail);
        LoginSharedPref.setPwd(context, mPassword);
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
}
