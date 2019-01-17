package com.justtennis.plugin.shared.parser;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractFormParser extends AbstractParser {

    protected abstract void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form);

    protected AbstractFormResponse parseForm(String content, AbstractFormRequest request, AbstractFormResponse response) {
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(request.formQuery);
            if (forms != null && !forms.isEmpty()) {
                Element ret = forms.first();
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

                if (request.submitQuery != null && !request.submitQuery.isEmpty()) {
                    response.button = parseElement(ret, request.submitQuery);
                }

                parseFormExtra(request, response, ret);
            } else {
                System.err.println("\r\n==============> form '"+request.formQuery+"' not found");
            }
        }
        System.out.println("===========================<");
        return response;
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
