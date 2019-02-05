package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.common.FFTConfiguration;
import com.justtennis.plugin.fft.converter.FindCompetitionFormResponseConverter;
import com.justtennis.plugin.fft.model.enums.EnumCompetition;
import com.justtennis.plugin.fft.parser.FindCompetitionParser;
import com.justtennis.plugin.fft.parser.FormCompetitionParser;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionAjaxRequest;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionFormRequest;
import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;

import org.jsoup.helper.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FFTServiceFindCompetition extends AbstractFFTService {

    private static final String TAG = FFTServiceFindCompetition.class.getName();

    private FFTServiceFindCompetition(Context context) {
        super(context);
    }

    public static FFTServiceFindCompetition newInstance(Context context) {
        return new FFTServiceFindCompetition(context);
    }

    public FindCompetitionFormResponse getFindForm(ResponseHttp findPlayerResponseHttp, EnumCompetition.TYPE type, String city) {
        return getFindForm(findPlayerResponseHttp, type, city, null, null);
    }

    public FindCompetitionFormResponse getFindForm(ResponseHttp findPlayerResponseHttp, EnumCompetition.TYPE type, String city, Date dateStart, Date dateEnd) {
        logMethod("getFindForm");
        FindCompetitionFormResponse ret = null;

        Calendar cal = Calendar.getInstance();
        if (dateStart == null) {
            dateStart = cal.getTime();
        }
        if (dateEnd == null) {
            cal.add(Calendar.MONTH, 3);
            dateEnd = cal.getTime();
        }

//        System.out.println("==============> connection Return:\r\n" + findPlayerResponseHttp.body);

        if (!StringUtil.isBlank(findPlayerResponseHttp.body)) {
            ret = FormCompetitionParser.getInstance().parseForm(findPlayerResponseHttp.body, new FFTFindCompetitionFormRequest());
            ret.type.value = type.value;
            ret.city.value = city;
            ret.dateStart.value = FFTConfiguration.sdfFFT.format(dateStart);
            ret.dateEnd.value = FFTConfiguration.sdfFFT.format(dateEnd);
            System.out.println("==============> new form element name:" + ret.type.name + " value:" + ret.type.value);
            System.out.println("==============> new form element name:" + ret.city.name + " value:" + ret.city.value);
            System.out.println("==============> new form element name:" + ret.dateStart.name + " value:" + ret.dateStart.value);
            System.out.println("==============> new form element name:" + ret.dateEnd.name + " value:" + ret.dateEnd.value);
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
//        System.out.println("==============> body:" + findCompetitionResponseHttp.body);
        return FindCompetitionParser.parseFindCompetition(findCompetitionResponseHttp.body, new FFTFindCompetitionAjaxRequest());
    }

    public ResponseHttp navigateToFindCompetition(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFindCompetition");
        return doGetConnected(URL_ROOT, "/recherche/tournois/all", loginFormResponse);
    }
}
