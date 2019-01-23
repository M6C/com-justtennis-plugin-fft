package com.justtennis.plugin.eytm.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.service.AbstractLoginServiceTest;
import com.justtennis.plugin.shared.skeleton.IProxy;

abstract class AbstractEasyYouTMp3ServiceTest extends AbstractLoginServiceTest {

    abstract IProxy initializeService();

    @Override
    protected ResponseHttp doLogin() {
        initializeLoginService();
        return new ResponseHttp();
    }

    @Override
    protected void initializeLoginService() {
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