package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.model.enums.EnumPlayer;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;
import com.justtennis.plugin.fft.service.FFTServiceFindPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class FindPlayerTask extends AsyncTask<Void, String, List<FindPlayerResponse.PlayerItem>> {

    private static final String TAG = FindPlayerTask.class.getName();

    private FFTServiceFindPlayer fftServiceFindPlayer;
    private final EnumPlayer.GENRE genre;
    private final String firstname;
    private final String lastname;

    protected FindPlayerTask(Context context, EnumPlayer.GENRE genre, String firstname, String lastname) {
        fftServiceFindPlayer = newFFTServiceFindPlayer(context);
        this.genre = genre;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    protected List<FindPlayerResponse.PlayerItem> doInBackground(Void... params) {
        List<FindPlayerResponse.PlayerItem> ret = new ArrayList<>();
        try {
            FindPlayerFormResponse findPlayerFormResponse;
            this.publishProgress("Info - Navigate to Find Player");
            ResponseHttp findPlayer = fftServiceFindPlayer.navigateToFindPlayer(null);
            this.publishProgress("Info - Navigate to Parsing Player Form");
            findPlayerFormResponse = fftServiceFindPlayer.getFindForm(findPlayer, genre,firstname, lastname);

            if (findPlayerFormResponse.action != null) {
                this.publishProgress("Successfull - Navigate to Find Player so Submitting Form");
                ResponseHttp submitForm = fftServiceFindPlayer.submitFormFindPlayer(null, findPlayerFormResponse);
                if (submitForm.body != null) {
                    FindPlayerResponse palmaresMillesimeMatch = fftServiceFindPlayer.getFindPlayer(submitForm);
                    if (palmaresMillesimeMatch != null && !palmaresMillesimeMatch.playerList.isEmpty()) {
                        ret = palmaresMillesimeMatch.playerList;
                    } else {
                        Log.w(TAG, "getPalmaresMillesime is empty");
                    }
                } else {
                    this.publishProgress("Failed - Submitting Find Player Form");
                    Log.w(TAG, "navigateToPalmares  body is empty");
                }
            } else {
                this.publishProgress("Failed - Parsing Player Form");
                Log.w(TAG, "getPalmares action is empty");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Find Player message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FFTServiceFindPlayer newFFTServiceFindPlayer(Context context) {
        return FFTServiceFindPlayer.newInstance(context);
    }
}
