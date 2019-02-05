package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.converter.FindPlayerFormResponseConverter;
import com.justtennis.plugin.fft.model.enums.EnumPlayer;
import com.justtennis.plugin.fft.parser.FindPlayerParser;
import com.justtennis.plugin.fft.parser.FormPlayerParser;
import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.request.FFTFindPlayerRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FFTServiceFindPlayer extends AbstractFFTService {

    private static final String TAG = FFTServiceFindPlayer.class.getName();

    private FFTServiceFindPlayer(Context context) {
        super(context);
    }

    public static FFTServiceFindPlayer newInstance(Context context) {
        return new FFTServiceFindPlayer(context);
    }

    public FindPlayerFormResponse getFindForm(ResponseHttp findPlayerResponseHttp, EnumPlayer.GENRE genre, String fistname, String lastname) {
        logMethod("getFindForm");
        FindPlayerFormResponse ret = null;

//        System.out.println("==============> connection Return:\r\n" + findPlayerResponseHttp.body);

        if (!StringUtil.isBlank(findPlayerResponseHttp.body)) {
            ret = FormPlayerParser.getInstance().parseForm(findPlayerResponseHttp.body, new FFTFindPlayerFormRequest());
            ret.genre.value = genre.value;
            ret.firstname.value = fistname;
            ret.lastname.value = lastname;
        }

        return ret;
    }

    public ResponseHttp submitFormFindPlayer(ResponseHttp loginFormResponse, FindPlayerFormResponse form) throws NotConnectedException {
        logMethod("submitFindForm");
        ResponseHttp ret = null;

        System.out.println("\n\n\n==============> FFT Form Find Player Action:" + form.action);

        Map<String, String> data = FindPlayerFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }

    public FindPlayerResponse getFindPlayer(ResponseHttp findPlayerResponseHttp) {
        logMethod("getFindCompetition");
        System.out.println("==============> body:" + findPlayerResponseHttp.body);
        return FindPlayerParser.parseFindPlayer(findPlayerResponseHttp.body, new FFTFindPlayerRequest());
    }

    public ResponseHttp navigateToFindPlayer(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFindCompetition");
        return doGetConnected(URL_ROOT, "/recherche-joueur", loginFormResponse);
    }
}
