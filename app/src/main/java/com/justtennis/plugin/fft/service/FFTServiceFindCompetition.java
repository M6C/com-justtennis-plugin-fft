package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.converter.FindCompetitionFormResponseConverter;
import com.justtennis.plugin.fft.model.enums.EnumCompetition;
import com.justtennis.plugin.fft.parser.FindCompetitionParser;
import com.justtennis.plugin.fft.parser.FormCompetitionParser;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionAjaxRequest;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionFormRequest;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionRequest;
import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class FFTServiceFindCompetition extends AbstractFFTService {

    private static final String TAG = FFTServiceFindCompetition.class.getName();
    private static SimpleDateFormat sdfFFT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private FFTServiceFindCompetition(Context context) {
        super(context);
    }

    public static FFTServiceFindCompetition newInstance(Context context) {
        return new FFTServiceFindCompetition(context);
    }

    public FindCompetitionFormResponse getFindForm(ResponseHttp findPlayerResponseHttp, EnumCompetition.TYPE type, String city) {
        Calendar cal = Calendar.getInstance();
        Date dateStart = cal.getTime();
        cal.add(Calendar.MONTH, 3);
        Date dateEnd = cal.getTime();
        return getFindForm(findPlayerResponseHttp, type, city, dateStart, dateEnd);
    }

    public FindCompetitionFormResponse getFindForm(ResponseHttp findPlayerResponseHttp, EnumCompetition.TYPE type, String city, Date dateStart, Date dateEnd) {
        logMethod("getFindForm");
        FindCompetitionFormResponse ret = null;

//        System.out.println("==============> connection Return:\r\n" + findPlayerResponseHttp.body);

        if (!StringUtil.isBlank(findPlayerResponseHttp.body)) {
            ret = FormCompetitionParser.getInstance().parseForm(findPlayerResponseHttp.body, new FFTFindCompetitionFormRequest());
            ret.type.value = type.value;
            ret.city.value = city;
            ret.dateStart.value = sdfFFT.format(dateStart);
            ret.dateEnd.value = sdfFFT.format(dateEnd);
        }

        return ret;
    }

    public ResponseHttp submitFindForm(ResponseHttp loginFormResponse, FindCompetitionFormResponse form) throws NotConnectedException {
        logMethod("submitFindForm");
        ResponseHttp ret = null;

        System.out.println("\n\n\n==============> FFT Form Find Competition Action:" + form.action);

        Map<String, String> data = FindCompetitionFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = doPostConnected(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }

    public FindCompetitionResponse getFindCompetition(ResponseHttp findCompetitionResponseHttp) {
        logMethod("getFindCompetition");
        System.out.println("==============> body:" + findCompetitionResponseHttp.body);
        return FindCompetitionParser.parseFindCompetition(findCompetitionResponseHttp.body, new FFTFindCompetitionAjaxRequest());
    }

    public ResponseHttp navigateToFindCompetition(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFindCompetition");
        return doGetConnected(URL_ROOT, "/recherche/tournois/all", loginFormResponse);
    }
}
