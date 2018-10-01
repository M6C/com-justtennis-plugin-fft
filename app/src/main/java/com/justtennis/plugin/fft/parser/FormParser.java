package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.LoginFormRequest;
import com.justtennis.plugin.fft.query.response.FormElement;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FormParser {

    private FormParser() {}

    public static LoginFormResponse parseFormLogin(String content, LoginFormRequest request) {
        LoginFormResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(request.formQuery);
            if (forms != null && !forms.isEmpty()) {
                ret = new LoginFormResponse();
                Element form = forms.first();
                ret.action = form.attr("action");
                System.out.println("==============> form action:" + ret.action);

                Elements inputs = form.select(request.hiddenQuery);
                if (inputs != null && !inputs.isEmpty()) {
                    for(int i=0 ; i<inputs.size() ; i++) {
                        Element input = inputs.get(i);
                        String name = input.attr("name");
                        String value = input.attr("value");
                        ret.input.put(name, value);
                        System.out.println("==============> form hidden -" + name + ":" + value);
                    }
                } else {
                    System.err.println("\r\n==============> form hidden '"+request.hiddenQuery+"' not found");
                }

                ret.button = parseElement(form, request.submitQuery);
                ret.login = parseElement(form, request.loginQuery);
                ret.password = parseElement(form, request.passwordQuery);
            } else {
                System.err.println("\r\n==============> form '"+request.formQuery+"' not found");
            }
        }
        return ret;
    }

    private static FormElement parseElement(Element form, String query) {
        FormElement ret = new FormElement();
        Elements buttons = form.select(query);
        if (buttons != null && !buttons.isEmpty()) {
            Element button = buttons.first();
            ret.name = button.attr("name");
            ret.value = button.attr("value");
            System.out.println("==============> form button name:" + ret.name + " value:" + ret.value);
        } else {
            System.err.println("\r\n==============> form element '"+query+"' not found");
        }
        return ret;
    }
}
