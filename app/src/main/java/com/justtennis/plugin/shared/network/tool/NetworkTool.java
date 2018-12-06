package com.justtennis.plugin.shared.network.tool;

import android.support.annotation.NonNull;

import com.justtennis.plugin.shared.network.model.ResponseElement;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkTool {

    private static NetworkTool instance;
    //    private CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
    private boolean doLog = false;
    private static final String cookie_empty = "reg_ext_ref=cookie_empty";

    private NetworkTool() {}

    public static NetworkTool getInstance() {
        if (instance == null) {
            instance = new NetworkTool();
        }
        return instance;
    }

    public static NetworkTool getInstance(boolean doLog) {
        return getInstance().setDoLog(doLog);
    }

//    public void initCookies(OkHttpClient.Builder clientBuilder) {
//        CookieHandler cookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
//        clientBuilder.cookieJar(new CookieJar() {
//
//            private List<Cookie> cookies;
//
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                this.cookies =  cookies;
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//                if (cookies != null)
//                    return cookies;
//                return new ArrayList<Cookie>();
//
//            }
//        });
//
//    }

    public void initCookies(OkHttpClient.Builder client, String cookie) {
//        HttpLoggingInterceptor logging1 = new HttpLoggingInterceptor();
//        logging1.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        client.addInterceptor(logging1);
//
//        HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();
//        logging2.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        client.addInterceptor(logging2);

//        client.cookieJar(new CookieJar() {
//            @Override
//            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//                final ArrayList<Cookie> oneCookie = new ArrayList<>(1);
//                Cookie persistentCookie = createNonPersistentCookie(cookie);
//                if (persistentCookie != null) {
//                    oneCookie.add(persistentCookie);
//                }
//                return oneCookie;
//            }
//        });
/*
        client.addInterceptor(chain -> {
            final Request original = chain.request();

            Request.Builder builder = original.newBuilder();
            if (cookie != null) {
                builder.addHeader("Cookie", cookie);
            }
            final Request authorized = builder.build();

            return chain.proceed(authorized);
        });
*/
    }

//    @Nullable
//    private static Cookie createNonPersistentCookie(String cookie) {
//        return (cookie == null) ? null : new Cookie.Builder()
////                .domain("fr-fr.facebook.com")
//                .domain("mon-espace-tennis.fft.fr")
//                .path("/")
//                .name("Cookie")
//                .value(cookie)
////                .httpOnly()
//                .secure()
//                .build();
//    }
//
//    public void initCookies(Request.Builder method, ResponseHttp response) {
//        if (method == null || response == null) {
//            return;
//        }
//
//        initCookies(method, buildCookie(response));
//    }

    public void initCookies(Request.Builder method, String cookie) {
        logMe("NetworkTool - initCookies - Cookie Request.Builder ------------------>");
        logMe("NetworkTool - initCookies - Cookie = " + cookie);

        Map<String, String> headers = getHeaders(cookie);
        for(String key : headers.keySet()) {
            String val = headers.get(key);
            logMe(MessageFormat.format("NetworkTool - initCookies - Cookie addHeader key:{0} value:{1}", key, val));
            method.addHeader(key, val);
        }
        logMe("NetworkTool - initCookies - Cookie ------------------<");
    }

    public void initCookies(Request method, String cookie) {
        logMe("NetworkTool - initCookies - Cookie Request ------------------>");
        logMe("NetworkTool - initCookies - Cookie = " + cookie);

        Map<String, String> headers = getHeaders(cookie);
        Headers headers1 = method.headers();
        for(String key : headers.keySet()) {
            String val = headers.get(key);
            headers1.names().add(key);
            logMe(MessageFormat.format("NetworkTool - initCookies - Cookie addHeader key:{0} value:{1}", key, val));
            headers1.values(key).add(val);
        }
        logMe("NetworkTool - initCookies - Cookie ------------------<");
    }

    @NonNull
    public String buildCookie(ResponseHttp response) {
        StringBuilder strCookie = new StringBuilder("");
        for(ResponseElement head : response.headerCookie) {
            String name = head.name;
            String value = head.value.split(";", 2)[0];
            logMe((MessageFormat.format("NetworkTool - buildCookie head:{0} value:{1}", head, value).trim()));
            if (strCookie.length() > 0) {
                strCookie.append("; ");
            }
            strCookie.append(MessageFormat.format("{0}={1}", name, value));
        }
        return strCookie.toString();
    }

    @NonNull
    public String buildCookie(Response response) {
        StringBuilder strCookie = new StringBuilder("");
        for(String name : response.headers().names()) {
            if ("Set-Cookie".equals(name)) {
                String val = response.headers().get(name);
                String value = val.split(";", 2)[0];
                logMe(("NetworkTool - initCookies head:" + name + " value:" + value).trim());
                if (strCookie.length()>0) {
                    strCookie.append("; ");
                }
                strCookie.append(value);
            }
        }
        return strCookie.toString();
    }

    public ResponseHttp buildResponseHttp(ResponseHttp respHttp, Response response, Request request) {
        ResponseHttp ret = (respHttp != null) ? respHttp : new ResponseHttp();
        if (!addResponseHeader(ret, response)) {
            // Get cookie from request if no cookies find in response
            addResponseHeaderCookie(ret, request);
        }
        ret.statusCode = response.code();
//        if (response.code() == 302) {
//            ret.pathRedirect = response.request().url().toString();//.encodedQuery();
//        }
        try {
            if (response.body() != null) {
                ret.body = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            logMe(e);
        }
        return ret;
    }

//    public void showCookies(HttpClient client, String logonSite, int logonPort) {
//        logMe("");
//        // See if we got any cookies
//        Cookie[] initcookies = cookiespec.match(logonSite, logonPort, "/", true, client.getState().getCookies());
//        logMe("NetworkTool - showCookies - Initial set of cookies:");
//        if (initcookies.length == 0) {
//            logMe("NetworkTool - showCookes - None");
//        } else {
//            for (int i = 0; i < initcookies.length; i++) {
//                logMe("NetworkTool - showCookies - " + initcookies[i].toString());
//            }
//        }
//        logMe("");
//    }

    public void showHeaders(Request request, Response response) {
        showRequestHeaders(request);
        showResponseHeaders(response);
    }

    public void showRequestHeaders(Request request) {
        showRequestHeaders("showRequestHeaders", request.headers());
    }

    public void showResponseHeaders(Response response) {
        String title = "showResponseHeaders";
        logMe("\r\nNetworkTool - "+title+"----------------------------- response code:" + response.code());
        showRequestHeaders(title, response.headers());
    }

    public boolean isOk(int statusCode) {
        return statusCode == HttpStatus.SC_OK;
    }

    public boolean isRedirect(int statusCode) {
        return statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
                statusCode== HttpStatus.SC_MOVED_PERMANENTLY ||
                statusCode== HttpStatus.SC_SEE_OTHER ||
                statusCode == HttpStatus.SC_TEMPORARY_REDIRECT;
    }

    public NetworkTool setDoLog(boolean doLog) {
        this.doLog = doLog;
        return this;
    }

    private Map<String, String> getHeaders(String cookie) {
        Map<String, String> method = new HashMap<>();

        String c = (cookie != null) ? cookie : cookie_empty;
        method.put("Cookie", c);
        method.put("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
        method.put("Cache-Control", "max-age=0");
        method.put("Content-Type", "application/x-www-form-urlencoded");
//        method.put("Origin", "https://fr-fr.facebook.com");
//        method.put("Referer", "https://fr-fr.facebook.com/");
        method.put("Upgrade-Insecure-Requests", "1");
        method.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
//            method.setRequestHeader("Accept-Encoding", "gzip, deflate, br");
        method.put("Accept-Encoding", "html");
        return method;
    }

    public boolean addResponseHeaderCookie(ResponseHttp respHttp, Response resp) {
        boolean ret = false;
        Headers headers = resp.headers();
        int size = headers.size();
        String logTitle = "NetworkTool - addResponseHeaderCookie";
        logMe(logTitle + " ---------------->");
        logMe(logTitle + " size:" + size);
        for (int i=0 ; i<size ; i++) {
            ResponseElement head = new ResponseElement();
            head.name = headers.name(i);
            head.value = headers.value(i);
            if ("Set-Cookie".equals(head.name)) {
                buildResponeElementCookie(logTitle, respHttp, head.value);
                ret = true;
            }
        }
        logMe(logTitle + " ----------------< return:" + ret);
        return ret;
    }

    public boolean addResponseHeaderCookie(ResponseHttp respHttp, Request request) {
        boolean ret = false;
        Headers headers = request.headers();
        int size = headers.size();
        String logTitle = "NetworkTool - addResponseHeaderCookie from Request";
        logMe(logTitle + "---------------->");
        logMe(logTitle + " size:" + size);
        for (int i=0 ; i<size ; i++) {
            ResponseElement head = new ResponseElement();
            head.name = headers.name(i);
            head.value = headers.value(i);
            if ("Cookie".equals(head.name)) {
                String[] tab = head.value.split(";");
                for(String part : tab) {
                    buildResponeElementCookie(logTitle, respHttp, part);
                }
                ret = true;
            }
        }
        logMe(logTitle + "----------------< return:" + ret);
        return ret;
    }

    private boolean addResponseHeader(ResponseHttp respHttp, Response response) {
        boolean ret = false;
        Headers headers = response.headers();
        int size = headers.size();
        String logTitle = "NetworkTool - addResponseHeader from response";
        logMe(logTitle + " ---------------->");
        logMe(logTitle + " size:" + size);
        for (int i=0 ; i<size ; i++) {
            ResponseElement head = new ResponseElement();
            head.name = headers.name(i);
            head.value = headers.value(i);
            if ("Set-Cookie".equals(head.name)) {
                buildResponeElementCookie(logTitle, respHttp, head.value);
                ret = true;
            } else {
                respHttp.header.add(head);
                logMe(logTitle + " name:" + head.name + " value:" + head.value);
            }
        }
        logMe(logTitle + " ----------------< return:" + ret);
        return ret;
    }

    private void buildResponeElementCookie(String logTitle, ResponseHttp ret, String val) {
        ResponseElement element = null;
        String value = val.split(";", 2)[0].trim();
        if (value.indexOf("=")>=0) {
            String[] valTab = val.split("=", 2);
            boolean deleted = value.endsWith("=deleted");
            if (deleted) {
                ret.headerCookie.remove(valTab[0]);
                logMe(logTitle + " Cookie name:" + valTab[0] + " deleted");
            } else {
                element = new ResponseElement(valTab[0].trim(), valTab[1].trim());
            }
        } else {
            element = new ResponseElement(val.trim(), "");
        }
        if (element != null) {
            ret.headerCookie.add(element);
            logMe(logTitle + " Element name:" + element.name + " value:" + element.value);
        }
    }

    private void logMe(String message) {
        if (doLog) {
            System.out.println(message);
        }
    }

    private void logMe(Exception ex) {
        if (doLog) {
            ex.printStackTrace();
        }
    }

    private void showRequestHeaders(String title, Headers headers) {
        int size = headers.size();
        logMe("\r\nNetworkTool - "+title+"----------------------------- size:" + size);
        for (int i=0 ; i<size ; i++) {
            logMe("NetworkTool - " + headers.name(i) + ":" + headers.value(i));
        }
        logMe("-------------------------------------------------------------\r\n");
    }
}
