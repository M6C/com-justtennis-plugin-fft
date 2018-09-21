package com.justtennis.plugin.fft.task;

import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.RankingListResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.service.FFTService;

import java.io.IOException;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class UserLoginTask extends AsyncTask<Void, Void, List<RankingMatchResponse.RankingItem>> {

    private static final String TAG = UserLoginTask.class.getName();

    private final String mEmail;
    private final String mPassword;

    protected UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected List<RankingMatchResponse.RankingItem> doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

//        try {
//            // Simulate network access.
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            return false;
//        }
//
//        for (String credential : DUMMY_CREDENTIALS) {
//            String[] pieces = credential.split(":");
//            if (pieces[0].equals(mEmail)) {
//                // Account exists, return true if the password matches.
//                return pieces[1].equals(mPassword);
//            }
//        }
//
//        // TODO: register the new account here.
//        return true;

        try {
            FFTService fftService = newFFTService();
            Log.d(TAG, "getLoginForm login:" + mEmail + " pwd:" + mPassword);
            LoginFormResponse response = fftService.getLoginForm(mEmail, mPassword);
            if (response != null && response.action != null && !response.action.isEmpty()) {
                Log.d(TAG, "getLoginForm OK");
                ResponseHttp form = fftService.submitFormLogin(response);
                if (isFormLoginConnected(form)) {
                    Log.d(TAG, "submitFormLogin OK");
                    RankingListResponse rankingList = fftService.getRankingList(form);
                    if (rankingList != null && !rankingList.rankingList.isEmpty()) {
                        Log.d(TAG, "getRankingList OK");
                        RankingListResponse.RankingItem rank = rankingList.rankingList.get(0);

                        RankingMatchResponse ranking = fftService.getRankingMatch(form, rank.id);
                        return ranking.rankingList;
                    } else {
                        Log.w(TAG, "getRankingList return empty");
                    }
                } else {
                    Log.w(TAG, "submitFormLogin return empty");
                }
            } else {
                Log.w(TAG, "getLoginForm return empty");
            }
        } catch (IOException e) {
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
        return FFTService.newInstance();
    }
}
