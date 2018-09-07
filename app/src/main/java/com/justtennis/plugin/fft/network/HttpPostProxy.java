package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.StreamTool;
import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// https://gerardnico.com/lang/java/httpclient
// https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
// https://www.baeldung.com/httpclient-4-cookies
// http://hc.apache.org/httpclient-3.x/cookies.html

public class HttpPostProxy {
    private static final String proxyUser = "pckh146";
    private static final String proxyPw = "k5F+n7S!";

    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;
    private static final String LOGON_METHOD = "https";

    public static void main(String[] args) {
        try {
            post("https://kodejava.org", "", new HashMap<String, String>());
        } catch (URIException e) {
            e.printStackTrace();
        }
    }

    public static ResponseHttp post(String root, String path, Map<String, String> data) throws URIException {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT, LOGON_METHOD);
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        System.out.println("HttpPostProxy - url: " + root + path);

        HttpMethod method = new PostMethod(root + path);

        NetworkTool.showCookies(client, LOGON_SITE, LOGON_PORT);

        for(String key : data.keySet()) {
            String d = data.get(key);
            System.out.println("HttpPostProxy - Data key: " + key + " value:" + d);
            ((PostMethod)method).addParameter(key, d);
        }

        HostConfiguration config = client.getHostConfiguration();
        config.setProxy(PROXY_HOST, PROXY_PORT);

        Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPw);
        AuthScope authScope = new AuthScope(PROXY_HOST, PROXY_PORT);

        client.getState().setProxyCredentials(authScope, credentials);

        try {
            client.executeMethod(method);

            NetworkTool.showCookies(client, LOGON_SITE, LOGON_PORT);

            addResponseHeader(ret, method);
            ret.statusCode = method.getStatusCode();
            ret.pathRedirect = method.getPath();
            ret.body = StreamTool.readStream(method.getResponseBodyAsStream());

            logResponse(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return ret;
    }

    private static void addResponseHeader(ResponseHttp ret, HttpMethod method) {
        Header[] responseHeaders = method.getResponseHeaders();
        for(Header header : responseHeaders) {
            System.out.println("HttpPostProxy - addResponseHeader name:" + header.getName() + " value:" + header.getValue());
            ResponseElement head = new ResponseElement();
            head.name = header.getName();
            head.value = header.getValue();
            ret.header.add(head);
        }
    }

    private static void logResponse(ResponseHttp ret) {
        System.out.println("HttpPostProxy - Status Code = " + ret.statusCode);
        System.out.println("HttpPostProxy - Response = " + ret.body);
        System.out.println("HttpPostProxy - Response = " + (ret.body != null ? ret.body.length() : 0));
    }
}