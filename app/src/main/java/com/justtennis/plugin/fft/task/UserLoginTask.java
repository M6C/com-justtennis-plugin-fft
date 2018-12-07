package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.manager.ServiceManager;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.preference.LoginSharedPref;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.IServiceLogin;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class UserLoginTask extends AsyncTask<Void, String, Boolean> {

    private static final String TAG = UserLoginTask.class.getName();

    private final String mEmail;
    private final String mPassword;
    private IServiceLogin loginService;

    protected abstract IServiceLogin newLoginService(Context context);

    UserLoginTask(Context context, String email, String password) {
        mEmail = email;
        mPassword = password;
        loginService = newLoginService(context);
        saveData(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean ret = Boolean.FALSE;
        Log.d(TAG, "getLoginForm login:" + mEmail + " pwd:" + mPassword);
        this.publishProgress("Info - Navigate to Login");
        LoginFormResponse response = loginService.getLoginForm(mEmail, mPassword);
        if (response != null && response.action != null && !response.action.isEmpty()) {
            this.publishProgress("Successfull - Navigate to Login so Submitting Form");
            Log.d(TAG, "getLoginForm OK");
            ResponseHttp form = loginService.submitFormLogin(response);
            ret = isFormLoginConnected(form);
        } else {
            this.publishProgress("Failed - Navigate to Login");
            Log.w(TAG, "getLoginForm return empty");
        }
        return ret;
    }

    protected void saveData(Context context) {
        if (ServiceManager.getInstance().doSaveLogin()) {
            LoginSharedPref.setLogin(context, mEmail);
            LoginSharedPref.setPwd(context, mPassword);
        }
    }

    private boolean isFormLoginConnected(ResponseHttp form) {
        boolean ret = form != null && !form.headerCookie.isEmpty();
        Log.d(TAG, "isFormLoginConnected:" + ret);
        return ret;
    }
}
