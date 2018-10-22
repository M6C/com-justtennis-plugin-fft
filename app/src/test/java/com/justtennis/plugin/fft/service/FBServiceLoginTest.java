package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import org.junit.Test;

public class FBServiceLoginTest extends AbstractFBServiceTest {

//    @Test
//    public void testGetLoginForm() {
//
//        LoginFormResponse response = getLoginForm();
//
//        assertNotNull(response.action);
//        assertNotNull(response.button.name);
//        assertNotNull(response.login.name);
//        assertNotNull(response.password.name);
//        testLogin(response);
//        assertEquals(12, response.input.size());
//    }
//
//    @Test
//    public void testSubmitFormLogin() {
//        ResponseHttp form = doLogin();
//
//        assertNotNull(form.body);
//        assertEquals(302, form.statusCode);
//        assertEquals(15, form.header.size());
//    }
//
//    @Test
//    public void testNavigateToFormRedirect() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = serviceLogin.navigateToFormRedirect(form);
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//        assertNotNull(formRedirect.pathRedirect);
//        assertEquals(formRedirect.statusCode, 200);
//        assertEquals(formRedirect.header.size(), 0);
//    }

    @Test
    public void testNavigateToHomePage() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = serviceLogin.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.statusCode, 200);
        assertEquals(formRedirect.header.size(), 0);
    }
}