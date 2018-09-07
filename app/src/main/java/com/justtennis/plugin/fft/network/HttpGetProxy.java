package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.StreamTool;
import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class HttpGetProxy {
    private static final String proxyUser = "pckh146";
    private static final String proxyPw = "k5F+n7S!";

    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    public static void main(String[] args) {
        get("https://kodejava.org", "");
    }

    public static ResponseHttp get(String root, String path) {
        return get(root, path, null);
    }

    public static ResponseHttp get(String root, String path, ResponseHttp http) {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(root + path);

        System.out.println("HttpGetProxy - url: " + root + path);

        initCookies(method, http);

        HostConfiguration config = client.getHostConfiguration();
        config.setProxy(PROXY_HOST, PROXY_PORT);

        Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPw);
        AuthScope authScope = new AuthScope(PROXY_HOST, PROXY_PORT);

        client.getState().setProxyCredentials(authScope, credentials);

        try {
            client.executeMethod(method);

            ret.statusCode = method.getStatusCode();
            ret.pathRedirect = method.getPath();

            if (ret.statusCode == HttpStatus.SC_OK) {
                System.out.println("Response = " + StreamTool.readStream(method.getResponseBodyAsStream()));
            } else {
                while (NetworkTool.isRedirect(ret.statusCode)) {
                    method.releaseConnection();
                    method = null;
                    System.out.println("Move to pathRedirect = " + root + ret.pathRedirect);
                    ret = get(root, ret.pathRedirect);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }

        return ret;
    }

    private static void initCookies(HttpMethod method, ResponseHttp response) {
        if (method == null || response == null) {
            return;
        }

        StringBuilder strCookie = new StringBuilder("");
        for(ResponseElement head : response.header) {
            System.out.println("HttpGetProxy - initCookies head:" + head);
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
}