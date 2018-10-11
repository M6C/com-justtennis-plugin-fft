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
    private static boolean doLog = false;

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

        logMe("NetworkTool - initCookies - Cookie = " + cookie);
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.setRequestHeader("Cookie", cookie);
    }

    @NonNull
    public static String buildCookie(ResponseHttp response) {
        StringBuilder strCookie = new StringBuilder("");
        for(ResponseElement head : response.header) {
            if ("Set-Cookie".equals(head.name)) {
                String value = head.value.split(";", 2)[0];
                logMe(("NetworkTool - initCookies head:" + head + " value:" + value).trim());
                if (strCookie.length()>0) {
                    strCookie.append("; ");
                }
                strCookie.append(value);
            }
        }
        return strCookie.toString();
    }

    public static void showCookies(HttpClient client, String logonSite, int logonPort) {
        logMe("");
        // See if we got any cookies
        Cookie[] initcookies = cookiespec.match(logonSite, logonPort, "/", true, client.getState().getCookies());
        logMe("NetworkTool - showCookies - Initial set of cookies:");
        if (initcookies.length == 0) {
            logMe("NetworkTool - showCookes - None");
        } else {
            for (int i = 0; i < initcookies.length; i++) {
                logMe("NetworkTool - showCookies - " + initcookies[i].toString());
            }
        }
        logMe("");
    }

    public static void showheaders(HttpMethod method) {
        showRequestHeaders(method);
        showResponseHeaders(method);
    }

    public static void showRequestHeaders(HttpMethod method) {
        logMe("\r\nNetworkTool - showRequestHeaders-----------------------------");
        for (Header head : method.getRequestHeaders()) {
            logMe("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        logMe("-------------------------------------------------------------\r\n");
    }

    public static void showResponseHeaders(HttpMethod method) {
        logMe("\r\nNetworkTool - showResponseHeaders ---------------------------");
        for (Header head : method.getResponseHeaders()) {
            logMe("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        logMe("-------------------------------------------------------------\r\n");
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

    public static void setDoLog(boolean doLog) {
        NetworkTool.doLog = doLog;
    }

    private static void logMe(String message) {
        if (doLog) {
            System.out.println(message);
        }
    }
}
