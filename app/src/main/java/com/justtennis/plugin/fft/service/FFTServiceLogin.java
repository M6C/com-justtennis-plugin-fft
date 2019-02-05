package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.query.request.FFTLoginFormRequest;
import org.cameleon.android.shared.converter.LoginFormResponseConverter;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.network.tool.NetworkTool;
import org.cameleon.android.shared.parser.FormLoginParser;
import org.cameleon.android.shared.preference.LoginSharedPref;
import org.cameleon.android.shared.query.response.LoginFormResponse;
import org.cameleon.android.shared.service.IServiceLogin;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FFTServiceLogin extends AbstractFFTService implements IServiceLogin {

    private static final String TAG = FFTServiceLogin.class.getName();

    private FFTServiceLogin(Context context) {
        super(context);
    }

    public static FFTServiceLogin newInstance(Context context) {
        return new FFTServiceLogin(context);
    }

    @Override
    public LoginFormResponse getLoginForm(String login, String password) {
        logMethod("getLoginForm");
        LoginFormResponse ret = null;
        System.out.println("\r\n" + URL_ROOT);

        ResponseHttp respRoot = doGet(URL_ROOT);
//        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        if (!StringUtil.isBlank(respRoot.body)) {
            ret = FormLoginParser.getInstance().parseForm(respRoot.body, new FFTLoginFormRequest());
            ret.login.value = login;
            ret.password.value = password;
            System.out.println("==============> form element name:" + ret.login.name + " value:" + ret.login.value);
            System.out.println("==============> form element name:" + ret.password.name + " value:" + ret.password.value);
        }

        return ret;
    }

    @Override
    public ResponseHttp submitFormLogin(LoginFormResponse form) {
        logMethod("submitFormLogin");
        ResponseHttp ret = null;

        System.out.println("");
        System.out.println("==============> FFT Form Login Action:" + form.action);

        Map<String, String> data = LoginFormResponseConverter.toDataMap(form);
        if (!StringUtil.isBlank(form.action)) {
            ret = doPost(URL_ROOT, form.action, data);

            String cookie = NetworkTool.getInstance().buildCookie(ret);
            if (!cookie.isEmpty()) {
                LoginSharedPref.setCookie(context, cookie);
                LoginSharedPref.setHomePage(context, form.action);
            } else {
                LoginSharedPref.cleanSecurity(context);
            }
        } else {
            LoginSharedPref.cleanSecurity(context);
        }

        return ret;
    }

    public ResponseHttp navigateToFormRedirect(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToFormRedirect");
        return loginFormResponse;
//        if (loginFormResponse.pathRedirect != null && !loginFormResponse.pathRedirect.isEmpty()) {
//            ResponseHttp responseHttp = doGetConnected(URL_ROOT, loginFormResponse.pathRedirect, loginFormResponse);
//            if (NetworkTool.getInstance().isOk(responseHttp.statusCode)) {
//                LoginSharedPref.setHomePage(context, responseHttp.pathRedirect);
//            } else {
//                LoginSharedPref.cleanSecurity(context);
//            }
//            return responseHttp;
//        }
//        return null;
    }

    public ResponseHttp navigateToHomePage(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToHomePage");
        String homePage = LoginSharedPref.getHomePage(context);
        if (homePage != null && !homePage.isEmpty()) {
            return doGetConnected(URL_ROOT, homePage, loginFormResponse);
        } else {
            throw new NotConnectedException("navigateToHomePage - No Home Page found");
        }
    }
}
