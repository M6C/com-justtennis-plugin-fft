package com.justtennis.plugin.yout.service;

import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.query.response.LoginFormResponse;
import org.cameleon.android.shared.service.AbstractLoginServiceTest;
import org.cameleon.android.shared.skeleton.IProxy;

abstract class AbstractYouTServiceTest extends AbstractLoginServiceTest {

    private static YoutServiceLogin youtServiceLogin;

    abstract IProxy initializeService();

    @Override
    protected ResponseHttp doLogin() {
        initializeLoginService();
        return youtServiceLogin.navigateToHomePage();
    }

    @Override
    protected void initializeLoginService() {
        youtServiceLogin = YoutServiceLogin.newInstance(null);
        initializeProxy(youtServiceLogin);
        initializeProxy(initializeService());
    }

    @Override
    protected String getPaswd() {
        return null;
    }

    @Override
    protected String getLogin() {
        return null;
    }

    @Override
    protected LoginFormResponse getLoginForm() {
        return null;
    }
}