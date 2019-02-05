package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.cameleon.android.shared.exception.NotConnectedException;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.fft.service.FFTServiceRanking;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class RankingMatchTask extends AsyncTask<Void, String, List<RankingMatchResponse.RankingItem>> {

    private static final String TAG = RankingMatchTask.class.getName();

    private FFTServiceRanking fftServiceRanking;

    protected RankingMatchTask(Context context) {
        fftServiceRanking = newFFTService(context);
    }

    @Override
    protected List<RankingMatchResponse.RankingItem> doInBackground(Void... params) {
        List<RankingMatchResponse.RankingItem> ret = new ArrayList<>();
        try {
            Log.d(TAG, "submitFormLogin OK");
            this.publishProgress("Info - Navigate to Ranking and Getting Ranking");
            RankingListResponse rankingList = fftServiceRanking.getRankingList(null);
            if (rankingList != null && !rankingList.rankingList.isEmpty()) {
                Log.d(TAG, "getRankingList OK");
                RankingListResponse.RankingItem rank = rankingList.rankingList.get(0);

                RankingMatchResponse ranking = fftServiceRanking.getRankingMatch(null, rank.id);
                ret = ranking.rankingList;
            } else {
                this.publishProgress("Failed - Navigate to Ranking and Getting Ranking");
                Log.w(TAG, "getRankingList return empty");
            }
        } catch (NotConnectedException e) {
            this.publishProgress("Error - Ranking Match message:" + e.getMessage());
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FFTServiceRanking newFFTService(Context context) {
        return FFTServiceRanking.newInstance(context);
    }
}
