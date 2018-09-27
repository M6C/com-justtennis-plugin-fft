package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.StreamTool;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.network.tool.NetworkTool;
import com.justtennis.plugin.fft.skeleton.IProxy;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpGetProxy implements IProxy {

    private String proxyHost;
    private int    proxyPort;
    private String proxyUser;
    private String proxyPw;

    private HttpGetProxy() {}

    public static HttpGetProxy newInstance() {
        return new HttpGetProxy();
    }

    public ResponseHttp get(String root, String path) {
        return get(root, path, (String)null);
    }

    public ResponseHttp get(String root, String path, ResponseHttp http) {
        String cookie = NetworkTool.buildCookie(http);
        return get(root, path, cookie);
    }

    public ResponseHttp get(String root, String path, String cookie) {
        return get(root, path, null, cookie);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data) {
        return get(root, path, data, (String)null);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data, ResponseHttp http) {
        String cookie = NetworkTool.buildCookie(http);
        return get(root, path, data, cookie);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data, String cookie) {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();

        StringBuilder url = new StringBuilder(root).append(path);
        if (data != null && data.size() > 0) {
            try {
                boolean first = true;
                for(String key : data.keySet()) {
                    if (first) {
                        first = false;
                        url.append("?");
                    } else {
                        url.append("&");
                    }
                    url.append(key).append("=").append(URLEncoder.encode(data.get(key), "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HttpMethod method = new GetMethod(url.toString());

        System.out.println("HttpGetProxy - url: " + url.toString());

        NetworkTool.initCookies(method, cookie);

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

            ret.statusCode = method.getStatusCode();
            ret.pathRedirect = method.getPath();

            System.out.println("HttpGetProxy - StatusCode:" + ret.statusCode);

            if (ret.statusCode == HttpStatus.SC_OK) {
                ret.body = StreamTool.readStream(method.getResponseBodyAsStream());
                System.out.println("HttpGetProxy - Response: " + ret.body);
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
}