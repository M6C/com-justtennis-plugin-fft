package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class FBServiceLoginTest extends AbstractFBServiceTest {

    @Override
    protected IProxy initializeService() {
        return null;
    }

    @Test
    public void testGetLoginForm() {

        LoginFormResponse response = getLoginForm();

        assertNotNull(response.action);
        assertNotNull(response.button.name);
        assertNotNull(response.login.name);
        assertNotNull(response.password.name);
        testLogin(response);
        assertEquals(12, response.input.size());
    }

    @Test
    public void testSubmitFormLogin() {
        ResponseHttp form = doLogin();
        assertEquals(form.statusCode, 200);

        String cookie = NetworkTool.getInstance().buildCookie(form);
        System.err.println("==========> Cookie:" + cookie);
        assertNotNull(cookie);
        assertFalse(cookie.isEmpty());

        assertNotNull(form.body);
        assertFalse(form.body.isEmpty());
        System.err.println("==========> Body Length:" + form.body.length());
        writeResourceFile(form.body, "FBServiceLoginTest_testSubmitFormLogin.html");
    }

// OkHttp Automatic redirect
//    @Test
//    public void testNavigateToFormRedirect() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = serviceLogin.navigateToFormRedirect(form);
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//        assertFalse(formRedirect.body.isEmpty());
//        System.err.println("==========> Body Length:" + formRedirect.body.length());
//        writeResourceFile(formRedirect.body, "test2.html");
//        assertNotNull(formRedirect.pathRedirect);
//        assertEquals(formRedirect.statusCode, 200);
//        assertEquals(formRedirect.header.size(), 0);
//    }
}