package com.justtennis.plugin.shared.network.tool;

import android.support.annotation.NonNull;

import com.justtennis.plugin.shared.network.model.ResponseElement;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkTool {

    private static NetworkTool instance;
    private boolean doLog = false;
    public static final String cookie_empty = "reg_ext_ref=cookie_empty";

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
            if ("Set-Cookie".equalsIgnoreCase(name)) {
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
        try {
            if (response.body() != null) {
                String contentType = response.header("Content-Type");
                if ("application/octet-stream".equals(contentType) || "audio/mpeg3".equals(contentType)) {
                    ret.raw = response.body().byteStream();
                } else{
                    ret.body = response.body().string();
                    response.body().close();
                }
            }
        } catch (IOException e) {
            logMe(e);
        }
        return ret;
    }

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
        method.put("Upgrade-Insecure-Requests", "1");
        method.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
//            method.put("Accept-Encoding", "gzip, deflate, br");
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
            if ("Set-Cookie".equalsIgnoreCase(head.name)) {
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
            if ("Cookie".equalsIgnoreCase(head.name) && !head.value.contains(cookie_empty)) {
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
            if ("Set-Cookie".equalsIgnoreCase(head.name)) {
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
        if (value.indexOf("=")>=0 && !value.contains(cookie_empty)) {
            String[] valTab = value.split("=", 2);
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
