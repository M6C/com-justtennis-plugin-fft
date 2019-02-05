package org.cameleon.android.shared.service;

import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.query.response.LoginFormResponse;

public abstract class AbstractLoginServiceTest extends AbstractServiceTest {

    protected abstract String getPaswd();
    protected abstract String getLogin();


    protected abstract LoginFormResponse getLoginForm();
    protected abstract ResponseHttp doLogin();
    protected abstract void initializeLoginService();

    protected void testLogin(LoginFormResponse response) {
        assertEquals(getLogin(), response.login.value);
        assertEquals(getPaswd(), response.password.value);
    }
}