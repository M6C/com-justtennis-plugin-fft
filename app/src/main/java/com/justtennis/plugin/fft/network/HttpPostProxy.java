package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.StreamTool;
import com.justtennis.plugin.fft.network.model.ResponseElement;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;
import com.justtennis.plugin.fft.skeleton.IProxy;

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

public class HttpPostProxy implements IProxy {

    private String proxyHost;
    private int    proxyPort;
    private String proxyUser;
    private String proxyPw;
    private String site;
    private int    port;
    private String method;

    private HttpPostProxy() {}

    public static HttpPostProxy newInstance() {
        return new HttpPostProxy();
    }

    public ResponseHttp post(String root, String path, Map<String, String> data) throws URIException {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();
        if (site != null && port > 0 && method != null) {
            client.getHostConfiguration().setHost(site, port, method);
        }
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        System.out.println("HttpPostProxy - url: " + root + path);

        HttpMethod method = new PostMethod(root + path);

        if (site != null && port > 0) {
            NetworkTool.showCookies(client, site, port);
        }

        for(String key : data.keySet()) {
            String d = data.get(key);
            System.out.println("HttpPostProxy - Data key: " + key + " value:" + d);
            ((PostMethod)method).addParameter(key, d);
        }

        if (proxyHost != null && proxyPort > 0) {
            HostConfiguration config = client.getHostConfiguration();
            config.setProxy(proxyHost, proxyPort);
        }

        if (proxyHost != null && proxyPort > 0 && proxyUser != null && proxyPw != null) {
            Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPw);
            AuthScope authScope = new AuthScope(proxyHost, proxyPort);
            client.getState().setProxyCredentials(authScope, credentials);
        }

        try {
            client.executeMethod(method);

            if (site != null && port > 0) {
                NetworkTool.showCookies(client, site, port);
            }

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

    public HttpPostProxy setSite(String site) {
        this.site = site;
        return this;
    }

    public HttpPostProxy setPort(int port) {
        this.port = port;
        return this;
    }

    public HttpPostProxy setMethod(String method) {
        this.method = method;
        return this;
    }

    private void addResponseHeader(ResponseHttp ret, HttpMethod method) {
        Header[] responseHeaders = method.getResponseHeaders();
        for(Header header : responseHeaders) {
            System.out.println("HttpPostProxy - addResponseHeader name:" + header.getName() + " value:" + header.getValue());
            ResponseElement head = new ResponseElement();
            head.name = header.getName();
            head.value = header.getValue();
            ret.header.add(head);
        }
    }

    private void logResponse(ResponseHttp ret) {
        System.out.println("HttpPostProxy - Status Code = " + ret.statusCode);
        System.out.println("HttpPostProxy - Response = " + ret.body);
        System.out.println("HttpPostProxy - Response = " + (ret.body != null ? ret.body.length() : 0));
    }
}