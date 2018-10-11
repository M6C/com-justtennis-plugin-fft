package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.converter.LoginFormResponseConverter;
import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;
import com.justtennis.plugin.fft.parser.FormParser;
import com.justtennis.plugin.fft.preference.FFTSharedPref;
import com.justtennis.plugin.fft.query.request.FFTLoginFormRequest;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FFTServiceLogin extends AbstracFFTService {

    private static final String TAG = FFTServiceLogin.class.getName();

    private FFTServiceLogin(Context context) {
        super(context);
        initializeProxy(this);
    }

    public static FFTServiceLogin newInstance(Context context) {
        return new FFTServiceLogin(context);
    }

    public LoginFormResponse getLoginForm(String login, String password) {
        logMethod("getLoginForm");
        LoginFormResponse ret = null;
        System.out.println("\r\n" + URL_ROOT);

        ResponseHttp respRoot = doGet(URL_ROOT, "");
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        if (!StringUtil.isBlank(respRoot.body)) {
            ret = FormParser.parseFormLogin(respRoot.body, new FFTLoginFormRequest());
            ret.login.value = login;
            ret.password.value = password;
        }

        return ret;
    }

    public ResponseHttp submitFormLogin(LoginFormResponse form) {
        logMethod("submitFormLogin");
        ResponseHttp ret = null;

        System.out.println("");
        System.out.println("==============> Form Action:" + form.action);

        Map<String, String> data = LoginFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = doPost(URL_ROOT, form.action, data);

            String cookie = NetworkTool.buildCookie(ret);
            if (!cookie.isEmpty()) {
                FFTSharedPref.setCookie(context, cookie);
                FFTSharedPref.setHomePage(context, form.action);
            } else {
                FFTSharedPref.cleanSecurity(context);
            }
        } else {
            FFTSharedPref.cleanSecurity(context);
        }

        return ret;
    }

    public ResponseHttp navigateToFormRedirect(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFormRedirect");
        if (loginFormResponse.pathRedirect != null && !loginFormResponse.pathRedirect.isEmpty()) {
            ResponseHttp responseHttp = doGetConnected(URL_ROOT, loginFormResponse.pathRedirect, loginFormResponse);
            if (NetworkTool.isOk(responseHttp.statusCode)) {
                FFTSharedPref.setHomePage(context, responseHttp.pathRedirect);
            } else {
                FFTSharedPref.cleanSecurity(context);
            }
            return responseHttp;
        }
        return null;
    }

    public ResponseHttp navigateToHomePage(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToHomePage");
        String homePage = FFTSharedPref.getHomePage(context);
        if (homePage != null && !homePage.isEmpty()) {
            return doGetConnected(URL_ROOT, homePage, loginFormResponse);
        } else {
            throw new NotConnectedException("navigateToHomePage - No Home Page found");
        }
    }
}
