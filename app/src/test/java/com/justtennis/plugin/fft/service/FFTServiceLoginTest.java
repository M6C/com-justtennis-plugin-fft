package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import org.junit.Test;

public class FFTServiceLoginTest extends AbstractFFTServiceTest {

    @Test
    public static void testGetLoginForm() {

        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        assertNotNull(response.action);
        assertNotNull(response.button.name);
        assertNotNull(response.login.name);
        assertNotNull(response.password.name);
        assertEquals(LOGIN, response.login.value);
        assertEquals(PASWD, response.password.value);
        assertEquals(2, response.input.size());
    }

    @Test
    public static void testSubmitFormLogin() {
        ResponseHttp form = doLogin();

        assertNotNull(form.body);
        assertNotNull(form.pathRedirect);
        assertEquals(form.header.size(), 16);
    }

    @Test
    public static void testNavigateToFormRedirect() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fftService.navigateToFormRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.header.size(), 0);
    }
}