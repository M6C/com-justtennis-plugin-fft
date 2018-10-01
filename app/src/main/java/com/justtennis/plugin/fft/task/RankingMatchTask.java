package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.fft.service.FFTService;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class RankingMatchTask extends AsyncTask<Void, Void, List<RankingMatchResponse.RankingItem>> {

    private static final String TAG = RankingMatchTask.class.getName();

    private FFTService fftService;

    protected RankingMatchTask(Context context) {
        fftService = newFFTService(context);
    }

    @Override
    protected List<RankingMatchResponse.RankingItem> doInBackground(Void... params) {
        List<RankingMatchResponse.RankingItem> ret = new ArrayList<>();
        try {
            Log.d(TAG, "submitFormLogin OK");
            RankingListResponse rankingList = fftService.getRankingList(null);
            if (rankingList != null && !rankingList.rankingList.isEmpty()) {
                Log.d(TAG, "getRankingList OK");
                RankingListResponse.RankingItem rank = rankingList.rankingList.get(0);

                RankingMatchResponse ranking = fftService.getRankingMatch(null, rank.id);
                ret = ranking.rankingList;
            } else {
                Log.w(TAG, "getRankingList return empty");
            }
        } catch (NotConnectedException e) {
            Log.e(TAG, "doInBackground", e);
        }
        return ret;
    }

    private FFTService newFFTService(Context context) {
        return FFTService.newInstance(context);
    }
}
