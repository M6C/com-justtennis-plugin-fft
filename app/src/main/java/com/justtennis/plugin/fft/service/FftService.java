package com.justtennis.plugin.fft.service;

import android.support.annotation.NonNull;

import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.ProxiedHttpsConnection;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.fabric.sdk.android.services.network.HttpRequest;

public class FftService {

    private static final String proxy = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final String port = "8080";
    private static final String proxyUser = "pckh146";
    private static final String proxyPw = "k5F+n7S!";

    private static final String KEY_STATUS_302_FOUND = "HTTP/1.0 302 Found";
    private static final String KEY_LOCATION = "Location";
    private static final String QUERY_FORM_USER_LOGIN = "form.user-login";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[name$=op]";
    private static final String QUERY_LOGIN = "input[name$=name]";
    private static final String QUERY_PSWD = "input[name$=pass]";

    private FftService() {
    }

    public static void connect(String login, String password) throws IOException {

        String[] sURL = new String[] {
//                "https://www.google.com",
//                "https://myaccount.google.com",
                "https://mon-espace-tennis.fft.fr"
        };

        for (String u : sURL) {
            u = formatUrl(u);
            System.out.println("\r\n" + u);

            URL url = new URL(u);
            while (true) {
                ProxiedResponse a = readConnection(url);

                String newUrl = null;
                if (a.header.containsKey(KEY_STATUS_302_FOUND)) {
                    if (a.header.containsKey(KEY_LOCATION)) {
                        newUrl = a.header.get(KEY_LOCATION);
                        System.out.println("\r\n==============> new Url:" + newUrl);
                    }
                } else {
                    System.out.println("==============> connection Return:\r\n" + a.content);

                    FormResponse form = parseContent(a.content);
                    form.login.value = login;
                    form.password.value = password;
                }
                if (newUrl == null) {
                    break;
                } else {
                    url = new URL(newUrl);
                }
            }
        }
    }

    public static void submitForm(String login, String password) throws IOException {
        String url1 = "https://mon-espace-tennis.fft.fr";
        System.out.println("\r\n" + url1);

        URL url = new URL(url1);
        ProxiedResponse ret1 = readConnection(url);
        FormResponse form = parseContent(ret1.content);
        form.login.value = login;
        form.password.value = password;
        System.out.println("==============> connection Return:\r\n" + ret1.content);

        String url2 = url1 + form.action;
        System.out.println("");
        System.out.println("==============> Form Action:" + url2);

        url = new URL(url2);
        Map<String, String> data = new HashMap<>();
        data.put(form.button.name, form.button.value);
        data.put(form.login.name, form.login.value);
        data.put(form.password.name, form.password.value);
        data.putAll(form.input);

        HttpMethod method = HttpPostProxy.post(url1, form.action, data);
//        System.out.println("==============> Status Code: " + method.getStatusCode());
//        System.out.println("==============> Response: " + method.getResponseBodyAsString());

        url2 = url1 + "/bloc_home/redirect/classement";

        //        ProxiedResponse ret2 = readConnection(url, "POST", data);
//        System.out.println("==============> connection Return:\r\n" + ret2.content);

//        doSubmit(url1, form);

//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setUseCaches (true);
//        conn.setDoOutput(true);
//        conn.setDoInput(true);
//
//        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//
//        Set keys = data.keySet();
//        Iterator keyIter = keys.iterator();
//        String content = "";
//        for(int i=0; keyIter.hasNext(); i++) {
//            Object key = keyIter.next();
//            if(i!=0) {
//                content += "&";
//            }
//            content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
//        }
//        System.out.println(content);
//        out.writeBytes(content);
//        out.flush();
//        out.close();
//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line = "";
//        while((line=in.readLine())!=null) {
//            System.out.println(line);
//        }
//        in.close();
    }

    public static void doSubmit(String url, FormResponse form) throws IOException {
        String encoded = HttpRequest.Base64.encode(proxyUser + ":" + proxyPw)
                .replace("\r\n", "");
        String proxyHeader = "Proxy-Authorization: Basic " + encoded;
        byte[] byteHeader = proxyHeader.getBytes("ASCII7");
//        Socket socket = null;
//        try {
//            socket = new Socket();
////        socket.setSoTimeout(getReadTimeout());
//            socket.connect(new InetSocketAddress(proxy, Integer.parseInt(port)), 0);
//            socket.getOutputStream().write(byteHeader);
//            socket.getOutputStream().flush();
//        }
//        finally {
//            if (socket != null) {
//                socket.close();
//            }
//        }

        String u = url + form.action;
        System.out.println("\r\n" + u);
        URL siteUrl = new URL(u);
        Map<String, String> data = new HashMap<>();
        data.put(form.button.name, form.button.value);
        data.put(form.login.name, form.login.value);
        data.put(form.password.name, form.password.value);
        data.putAll(form.input);

        HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setUseCaches (true);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        OutputStream outProxy = conn.getOutputStream();
        outProxy.write(byteHeader);
        outProxy.write("\r\n".getBytes());
        outProxy.flush();

        DataOutputStream out = new DataOutputStream(conn.getOutputStream());

        Set keys = data.keySet();
        Iterator keyIter = keys.iterator();
        StringBuilder content = new StringBuilder();
        for(int i=0; keyIter.hasNext(); i++) {
            Object key = keyIter.next();
            if(i!=0) {
                content.append("&");
            }
            content.append(key).append("=").append(URLEncoder.encode(data.get(key), "UTF-8"));
        }
        System.out.println(content);
        out.writeBytes(content.toString());
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        while((line=in.readLine())!=null) {
            System.out.println(line);
        }
        in.close();
    }

    private static FormResponse parseContent(String content) {
        FormResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(QUERY_FORM_USER_LOGIN);
            if (forms != null && !forms.isEmpty()) {
                ret = new FormResponse();
                Element form = forms.first();
                ret.action = form.attr("action");
                System.out.println("==============> form action:" + ret.action);

                Elements inputs = form.select(QUERY_INPUT_TYPE_HIDDEN);
                if (inputs != null && !inputs.isEmpty()) {
                    for(int i=0 ; i<inputs.size() ; i++) {
                        Element input = inputs.get(i);
                        String name = input.attr("name");
                        String value = input.attr("value");
                        ret.input.put(name, value);
                        System.out.println("==============> form input -" + name + ":" + value);
                    }
                } else {
                    System.err.println("\r\n==============> form input '"+QUERY_INPUT_TYPE_HIDDEN+"' not found");
                }

                ret.button = parseElement(form, QUERY_BUTTON);
                ret.login = parseElement(form, QUERY_LOGIN);
                ret.password = parseElement(form, QUERY_PSWD);
            } else {
                System.err.println("\r\n==============> form '"+QUERY_FORM_USER_LOGIN+"' not found");
            }
        }
        return ret;
    }

    private static FormResponse.Element parseElement(Element form, String query) {
        FormResponse.Element ret = new FormResponse.Element();
        Elements buttons = form.select(query);
        if (buttons != null && !buttons.isEmpty()) {
            Element button = buttons.first();
            ret.name = button.attr("name");
            ret.value = button.attr("value");
            System.out.println("==============> form button name:" + ret.name + " value:" + ret.value);
        } else {
            System.err.println("\r\n==============> form button '"+QUERY_BUTTON+"' not found");
        }
        return ret;
    }

    private static String formatUrl(String u) {
        return (u.endsWith("/")) ? u : u + "/";
    }

    private static HttpURLConnection openConnection(URL url) throws IOException {
        return new ProxiedHttpsConnection(url, proxy, Integer.parseInt(port), proxyUser, proxyPw);
    }

    @NonNull
    private static ProxiedResponse readConnection(URL url) throws IOException {
        return readConnection(url, null, null);
    }

    @NonNull
    private static ProxiedResponse readConnection(URL url, String method, Map<String, String> data) throws IOException {
        ProxiedResponse response = new ProxiedResponse();
        ProxiedHttpsConnection httpCon = (ProxiedHttpsConnection) openConnection(url);
        if (method != null) {
            httpCon.setRequestMethod(method);
        }
        if (data != null) {
            httpCon.addData(data);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            boolean header = true;
            while ((inputLine = in.readLine()) != null) {
                header = header && !inputLine.trim().isEmpty();
                if (header) {
                    int start = inputLine.indexOf(":");
                    String key = "", value = "";
                    if (start > 0) {
                        key = inputLine.substring(0, start);
                        value = inputLine.substring(start + 1);
                    } else {
                        key = inputLine;
                    }
                    System.out.println("==============> header -" + key + ":" + value);
                    response.header.put(key, value);
                } else {
                    a.append(inputLine);
                }
            }
            response.content = a.toString();
            in.close();
            return response;
        } finally {
            if (in != null) {
                in.close();
            }
            httpCon.disconnect();
        }
    }

    private static class ProxiedResponse {
        Map<String, String> header = new HashMap<>();
        String content;
    }

    private static class FormResponse {
        String action;
        Map<String, String> input = new HashMap<>();
        Element login = new Element();
        Element password = new Element();
        Element button = new Element();

        private static class Element {
            String name;
            String value;
        }
    }
}
