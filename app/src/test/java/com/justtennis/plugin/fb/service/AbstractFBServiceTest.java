package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.AbstractServiceTest;
import com.justtennis.plugin.shared.skeleton.IProxy;

public abstract class AbstractFBServiceTest extends AbstractServiceTest {

    private static final String LOGIN = "chauc69dan@gmail.com";
    private static final String PASWD = "dan69chauc";

    private static final boolean useProxy = true;

    protected static FBServiceLogin serviceLogin;

    protected abstract IProxy initializeService();

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
        initializeProxy(initializeService());
    }
}