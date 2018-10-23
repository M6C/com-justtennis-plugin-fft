package com.justtennis.plugin.shared.network;

import com.justtennis.plugin.shared.StreamTool;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;

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

public class HttpGetProxy extends AbstractHttpProxy {

    private static final String TAG = HttpGetProxy.class.getName();

    private boolean doRedirect = true;
    private boolean doLog = true;

    private HttpGetProxy() {}

    public static HttpGetProxy newInstance() {
        return new HttpGetProxy();
    }

    public ResponseHttp get(String root, String path) {
        return get(root, path, (String)null);
    }

    public ResponseHttp get(String root, String path, ResponseHttp http) {
        String cookie = NetworkTool.getInstance(doLog).buildCookie(http);
        return get(root, path, cookie);
    }

    public ResponseHttp get(String root, String path, String cookie) {
        return get(root, path, null, cookie);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data) {
        return get(root, path, data, (String)null);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data, ResponseHttp http) {
        String cookie = NetworkTool.getInstance(doLog).buildCookie(http);
        return get(root, path, data, cookie);
    }

    public ResponseHttp get(String root, String path, Map<String, String> data, String cookie) {
        ResponseHttp ret = new ResponseHttp();
        HttpClient client = new HttpClient();

        String s = buildUrl(root, path);
        StringBuilder url = new StringBuilder(s);
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
        s = url.toString();

        logMe("HttpGetProxy - url: " + s);
        HttpMethod method = new GetMethod(s);

        NetworkTool.getInstance(doLog).initCookies(method, cookie);

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

            NetworkTool.getInstance(doLog).showheaders(method);

            addResponseHeader(ret, method);
            ret.statusCode = method.getStatusCode();
            ret.pathRedirect = method.getPath();

            if (ret.statusCode == HttpStatus.SC_OK) {
                ret.body = StreamTool.readStream(method.getResponseBodyAsStream());
            } else {
                while (doRedirect && NetworkTool.getInstance(doLog).isRedirect(ret.statusCode)) {
                    method.releaseConnection();
                    method = null;
                    logMe("Move to pathRedirect = " + root + ret.pathRedirect);
                    ret = get(root, ret.pathRedirect, cookie);
                }
            }
            logResponse(TAG, ret);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }

        return ret;
    }
    public HttpGetProxy setDoRedirect(boolean doRedirect) {
        this.doRedirect = doRedirect;
        return this;
    }

    public HttpGetProxy setDoLog(boolean doLog) {
        this.doLog = doLog;
        return this;
    }

    @Override
    void logResponse(String TAG, ResponseHttp ret, String hearder) {
        if (doLog) {
            super.logResponse(TAG, ret, hearder);
        }
    }

    private void logMe(String message) {
        if (doLog) {
            System.out.println(message);
        }
    }
}