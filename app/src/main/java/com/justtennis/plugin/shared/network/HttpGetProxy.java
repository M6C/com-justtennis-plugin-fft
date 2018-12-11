package com.justtennis.plugin.shared.network;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        OkHttpClient.Builder clientBuilder = createClient(ret, cookie);

        String s = buildUrl(root, path, data);
        logMe("HttpGetProxy - url: " + s);

        Request.Builder requestBuilder = new Request.Builder()
                .url(s)
                .get();

        NetworkTool.getInstance(doLog).initCookies(requestBuilder, cookie);

        try {
            OkHttpClient client = clientBuilder.build();
            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            NetworkTool.getInstance().buildResponseHttp(ret, response, request);
            NetworkTool.getInstance(doLog).showHeaders(request, response);

//            if (ret.statusCode == HttpStatus.SC_OK) {
//            } else {
//                while (doRedirect && NetworkTool.getInstance(doLog).isRedirect(ret.statusCode)) {
//                    logMe("Move to pathRedirect = " + root + ret.pathRedirect);
//                    ret = get(root, ret.pathRedirect, cookie);
//                }
//            }
            logResponse(TAG, ret);
        } catch (IOException e) {
            e.printStackTrace();
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