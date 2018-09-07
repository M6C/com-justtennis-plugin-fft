package com.justtennis.plugin.fft.network.tool;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class NetworkTool {

    private static CookieSpec cookiespec = CookiePolicy.getDefaultSpec();

    private NetworkTool() {}

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

    public static boolean isRedirect(HttpMethod method) {
        return method.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY ||
                method.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY ||
                method.getStatusCode() == HttpStatus.SC_SEE_OTHER ||
                method.getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT;
    }
}
