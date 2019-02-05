package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.model.enums.EnumCompetition;
import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import com.justtennis.plugin.fft.service.FFTServiceFindCompetition;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class FindCompetitionTask extends AsyncTask<Void, String, List<FindCompetitionResponse.CompetitionItem>> {

    private static final String TAG = FindCompetitionTask.class.getName();

    private FFTServiceFindCompetition fftServiceFindCompetition;
    private final EnumCompetition.TYPE type;
    private final String city;
    private final Date dateStart;
    private final Date dateEnd;

    protected FindCompetitionTask(Context context, EnumCompetition.TYPE type, String city, Date dateStart, Date dateEnd) {
        fftServiceFindCompetition = newFFTServiceFindCompetition(context);
        this.type = type;
        this.city = city;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    @Override
    protected List<FindCompetitionResponse.CompetitionItem> doInBackground(Void... params) {
        List<FindCompetitionResponse.CompetitionItem> ret = new ArrayList<>();
        try {
            FindCompetitionFormResponse findCompetitionFormResponse;
            this.publishProgress("Info - Navigate to Find Competition");
            ResponseHttp findCompetition = fftServiceFindCompetition.navigateToFindCompetition(null);
            this.publishProgress("Info - Navigate to Parsing Competition Form");
            findCompetitionFormResponse = fftServiceFindCompetition.getFindForm(findCompetition, type, city, dateStart, dateEnd);

            if (findCompetitionFormResponse.action != null) {
                this.publishProgress("Successfull - Navigate to Find Competition so Submitting Form");
                ResponseHttp submitForm = fftServiceFindCompetition.submitFindForm(null, findCompetitionFormResponse);
                if (submitForm.body != null) {
                    FindCompetitionResponse palmaresMillesimeMatch = fftServiceFindCompetition.getFindCompetition(submitForm);
                    if (palmaresMillesimeMatch != null && !palmaresMillesimeMatch.competitionList.isEmpty()) {
                        ret = palmaresMillesimeMatch.competitionList;
                    } else {
                        Log.w(TAG, "getFindCompetition is empty");
                    }
                } else {
                    this.publishProgress("Failed - Submitting Find Competition Form");
                    Log.w(TAG, "submitFindForm  body is empty");
                }
            } else {
                this.publishProgress("Failed - Parsing Competition Form");
                Log.w(TAG, "getFindForm action is empty");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Find Competition message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FFTServiceFindCompetition newFFTServiceFindCompetition(Context context) {
        return FFTServiceFindCompetition.newInstance(context);
    }
}
