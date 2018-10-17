package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.converter.FindPlayerFormResponseConverter;
import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.parser.FindPlayerParser;
import com.justtennis.plugin.fft.parser.FormParser;
import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.request.FFTFindPlayerRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FFTServiceFindPlayer extends AbstractFFTService {

    private static final String TAG = FFTServiceFindPlayer.class.getName();

    private FFTServiceFindPlayer(Context context) {
        super(context);
        initializeProxy(this);
    }

    public static FFTServiceFindPlayer newInstance(Context context) {
        return new FFTServiceFindPlayer(context);
    }

    public FindPlayerFormResponse getFindPlayerForm(ResponseHttp findPlayerResponseHttp, PLAYER_GENRE genre, String fistname, String lastname) {
        logMethod("getFindPlayerForm");
        FindPlayerFormResponse ret = null;

        System.out.println("==============> connection Return:\r\n" + findPlayerResponseHttp.body);

        if (!StringUtil.isBlank(findPlayerResponseHttp.body)) {
            ret = FormParser.parseFormFindPlayer(findPlayerResponseHttp.body, new FFTFindPlayerFormRequest());
            ret.genre.value = genre.value;
            ret.firstname.value = fistname;
            ret.lastname.value = lastname;
        }

        return ret;
    }

    public ResponseHttp submitFormFindPlayer(ResponseHttp loginFormResponse, FindPlayerFormResponse form) throws NotConnectedException {
        logMethod("submitFormFindPlayer");
        ResponseHttp ret = null;

        System.out.println("\n\n\n==============> Form Action:" + form.action);

        Map<String, String> data = FindPlayerFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }

    public FindPlayerResponse getFindPlayer(ResponseHttp findPlayerResponseHttp) {
        logMethod("getFindPlayer");
        System.out.println("==============> body:" + findPlayerResponseHttp.body);
        return FindPlayerParser.parseFindPlayer(findPlayerResponseHttp.body, new FFTFindPlayerRequest());
    }

    public ResponseHttp navigateToFindPlayer(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFindPlayer");
        return doGetConnected(URL_ROOT, "/recherche-joueur", loginFormResponse);
    }
}
