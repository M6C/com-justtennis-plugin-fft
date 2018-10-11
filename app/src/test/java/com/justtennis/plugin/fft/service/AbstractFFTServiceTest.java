package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.skeleton.IProxy;

import junit.framework.TestCase;

abstract class AbstractFFTServiceTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    // ROCAdavid75 / JEkonCAEN26 - leonie.roca / ymNgfBeJ36 - delphin.roca / 123456789
    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    private static final boolean useProxy = true;

    abstract IProxy initializeService();

    static FFTServiceLogin fftServiceLogin;

    LoginFormResponse getLogin() {
        initializeFFTService();
        return fftServiceLogin.getLoginForm(LOGIN, PASWD);
    }

    ResponseHttp doLogin() {
        LoginFormResponse response = getLogin();

        return fftServiceLogin.submitFormLogin(response);
    }

    void testLogin(LoginFormResponse response) {
        assertEquals(LOGIN, response.login.value);
        assertEquals(PASWD, response.password.value);
    }

    void changeMillesimeSelected(PalmaresMillesimeResponse palmaresMillesimeResponse) {
        for(PalmaresMillesimeResponse.Millesime millesime : palmaresMillesimeResponse.listMillesime) {
            if (!millesime.equals(palmaresMillesimeResponse.millesimeSelected)) {
                palmaresMillesimeResponse.millesimeSelected = millesime;
                break;
            }
        }
    }

    private void initializeFFTService() {
        fftServiceLogin = FFTServiceLogin.newInstance(null);
        initializeProxy(fftServiceLogin);
        initializeProxy(initializeService());
    }

    private void initializeProxy(IProxy instance) {
        if (useProxy && instance != null) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
    }
}