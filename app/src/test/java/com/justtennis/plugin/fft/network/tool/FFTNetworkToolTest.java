package com.justtennis.plugin.fft.network.tool;

import android.content.Context;

import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.service.FFTServiceLogin;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;

import junit.framework.TestCase;

import org.junit.Test;

public class FFTNetworkToolTest {//} extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    private static final boolean useProxy = true;

//    @Test
//    public static void testInitCookies(Context context) {
//        HttpMethod method = new GetMethod(URL_ROOT + "/bloc_home/redirect/classement");
//
//        FFTServiceLogin fftServiceLogin = newFFTService(context);
//        LoginFormResponse response = fftServiceLogin.getLoginForm(LOGIN, PASWD);
//
//        ResponseHttp form = fftServiceLogin.submitFormLogin(response);
//
//        NetworkTool.getInstance().setDoLog(true);
//        NetworkTool.getInstance().initCookies(method, form);
//
//        assertEquals(method.getParams().getCookiePolicy(), CookiePolicy.IGNORE_COOKIES);
//        assertNotNull(method.getRequestHeader("Cookie"));
//        assertFalse("Request Header Cookie must not be empty", method.getRequestHeader("Cookie").getValue().isEmpty());
//    }

    private static FFTServiceLogin newFFTService(Context context) {
        FFTServiceLogin instance = FFTServiceLogin.newInstance(context);
        if (useProxy) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
        return instance;
    }
}