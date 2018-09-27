package com.justtennis.plugin.fft.network.tool;

import android.support.annotation.NonNull;

import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
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

        initCookies(method, buildCookie(response));
    }

    public static void initCookies(HttpMethod method, String cookie) {
        if (cookie == null || cookie.isEmpty()) {
            return;
        }

        System.out.println("NetworkTool - initCookies - Cookie = " + cookie);
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.setRequestHeader("Cookie", cookie);
    }

    @NonNull
    public static String buildCookie(ResponseHttp response) {
        StringBuilder strCookie = new StringBuilder("");
        for(ResponseElement head : response.header) {
            if ("Set-Cookie".equals(head.name)) {
                String value = head.value.split(";", 2)[0];
                System.out.println(("NetworkTool - initCookies head:" + head + " value:" + value).trim());
                if (strCookie.length()>0) {
                    strCookie.append("; ");
                }
                strCookie.append(value);
            }
        }
        return strCookie.toString();
    }

    public static void showCookies(HttpClient client, String logonSite, int logonPort) {
        System.out.println("");
        // See if we got any cookies
        Cookie[] initcookies = cookiespec.match(logonSite, logonPort, "/", true, client.getState().getCookies());
        System.out.println("NetworkTool - showCookies - Initial set of cookies:");
        if (initcookies.length == 0) {
            System.out.println("NetworkTool - showCookes - None");
        } else {
            for (int i = 0; i < initcookies.length; i++) {
                System.out.println("NetworkTool - showCookies - " + initcookies[i].toString());
            }
        }
        System.out.println("");
    }

    public static void showheaders(HttpMethod method) {
        showRequestHeaders(method);
        showResponseHeaders(method);
    }

    public static void showRequestHeaders(HttpMethod method) {
        System.out.println("\r\nNetworkTool - showRequestHeaders-----------------------------");
        for (Header head : method.getRequestHeaders()) {
            System.out.println("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        System.out.println("-------------------------------------------------------------\r\n");
    }

    public static void showResponseHeaders(HttpMethod method) {
        System.out.println("\r\nNetworkTool - showResponseHeaders ---------------------------");
        for (Header head : method.getResponseHeaders()) {
            System.out.println("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        System.out.println("-------------------------------------------------------------\r\n");
    }

    public static boolean isOk(int statusCode) {
        return statusCode == HttpStatus.SC_OK;
    }

    public static boolean isRedirect(int statusCode) {
        return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                statusCode== HttpStatus.SC_MOVED_PERMANENTLY ||
                statusCode== HttpStatus.SC_SEE_OTHER ||
                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
    }
}
