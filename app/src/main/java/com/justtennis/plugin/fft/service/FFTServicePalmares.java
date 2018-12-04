package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.converter.PalmaresMillesimeFormResponseConverter;
import com.justtennis.plugin.fft.parser.MillesimeMatchParser;
import com.justtennis.plugin.fft.parser.PalmaresParser;
import com.justtennis.plugin.fft.query.request.FFTMillessimeMatchRequest;
import com.justtennis.plugin.fft.query.request.PalmaresMillesimeRequest;
import com.justtennis.plugin.fft.query.request.PalmaresRequest;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.query.response.PalmaresResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FFTServicePalmares extends AbstractFFTService {

    private static final String TAG = FFTServicePalmares.class.getName();

    private FFTServicePalmares(Context context) {
        super(context);
    }

    public static FFTServicePalmares newInstance(Context context) {
        return new FFTServicePalmares(context);
    }

    public PalmaresResponse getPalmares(ResponseHttp loginFormResponse) {
        logMethod("getPalmares");
        System.out.println("==============> body:" + loginFormResponse.body);
        return PalmaresParser.parsePalmares(loginFormResponse.body, new PalmaresRequest());
    }

    public ResponseHttp navigateToPalmares(ResponseHttp loginFormResponse, PalmaresResponse palmaresResponse) throws NotConnectedException {
        logMethod("navigateToPalmares");
        return doGetConnected(URL_ROOT, palmaresResponse.getUrlAction(), loginFormResponse);
    }

    public PalmaresMillesimeResponse getPalmaresMillesime(ResponseHttp palamresResponseHttp) {
        logMethod("getPalmaresMillesime");
        System.out.println("==============> body:" + palamresResponseHttp.body);
        return PalmaresParser.parsePalmaresMillesime(palamresResponseHttp.body, new PalmaresMillesimeRequest());
    }

    public ResponseHttp submitFormPalmaresMillesime(ResponseHttp loginFormResponse, PalmaresMillesimeResponse form) throws NotConnectedException {
        logMethod("submitFormPalmaresMillesime");
        ResponseHttp ret = null;

        System.out.println(
            "\n\n\n==============> FFT Form Palmares Millesime Action:" + form.action +
            "\r\n==============> FFT Form Method:" + form.method);

        form.select.value = form.millesimeSelected.value;

        Map<String, String> data = PalmaresMillesimeFormResponseConverter.toDataMap(form);
        data.put("mobile_sort", "nomAdversaire");
        if (!StringUtil.isBlank(form.action)) {
            if ("get".equalsIgnoreCase(form.method)) {
                ret = doGetConnected(URL_ROOT, form.action, data, loginFormResponse);
            } else {
                ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
            }
        }

        return ret;
    }

    public MillesimeMatchResponse getPalmaresMillesimeMatch(ResponseHttp palamresMillesimeResponseHttp) {
        logMethod("getPalmaresMillesimeMatch");
        System.out.println("==============> body:" + palamresMillesimeResponseHttp.body);
        return MillesimeMatchParser.parseMillesimeMatch(palamresMillesimeResponseHttp.body, new FFTMillessimeMatchRequest());
    }
}
