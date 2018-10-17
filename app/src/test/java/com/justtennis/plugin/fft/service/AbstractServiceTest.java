package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.skeleton.IProxy;

import junit.framework.TestCase;

abstract class AbstractServiceTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final boolean useProxy = true;

    protected abstract String getPaswd();
    protected abstract String getLogin();


    protected abstract LoginFormResponse getLoginForm();
    protected abstract ResponseHttp doLogin();
    protected abstract void initializeFFTService();

    protected void testLogin(LoginFormResponse response) {
        assertEquals(getLogin(), response.login.value);
        assertEquals(getPaswd(), response.password.value);
    }

    protected void initializeProxy(IProxy instance) {
        if (useProxy && instance != null) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
    }
}