package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.AbstractFormRequest;
import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.request.LoginFormRequest;
import com.justtennis.plugin.fft.query.response.AbstractFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FormElement;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FormParser {

    private FormParser() {}

    public static LoginFormResponse parseFormLogin(String content, LoginFormRequest request) {
        LoginFormResponse ret = new LoginFormResponse();
        Element form = parseForm(content, request, ret);
        if (form != null) {
            ret.login = parseElement(form, request.loginQuery);
            ret.password = parseElement(form, request.passwordQuery);
            return ret;
        } else {
            return null;
        }
    }

    public static FindPlayerFormResponse parseFormFindPlayer(String content, FFTFindPlayerFormRequest request) {
        FindPlayerFormResponse ret = new FindPlayerFormResponse();
        Element form = parseForm(content, request, ret);
        if (form != null) {
            ret.genre = parseElement(form, request.genreQuery);
            ret.firstname = parseElement(form, request.firstnameQuery);
            ret.lastname = parseElement(form, request.lastnameQuery);
        }
        return ret;
    }

    public static Element parseForm(String content, AbstractFormRequest request, AbstractFormResponse response) {
        Element ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(request.formQuery);
            if (forms != null && !forms.isEmpty()) {
                ret = forms.first();
                response.action = ret.attr("action");
                System.out.println("==============> ret action:" + response.action);

                Elements inputs = ret.select(request.hiddenQuery);
                if (inputs != null && !inputs.isEmpty()) {
                    for(int i=0 ; i<inputs.size() ; i++) {
                        Element input = inputs.get(i);
                        String name = input.attr("name");
                        String value = input.attr("value");
                        response.input.put(name, value);
                        System.out.println("==============> ret hidden -" + name + ":" + value);
                    }
                } else {
                    System.err.println("\r\n==============> ret hidden '"+request.hiddenQuery+"' not found");
                }

                response.button = parseElement(ret, request.submitQuery);
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
