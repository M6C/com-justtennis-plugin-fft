package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.skeleton.IProxy;

import org.junit.Test;

public class FFTServiceLoginTest extends AbstractFFTServiceTest {

    @Override
    IProxy initializeService() {
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
        assertEquals(2, response.input.size());
    }

    @Test
    public void testSubmitFormLogin() {
        ResponseHttp form = doLogin();

        assertNotNull(form.body);
        assertNotNull(form.pathRedirect);
        assertEquals(form.statusCode, 302);
        assertEquals(form.header.size(), 16);
    }

    @Test
    public void testNavigateToFormRedirect() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fftServiceLogin.navigateToFormRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.statusCode, 200);
        assertEquals(formRedirect.header.size(), 0);
    }
}