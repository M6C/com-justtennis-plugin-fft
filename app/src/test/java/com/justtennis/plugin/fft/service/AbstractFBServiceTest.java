package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

class AbstractFBServiceTest extends AbstractServiceTest {

    private static final String LOGIN = "LOGIN";
    private static final String PASWD = "PASWD";

    private static final boolean useProxy = true;

    static FBServiceLogin serviceLogin;

    @Override
    protected LoginFormResponse getLoginForm() {
        initializeFFTService();
        return serviceLogin.getLoginForm(LOGIN, PASWD);
    }

    @Override
    protected ResponseHttp doLogin() {
        LoginFormResponse response = getLoginForm();

        return serviceLogin.submitFormLogin(response);
    }

    @Override
    protected String getPaswd() {
        return PASWD;
    }

    @Override
    protected String getLogin() {
        return LOGIN;
    }

    @Override
    protected void initializeFFTService() {
        serviceLogin = FBServiceLogin.newInstance(null);
        initializeProxy(serviceLogin);
    }
}