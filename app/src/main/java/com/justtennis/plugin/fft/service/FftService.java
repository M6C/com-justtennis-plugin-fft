package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.model.ResponseHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public static void submitForm(String login, String password) throws IOException {
        String root = "https://mon-espace-tennis.fft.fr";
        System.out.println("\r\n" + root);

        URL url = new URL(root);
//        ProxiedResponse ret1 = readConnection(url);
        ResponseHttp respRoot = HttpGetProxy.get(root, "");

        FormResponse form = parseContent(respRoot.body);
        form.login.value = login;
        form.password.value = password;
        System.out.println("==============> connection Return:\r\n" + respRoot.body);

        System.out.println("");
        System.out.println("==============> Form Action:" + form.action);

        Map<String, String> data = new HashMap<>();
        data.put(form.button.name, form.button.value);
        data.put(form.login.name, form.login.value);
        data.put(form.password.name, form.password.value);
        data.putAll(form.input);

        ResponseHttp resPost = HttpPostProxy.post(root, form.action, data);

        if (resPost.pathRedirect != null && !resPost.pathRedirect.isEmpty()) {
            ResponseHttp resGetRedirect = HttpGetProxy.get(root, resPost.pathRedirect, resPost);

            ResponseHttp resClassement = HttpGetProxy.get(root, "/bloc_home/redirect/classement", resPost);
        }
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
