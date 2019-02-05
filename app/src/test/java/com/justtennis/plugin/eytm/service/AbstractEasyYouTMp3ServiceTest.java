package com.justtennis.plugin.eytm.service;

import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.query.response.LoginFormResponse;
import org.cameleon.android.shared.service.AbstractLoginServiceTest;
import org.cameleon.android.shared.skeleton.IProxy;

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