package com.justtennis.plugin.mytd.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.AbstractLoginServiceTest;
import com.justtennis.plugin.shared.skeleton.IProxy;

abstract class AbstractMp3YouTDwnServiceTest extends AbstractLoginServiceTest {

    private Mp3YouTDwnServiceLogin youtServiceLogin;

    abstract IProxy initializeService();

    @Override
    protected ResponseHttp doLogin() {
        initializeLoginService();
        return youtServiceLogin.navigateToHomePage();
    }

    @Override
    protected void initializeLoginService() {
        youtServiceLogin = Mp3YouTDwnServiceLogin.newInstance(null);
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