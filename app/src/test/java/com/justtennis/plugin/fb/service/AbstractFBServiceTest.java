package com.justtennis.plugin.fb.service;

import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.query.response.LoginFormResponse;
import org.cameleon.android.shared.service.AbstractLoginServiceTest;
import org.cameleon.android.shared.skeleton.IProxy;

public abstract class AbstractFBServiceTest extends AbstractLoginServiceTest {

    private static final String LOGIN = "LOGIN";
    private static final String PASWD = "PASSWORD";

    private static FBServiceLogin serviceLogin;

    protected abstract IProxy initializeService();

    @Override
    protected LoginFormResponse getLoginForm() {
        initializeLoginService();
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
    protected void initializeLoginService() {
        serviceLogin = FBServiceLogin.newInstance(null);
        initializeProxy(serviceLogin);
        initializeProxy(initializeService());
    }
}