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

    private static NetworkTool instance;
    private CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
    private boolean doLog = false;

    private NetworkTool() {}

    public static NetworkTool getInstance() {
        if (instance == null) {
            instance = new NetworkTool();
        }
        return instance;
    }

    public static NetworkTool getInstance(boolean doLog) {
        return getInstance().setDoLog(doLog);
    }

    public void initCookies(HttpMethod method, ResponseHttp response) {
        if (method == null || response == null) {
            return;
        }

        initCookies(method, buildCookie(response));
    }

    public void initCookies(HttpMethod method, String cookie) {
        if (cookie == null || cookie.isEmpty()) {
            logMe("NetworkTool - initCookies - Cookie FAKE");
            cookie = "sb=ib7NW5bLUHA8AqcU3pUTadLS; datr=ib7NW_JkbLr3pUBT7WyqD0tP; fr=1qQG9g3uHVHy6FCg0..Bbzb6J.lT.AAA.0.0.Bbzb6J.AWXAM1eo; reg_ext_ref=https%3A%2F%2Fwww.google.fr%2F; reg_fb_ref=https%3A%2F%2Ffr-fr.facebook.com%2F; reg_fb_gate=https%3A%2F%2Ffr-fr.facebook.com%2F";
        }
        logMe("NetworkTool - initCookies - Cookie = " + cookie);

        method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        method.setRequestHeader("Cookie", cookie);

//            method.setRequestHeader("Accept-Encoding", "gzip, deflate, br");
        method.setRequestHeader("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
        method.setRequestHeader("Cache-Control", "max-age=0");
//            client.getParams().setParameter("content-length", "745");
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        method.setRequestHeader("Origin", "https://fr-fr.facebook.com");
        method.setRequestHeader("Referer", "https://fr-fr.facebook.com/");
        method.setRequestHeader("Upgrade-Insecure-Requests", "1");
        method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    }

    @NonNull
    public String buildCookie(ResponseHttp response) {
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

    public void showCookies(HttpClient client, String logonSite, int logonPort) {
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

    public void showheaders(HttpMethod method) {
        showRequestHeaders(method);
        showResponseHeaders(method);
    }

    public void showRequestHeaders(HttpMethod method) {
        logMe("\r\nNetworkTool - showRequestHeaders----------------------------- size:" + method.getRequestHeaders().length);
        for (Header head : method.getRequestHeaders()) {
            logMe("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        logMe("-------------------------------------------------------------\r\n");
    }

    public void showResponseHeaders(HttpMethod method) {
        logMe("\r\nNetworkTool - showResponseHeaders --------------------------- size:" + method.getResponseHeaders().length);
        for (Header head : method.getResponseHeaders()) {
            logMe("NetworkTool - " + head.getName() + ":" + head.getValue());
        }
        logMe("-------------------------------------------------------------\r\n");
    }

    public boolean isOk(int statusCode) {
        return statusCode == HttpStatus.SC_OK;
    }

    public boolean isRedirect(int statusCode) {
        return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                statusCode== HttpStatus.SC_MOVED_PERMANENTLY ||
                statusCode== HttpStatus.SC_SEE_OTHER ||
                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
    }

    public NetworkTool setDoLog(boolean doLog) {
        NetworkTool.getInstance().doLog = doLog;
        return this;
    }

    private void logMe(String message) {
        if (doLog) {
            System.out.println(message);
        }
    }
}
