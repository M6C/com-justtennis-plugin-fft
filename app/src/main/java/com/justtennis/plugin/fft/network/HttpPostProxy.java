package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.StreamTool;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

public class HttpPostProxy extends AbstractHttpProxy {

    private static final String TAG = HttpPostProxy.class.getName();

    private boolean doRedirect = true;

    private HttpPostProxy() {super();}

    public static HttpPostProxy newInstance() {
        return new HttpPostProxy();
    }

    public ResponseHttp post(String root, String path, Map<String, String> data) {
        return post(root, path, data, (String)null);
    }

    public ResponseHttp post(String root, String path, Map<String, String> data, ResponseHttp http) {
        return post(root, path, data, NetworkTool.buildCookie(http));
    }

    public ResponseHttp post(String root, String path, Map<String, String> data, String cookie) {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();
        if (site != null && port > 0 && method != null) {
            client.getHostConfiguration().setHost(site, port, method);
        }
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

        String s = (path.contains("://")) ? path : root + path;
        System.out.println("HttpPostProxy - url: " + s);

        HttpMethod method = new PostMethod(root + path);

        NetworkTool.initCookies(method, cookie);

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

            NetworkTool.showheaders(method);

            if (site != null && port > 0) {
                NetworkTool.showCookies(client, site, port);
            }

            addResponseHeader(ret, method);
            ret.statusCode = method.getStatusCode();
            ret.pathRedirect = method.getPath();
            ret.body = StreamTool.readStream(method.getResponseBodyAsStream());

            logResponse(TAG, ret);

            if (doRedirect && NetworkTool.isRedirect(ret.statusCode)) {
                method.releaseConnection();
                method = null;

                String pathRedirect = ret.getHeader("Location");
                System.out.println("Move to pathRedirect = " + pathRedirect);
                if (pathRedirect.toLowerCase().startsWith(root.toLowerCase())) {
                    pathRedirect = pathRedirect.substring(root.length());
                }
                HttpGetProxy httpGetProxy = HttpGetProxy.newInstance();
                httpGetProxy
                        .setProxyHost(proxyHost)
                        .setProxyPort(proxyPort)
                        .setProxyUser(proxyUser)
                        .setProxyPw(proxyPw);
                ResponseHttp retGet = httpGetProxy
                        .setDoRedirect(doRedirect)
                        .setDoLog(false)
                        .get(root, pathRedirect, cookie);
                logResponse(TAG, retGet, "\r\nResponse from pathRedirect = " + pathRedirect);
                if (NetworkTool.isOk(retGet.statusCode)) {
                    ret.body = retGet.body;
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

    public HttpPostProxy setDoRedirect(boolean doRedirect) {
        this.doRedirect = doRedirect;
        return this;
    }
}