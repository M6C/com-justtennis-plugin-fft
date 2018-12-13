package com.justtennis.plugin.shared.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;

public abstract class AbstractLoginServiceTest extends AbstractServiceTest {

    protected abstract String getPaswd();
    protected abstract String getLogin();


    protected abstract LoginFormResponse getLoginForm();
    protected abstract ResponseHttp doLogin();
    protected abstract void initializeFFTService();

    protected void testLogin(LoginFormResponse response) {
        assertEquals(getLogin(), response.login.value);
        assertEquals(getPaswd(), response.password.value);
    }
}