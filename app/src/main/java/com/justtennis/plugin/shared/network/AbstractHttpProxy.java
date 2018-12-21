package com.justtennis.plugin.shared.network;

import android.support.annotation.NonNull;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.network.tool.NetworkTool;
import com.justtennis.plugin.shared.skeleton.IProxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

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

    boolean doLogBody = false;

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

    void logResponse(String TAG, ResponseHttp ret) {
        logResponse(TAG, ret, "");
    }

    void logResponse(String TAG, ResponseHttp ret, String hearder) {
        System.out.println(hearder +
                "\r\n"+TAG+" - Status Code = " + ret.statusCode +
                "\r\n"+TAG+" - Response = " + (doLogBody ? ret.body : "Disabled - doLogBody=false - length:" + ret.body.length())+
                "\r\n"+TAG+" - Response Length = " + (ret.body != null ? ret.body.length() : 0) +
                "\r\n"+TAG+" - Response Header = " + (ret.header != null ? ret.header.size() : 0)
        );
    }

    /**
     * NON PROXY METHOD - COMMON UTIL METHOD
     */

    @NonNull
    String buildUrl(String root, String path) {
        String s;
        if (path == null) {
            return root;
        } else if (path.contains("://")) {
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

    @NonNull
    String buildUrl(String root, String path, Map<String, String> data) {
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
        return url.toString();
    }


    OkHttpClient.Builder createClient(ResponseHttp respHttp, String cookie) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    private ResponseHttp responseRedirect;
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url();
                        System.out.println("==========> NetworkInterceptor - url: " + url);
                        if (responseRedirect != null) {
                            Request.Builder builder = request.newBuilder();
                            String cookie = NetworkTool.getInstance().buildCookie(responseRedirect);
                            System.err.println(MessageFormat.format("==========> NetworkInterceptor - Response Redirect Cookie:{0}", cookie));
                            NetworkTool.getInstance().initCookies(builder, cookie);
                            request = builder.build();
                        }
                        Response resp = chain.proceed(request);
                        int code = resp.code();
                        if (code == 302) {
                            System.err.println(MessageFormat.format("==========> NetworkInterceptor - code:{0}", code));
                            if (!NetworkTool.getInstance().addResponseHeaderCookie(respHttp, resp)) {
                                // Get cookie from request if no cookies find in response
                                NetworkTool.getInstance().addResponseHeaderCookie(respHttp, request);
                            }
                            responseRedirect = NetworkTool.getInstance().buildResponseHttp(null, resp, request);
                        }
                        return resp;
                    }
                })
                ;

        initalizeProxy(clientBuilder);

        return clientBuilder;
    }

    void initalizeProxy(OkHttpClient.Builder clientBuilder) {
        if (proxyHost != null && proxyPort > 0) {
            clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }

        if (proxyHost != null && proxyPort > 0 && proxyUser != null && proxyPw != null) {
            Authenticator proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) {
                    String credential = Credentials.basic(proxyUser, proxyPw);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
            clientBuilder.proxyAuthenticator(proxyAuthenticator);
        }
    }
}