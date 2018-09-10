package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.converter.LoginFormResponseConverter;
import com.justtennis.plugin.fft.model.FFTLoginFormRequest;
import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.parser.FormParser;

import java.io.IOException;
import java.util.Map;

public class FftService {

    private static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";

    private FftService() {
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
}
