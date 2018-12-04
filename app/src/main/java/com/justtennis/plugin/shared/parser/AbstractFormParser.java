package com.justtennis.plugin.shared.parser;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.request.LoginFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractFormParser extends AbstractParser {

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

    protected static Element parseForm(String content, AbstractFormRequest request, AbstractFormResponse response) {
        Element ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(request.formQuery);
            if (forms != null && !forms.isEmpty()) {
                ret = forms.first();
                response.action = ret.attr("action");
                System.out.println("===========================>");
                System.out.println("==============> Parsing Form");
                System.out.println("==============> ret action:" + response.action);

                if (request.hiddenQuery != null && !request.hiddenQuery.isEmpty()) {
                    Elements inputs = ret.select(request.hiddenQuery);
                    if (inputs != null && !inputs.isEmpty()) {
                        for (int i = 0; i < inputs.size(); i++) {
                            Element input = inputs.get(i);
                            String name = input.attr("name");
                            String value = input.attr("value");
                            response.input.put(name, value);
                            System.out.println("==============> ret hidden -" + name + ":" + value);
                        }
                    } else {
                        System.err.println("\r\n==============> ret hidden '" + request.hiddenQuery + "' not found");
                    }
                } else {
                    System.err.println("\r\n==============> ret hidden is empty.");
                }

                response.button = parseElement(ret, request.submitQuery);
                System.out.println("===========================<");
            } else {
                System.err.println("\r\n==============> form '"+request.formQuery+"' not found");
            }
        }
        return ret;
    }

    protected static FormElement parseElement(Element form, String query) {
        return parseElement(form, query, "name", "value");
    }

    protected static FormElement parseElement(Element form, String query, String keyAttrName, String keyAttrValue) {
        FormElement ret = new FormElement();
        Elements buttons = form.select(query);
        if (buttons != null && !buttons.isEmpty()) {
            Element button = buttons.first();
            if (keyAttrName != null) {
                ret.name = button.attr(keyAttrName);
            }
            if (keyAttrValue != null) {
                ret.value = button.attr(keyAttrValue);
            }
            System.out.println("==============> form element name:" + ret.name + " value:" + ret.value);
        } else {
            System.err.println("\r\n==============> form element '"+query+"' not found");
        }
        return ret;
    }
}
