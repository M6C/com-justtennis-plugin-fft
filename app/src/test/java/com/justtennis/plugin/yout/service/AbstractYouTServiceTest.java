package com.justtennis.plugin.yout.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.AbstractLoginServiceTest;
import com.justtennis.plugin.shared.skeleton.IProxy;

abstract class AbstractYouTServiceTest extends AbstractLoginServiceTest {

    private static YoutServiceLogin youtServiceLogin;

    abstract IProxy initializeService();

    @Override
    protected ResponseHttp doLogin() {
        initializeFFTService();
        return youtServiceLogin.navigateToHomePage();
    }

    @Override
    protected void initializeFFTService() {
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