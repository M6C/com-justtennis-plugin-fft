package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.service.FftService;

import junit.framework.TestCase;

import java.io.IOException;

public class FftServiceTest extends TestCase {

    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    public static void testGetLoginForm(String login, String password) {
        LoginFormResponse response = FftService.getLoginForm(LOGIN, PASWD);

        assertNotNull(response.action);
        assertNotNull(response.button.name);
        assertNotNull(response.login.name);
        assertNotNull(response.password.name);
        assertEquals(response.login.value, LOGIN);
        assertEquals(response.password.value, PASWD);
        assertEquals(response.input.size(), 16);
    }

    public static void testSubmitFormLogin() throws IOException {
        LoginFormResponse response = FftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FftService.submitFormLogin(response);

        assertNotNull(form.body);
        assertNotNull(form.pathRedirect);
        assertEquals(form.header.size(), 16);
    }

    public static void testNavigateToFormRedirect() throws IOException {
        LoginFormResponse response = FftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FftService.submitFormLogin(response);

        ResponseHttp formRedirect = FftService.navigateToFormRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.header.size(), 0);
    }

    public static void testNavigateToRanking() throws IOException {
        LoginFormResponse response = FftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FftService.submitFormLogin(response);

        ResponseHttp ranking = FftService.navigateToRanking(form);

        assertNotNull(ranking);
        assertNotNull(ranking.body);
        assertNotNull(ranking.pathRedirect);
        assertEquals(ranking.header.size(), 0);
    }
}