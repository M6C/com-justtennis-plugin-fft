package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.parser.RankingParser;
import com.justtennis.plugin.fft.query.request.FFTRankingListRequest;
import com.justtennis.plugin.fft.query.request.FFTRankingMatchRequest;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;

import org.jsoup.helper.StringUtil;

public class FFTServiceRanking extends AbstracFFTService {

    private static final String TAG = FFTServiceRanking.class.getName();

    private FFTServiceRanking(Context context) {
        super(context);
        initializeProxy(this);
    }

    public static FFTServiceRanking newInstance(Context context) {
        return new FFTServiceRanking(context);
    }

    public ResponseHttp navigateToRanking(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToRanking");
        return doGetConnected(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
    }

    public RankingListResponse getRankingList(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("getRankingList");
        ResponseHttp respRoot = doGetConnected(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        return RankingParser.parseRankingList(respRoot.body, new FFTRankingListRequest());
    }

    public RankingMatchResponse getRankingMatch(ResponseHttp loginFormResponse, String id) throws NotConnectedException {
        logMethod("getRankingMatch");
        ResponseHttp respRoot = doGetConnected(URL_ROOT, "/page_classement_ajax?id_bilan=" + id, loginFormResponse);
        if (!StringUtil.isBlank(respRoot.body)) {
            respRoot.body = format(respRoot.body);
            System.out.println("==============> getRankingMatch formated ranking.body:" + respRoot.body);
        }
        return RankingParser.parseRankingMatch(respRoot.body, new FFTRankingMatchRequest());
    }
}
