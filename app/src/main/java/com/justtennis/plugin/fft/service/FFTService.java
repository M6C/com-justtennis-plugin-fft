package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.converter.LoginFormResponseConverter;
import com.justtennis.plugin.fft.model.FFTLoginFormRequest;
import com.justtennis.plugin.fft.model.FFTRankingListRequest;
import com.justtennis.plugin.fft.model.FFTRankingMatchRequest;
import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.RankingListResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.parser.FormParser;
import com.justtennis.plugin.fft.parser.RankingParser;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.Map;

public class FFTService {

    private static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";

    private FFTService() {
    }

    public static LoginFormResponse getLoginForm(String login, String password) {
        System.out.println("\r\n" + URL_ROOT);

        ResponseHttp respRoot = HttpGetProxy.get(URL_ROOT, "");
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        LoginFormResponse form = FormParser.parseFormLogin(respRoot.body, new FFTLoginFormRequest());
        form.login.value = login;
        form.password.value = password;

        return form;
    }

    public static ResponseHttp submitFormLogin(LoginFormResponse form) throws IOException {
        System.out.println("");
        System.out.println("==============> Form Action:" + form.action);

        Map<String, String> data = LoginFormResponseConverter.toDataMap(form);

        return HttpPostProxy.post(URL_ROOT, form.action, data);
    }

    public static ResponseHttp navigateToFormRedirect(ResponseHttp loginFormResponse) {
        if (loginFormResponse.pathRedirect != null && !loginFormResponse.pathRedirect.isEmpty()) {
            return HttpGetProxy.get(URL_ROOT, loginFormResponse.pathRedirect, loginFormResponse);
        }
        return null;
    }

    public static ResponseHttp navigateToRanking(ResponseHttp loginFormResponse) {
        return HttpGetProxy.get(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
    }

    public static RankingListResponse getRankingList(ResponseHttp loginFormResponse) {
        ResponseHttp respRoot = HttpGetProxy.get(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        return RankingParser.parseRankingList(respRoot.body, new FFTRankingListRequest());
    }

    public static RankingMatchResponse getRankingMatch(ResponseHttp loginFormResponse, String id) {
        ResponseHttp respRoot = HttpGetProxy.get(URL_ROOT, "/page_classement_ajax?id_bilan=" + id, loginFormResponse);
        if (!StringUtil.isBlank(respRoot.body)) {
            respRoot.body = format(respRoot.body);
            System.out.println("==============> getRankingMatch formated ranking.body:" + respRoot.body);
        }
        return RankingParser.parseRankingMatch(respRoot.body, new FFTRankingMatchRequest());
    }

    private static String format(String str) {
        String ret = decode(str);
        ret = ret.replaceAll("\\\\n", "");
        ret = ret.replaceAll("\\\\/", "/");
        return ret;
    }

    private static String decode(final String in) {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1) {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

}
