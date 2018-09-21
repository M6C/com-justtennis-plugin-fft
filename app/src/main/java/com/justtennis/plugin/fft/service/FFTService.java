package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.converter.LoginFormResponseConverter;
import com.justtennis.plugin.converter.PalmaresMillesimeFormResponseConverter;
import com.justtennis.plugin.fft.model.FFTLoginFormRequest;
import com.justtennis.plugin.fft.model.FFTRankingListRequest;
import com.justtennis.plugin.fft.model.FFTRankingMatchRequest;
import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.PalmaresMillesimeRequest;
import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.model.PalmaresRequest;
import com.justtennis.plugin.fft.model.PalmaresResponse;
import com.justtennis.plugin.fft.model.RankingListResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.parser.FormParser;
import com.justtennis.plugin.fft.parser.PalmaresParser;
import com.justtennis.plugin.fft.parser.RankingParser;
import com.justtennis.plugin.fft.skeleton.IProxy;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.Map;

public class FFTService implements IProxy {

    private static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;
    private static final String LOGON_METHOD = "https";

    private String proxyHost;
    private int    proxyPort;
    private String proxyUser;
    private String proxyPw;

    private FFTService() {
    }

    public static FFTService newInstance() {
        return new FFTService();
    }

    public LoginFormResponse getLoginForm(String login, String password) {
        logMethod("getLoginForm");
        LoginFormResponse ret = null;
        System.out.println("\r\n" + URL_ROOT);

        ResponseHttp respRoot = newHttpGetProxy().get(URL_ROOT, "");
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        if (!StringUtil.isBlank(respRoot.body)) {
            ret = FormParser.parseFormLogin(respRoot.body, new FFTLoginFormRequest());
            ret.login.value = login;
            ret.password.value = password;
        }

        return ret;
    }

    public ResponseHttp submitFormLogin(LoginFormResponse form) throws IOException {
        logMethod("submitFormLogin");
        ResponseHttp ret = null;

        System.out.println("");
        System.out.println("==============> Form Action:" + form.action);

        Map<String, String> data = LoginFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = newHttpPostProxy().post(URL_ROOT, form.action, data);
        }

        return ret;
    }

    public ResponseHttp navigateToFormRedirect(ResponseHttp loginFormResponse) {
        logMethod("navigateToFormRedirect");
        if (loginFormResponse.pathRedirect != null && !loginFormResponse.pathRedirect.isEmpty()) {
            return newHttpGetProxy().get(URL_ROOT, loginFormResponse.pathRedirect, loginFormResponse);
        }
        return null;
    }

    public ResponseHttp navigateToRanking(ResponseHttp loginFormResponse) {
        logMethod("navigateToRanking");
        return newHttpGetProxy().get(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
    }

    public RankingListResponse getRankingList(ResponseHttp loginFormResponse) {
        logMethod("getRankingList");
        ResponseHttp respRoot = newHttpGetProxy().get(URL_ROOT, "/bloc_home/redirect/classement", loginFormResponse);
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        return RankingParser.parseRankingList(respRoot.body, new FFTRankingListRequest());
    }

    public RankingMatchResponse getRankingMatch(ResponseHttp loginFormResponse, String id) {
        logMethod("getRankingMatch");
        ResponseHttp respRoot = newHttpGetProxy().get(URL_ROOT, "/page_classement_ajax?id_bilan=" + id, loginFormResponse);
        if (!StringUtil.isBlank(respRoot.body)) {
            respRoot.body = format(respRoot.body);
            System.out.println("==============> getRankingMatch formated ranking.body:" + respRoot.body);
        }
        return RankingParser.parseRankingMatch(respRoot.body, new FFTRankingMatchRequest());
    }

    public PalmaresResponse getPalmares(ResponseHttp loginFormResponse) {
        logMethod("getPalmares");
        System.out.println("==============> body:" + loginFormResponse.body);
        return PalmaresParser.parsePalmares(loginFormResponse.body, new PalmaresRequest());
    }

    public ResponseHttp navigateToPalmares(ResponseHttp loginFormResponse, PalmaresResponse palmaresResponse) {
        logMethod("navigateToPalmares");
        return newHttpGetProxy().get(URL_ROOT, palmaresResponse.action, loginFormResponse);
    }

    public PalmaresMillesimeResponse getPalmaresMillesime(ResponseHttp palamresResponseHttp) {
        logMethod("getPalmaresMillesime");
        System.out.println("==============> body:" + palamresResponseHttp.body);
        return PalmaresParser.parsePalmaresMillesime(palamresResponseHttp.body, new PalmaresMillesimeRequest());
    }

    public ResponseHttp submitFormPalmaresMillesime(ResponseHttp loginFormResponse, PalmaresMillesimeResponse form) throws IOException {
        logMethod("submitFormLogin");
        ResponseHttp ret = null;

        System.out.println("");
        System.out.println("==============> Form Action:" + form.action);

        form.select.value = form.millesimeSelected.value;

        Map<String, String> data = PalmaresMillesimeFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = newHttpPostProxy().post(URL_ROOT, form.action, data, loginFormResponse);
        }

        return ret;
    }

    private String format(String str) {
        String ret = decode(str);
        ret = ret.replaceAll("\\\\n", "");
        ret = ret.replaceAll("\\\\/", "/");
        return ret;
    }

    private String decode(final String in) {
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

    private HttpGetProxy newHttpGetProxy() {
        HttpGetProxy instance = HttpGetProxy.newInstance();
        setProxy(instance);
        return instance;
    }

    private HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = HttpPostProxy.newInstance();
        setProxy(instance);
        instance.setSite(LOGON_SITE)
                .setPort(LOGON_PORT)
                .setMethod(LOGON_METHOD);
        return instance;
    }

    private void setProxy(IProxy instance) {
        instance.setProxyHost(proxyHost)
                .setProxyPort(proxyPort)
                .setProxyUser(proxyUser)
                .setProxyPw(proxyPw);
    }

    @Override
    public IProxy setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    @Override
    public IProxy setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    @Override
    public IProxy setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    @Override
    public IProxy setProxyPw(String proxyPw) {
        this.proxyPw = proxyPw;
        return this;
    }

    private void logMethod(String method) {
        System.out.println("\n==========================================================================");
        System.out.println("==============> Method:" + method);
        System.out.println("==========================================================================\r\n");

    }
}
