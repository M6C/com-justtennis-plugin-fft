package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;

import junit.framework.TestCase;

public abstract class AbstractFFTServiceTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    // ROCAdavid75 / JEkonCAEN26 - leonie.roca / ymNgfBeJ36 - delphin.roca / 123456789
    static final String LOGIN = "leandre.roca2006";
    static final String PASWD = "lR123456789";

    private static final boolean useProxy = true;

    static FFTService fftService;

    static ResponseHttp doLogin() {
        fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        return fftService.submitFormLogin(response);
    }

    static void changeMillesimeSelected(PalmaresMillesimeResponse palmaresMillesimeResponse) {
        for(PalmaresMillesimeResponse.Millesime millesime : palmaresMillesimeResponse.listMillesime) {
            if (!millesime.equals(palmaresMillesimeResponse.millesimeSelected)) {
                palmaresMillesimeResponse.millesimeSelected = millesime;
                break;
            }
        }
    }

    static FFTService newFFTService() {
        FFTService instance = FFTService.newInstance(null);
        if (useProxy) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
        return instance;
    }
}