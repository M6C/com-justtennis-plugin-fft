package com.justtennis.plugin.fft.network;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class HttpGetProxy {
    private static final String proxyUser = "pckh146";
    private static final String proxyPw = "k5F+n7S!";

    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    public static void main(String[] args) {
        get("https://kodejava.org");
    }

    public static HttpMethod get(String url) {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);

        HostConfiguration config = client.getHostConfiguration();
        config.setProxy(PROXY_HOST, PROXY_PORT);

        Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPw);
        AuthScope authScope = new AuthScope(PROXY_HOST, PROXY_PORT);

        client.getState().setProxyCredentials(authScope, credentials);

        try {
            client.executeMethod(method);

            if (method.getStatusCode() == HttpStatus.SC_OK) {
                String response = method.getResponseBodyAsString();
                System.out.println("Response = " + response);
            } else {
                while (method.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                    method.releaseConnection();
                    String urlTmp = url + method.getPath();
                    System.out.println("Move to path = " + urlTmp);
                    method = get(urlTmp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return method;
    }
}