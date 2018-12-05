package com.justtennis.plugin.shared.network;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

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
        return post(root, path, data, NetworkTool.getInstance().buildCookie(http));
    }

    public ResponseHttp post(String root, String path, Map<String, String> data, String cookie) {
        ResponseHttp ret = new ResponseHttp();
        OkHttpClient.Builder clientBuilder = createClient(ret, cookie);

        String s = buildUrl(root, path);
        System.out.println("HttpPostProxy - url: " + s);

        // Initialize Builder (not RequestBody)
        FormBody.Builder builder = new FormBody.Builder();
        // Add Params to Builder
        for ( Map.Entry<String, String> entry : data.entrySet() ) {
            builder.add( entry.getKey(), entry.getValue() );
        }
        // Create RequestBody
        RequestBody formBody = builder.build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(s)
                .post(formBody);

        NetworkTool.getInstance().initCookies(requestBuilder, cookie);

        try {
            OkHttpClient client = clientBuilder.build();
            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            NetworkTool.getInstance().buildResponseHttp(ret, response);
            NetworkTool.getInstance().showHeaders(request, response);

            logResponse(TAG, ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

//    public HttpPostProxy setDoRedirect(boolean doRedirect) {
//        this.doRedirect = doRedirect;
//        return this;
//    }

//    private void doRedirect(String root, String cookie, ResponseHttp ret) {
//        String pathRedirect = ret.getHeader("Location");
//        System.out.println("Move to pathRedirect = " + pathRedirect);
//        if (pathRedirect.toLowerCase().startsWith(root.toLowerCase())) {
//            pathRedirect = pathRedirect.substring(root.length());
//        }
//
//        String c = cookie;
//        if (c == null) {
//            // Recreate c if null
//            c = NetworkTool.getInstance().buildCookie(ret);
//        }
//
//        HttpGetProxy httpGetProxy = HttpGetProxy.newInstance();
//        httpGetProxy
//                .setProxyHost(proxyHost)
//                .setProxyPort(proxyPort)
//                .setProxyUser(proxyUser)
//                .setProxyPw(proxyPw);
//        ResponseHttp retGet = httpGetProxy
//                .setDoRedirect(doRedirect)
//                .setDoLog(false)
//                .get(root, pathRedirect, c);
//        logResponse(TAG, retGet, "\r\nResponse from pathRedirect = " + pathRedirect);
//        if (NetworkTool.getInstance().isOk(retGet.statusCode)) {
//            ret.body = retGet.body;
//        }
//    }
}