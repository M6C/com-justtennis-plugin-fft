package com.justtennis.plugin.fft.network.tool;

import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.service.FFTService;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class NetworkToolTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";
    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;

    private static final boolean useProxy = true;

    public static void testInitCookies() throws IOException {
        HttpMethod method = new GetMethod(URL_ROOT + "/bloc_home/redirect/classement");

        LoginFormResponse response = newFFTService().getLoginForm(LOGIN, PASWD);

        ResponseHttp form = newFFTService().submitFormLogin(response);

        NetworkTool.initCookies(method, form);

        assertEquals(method.getParams().getCookiePolicy(), CookiePolicy.IGNORE_COOKIES);
        assertNotNull(method.getRequestHeader("Cookie"));
        assertFalse("Request Header Cookie must not be empty", method.getRequestHeader("Cookie").getValue().isEmpty());
    }

    public static void testShowCookies() {
        HttpClient client = new HttpClient();
        NetworkTool.showCookies(client, LOGON_SITE, LOGON_PORT);
    }

    public static void testIsRedirect() {
        assertTrue(NetworkTool.isRedirect(HttpStatus.SC_MOVED_TEMPORARILY));
        assertTrue(NetworkTool.isRedirect(HttpStatus.SC_MOVED_PERMANENTLY));
        assertTrue(NetworkTool.isRedirect(HttpStatus.SC_SEE_OTHER));
        assertTrue(NetworkTool.isRedirect(HttpStatus.SC_TEMPORARY_REDIRECT));
        assertFalse(NetworkTool.isRedirect(HttpStatus.SC_OK));
    }

    private static FFTService newFFTService() {
        FFTService instance = FFTService.newInstance();
        if (useProxy) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
        return instance;
    }
}