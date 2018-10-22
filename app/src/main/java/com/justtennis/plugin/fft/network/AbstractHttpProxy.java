package com.justtennis.plugin.fft.network;

import android.support.annotation.NonNull;

import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.skeleton.IProxy;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;

// https://gerardnico.com/lang/java/httpclient
// https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
// https://www.baeldung.com/httpclient-4-cookies
// http://hc.apache.org/httpclient-3.x/cookies.html

public class AbstractHttpProxy implements IProxy {

    String proxyHost;
    int    proxyPort;
    String proxyUser;
    String proxyPw;
    String site;
    int    port;
    String method;

    AbstractHttpProxy() {}

    @Override
    public IProxy setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    @Override
    public IProxy setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    @Override
    public IProxy setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    @Override
    public IProxy setProxyPw(String proxyPw) {
        this.proxyPw = proxyPw;
        return this;
    }

    public AbstractHttpProxy setSite(String site) {
        this.site = site;
        return this;
    }

    public AbstractHttpProxy setPort(int port) {
        this.port = port;
        return this;
    }

    public AbstractHttpProxy setMethod(String method) {
        this.method = method;
        return this;
    }

    void addResponseHeader(ResponseHttp ret, HttpMethod method) {
        Header[] responseHeaders = method.getResponseHeaders();
        System.out.println("HttpPostProxy - addResponseHeader size:" + responseHeaders.length);
        for(Header header : responseHeaders) {
//            System.out.println("HttpPostProxy - addResponseHeader name:" + header.getName() + " value:" + header.getValue());
            ResponseElement head = new ResponseElement();
            head.name = header.getName();
            head.value = header.getValue();
            ret.header.add(head);
        }
    }

    void logResponse(String TAG, ResponseHttp ret) {
        logResponse(TAG, ret, "");
    }

    void logResponse(String TAG, ResponseHttp ret, String hearder) {
        System.out.println(hearder +
            "\r\n"+TAG+" - Status Code = " + ret.statusCode +
            "\r\n"+TAG+" - Response = " + ret.body +
            "\r\n"+TAG+" - Response = " + (ret.body != null ? ret.body.length() : 0)
        );
    }

    /**
     * NON PROXY METHOD - COMMON UTIL METHOD
     */

    @NonNull
    String buildUrl(String root, String path) {
        String s;
        if (path.contains("://")) {
            s = path;
        } else {
            if (root.endsWith("/") && path.startsWith("/")) {
                s = root + path.substring(1);
            } else {
                s = root + path;
            }
        }
        return s;
    }
}