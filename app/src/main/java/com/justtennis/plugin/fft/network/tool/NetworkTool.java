package com.justtennis.plugin.fft.network.tool;

import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class NetworkTool {

    private static CookieSpec cookiespec = CookiePolicy.getDefaultSpec();

    private NetworkTool() {}

    public static void initCookies(HttpMethod method, ResponseHttp response) {
        if (method == null || response == null) {
            return;
        }

        StringBuilder strCookie = new StringBuilder("");
        for(ResponseElement head : response.header) {
            if ("Set-Cookie".equals(head.name)) {
                String value = head.value.split(";", 2)[0];
                System.out.println(("HttpGetProxy - initCookies head:" + head + " value:" + value).trim());
                if (strCookie.length()>0) {
                    strCookie.append("; ");
                }
                strCookie.append(value);
            }
        }

        System.out.println("HttpGetProxy - initCookies - Cookie = " + strCookie);
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.setRequestHeader("Cookie", strCookie.toString());
    }

    public static void showCookies(HttpClient client, String logonSite, int logonPort) {
        System.out.println("");
        // See if we got any cookies
        Cookie[] initcookies = cookiespec.match(logonSite, logonPort, "/", true, client.getState().getCookies());
        System.out.println("HttpPostProxy - showCookies - Initial set of cookies:");
        if (initcookies.length == 0) {
            System.out.println("HttpPostProxy - showCookes - None");
        } else {
            for (int i = 0; i < initcookies.length; i++) {
                System.out.println("HttpPostProxy - showCookies - " + initcookies[i].toString());
            }
        }
        System.out.println("");
    }

    public static boolean isRedirect(int statusCode) {
        return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                statusCode== HttpStatus.SC_MOVED_PERMANENTLY ||
                statusCode== HttpStatus.SC_SEE_OTHER ||
                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
    }
}
