package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.PalmaresMillesimeRequest;
import com.justtennis.plugin.fft.query.request.PalmaresRequest;
import com.justtennis.plugin.fft.query.response.FormElement;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.query.response.PalmaresResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PalmaresParser {

    private PalmaresParser() {}

    public static PalmaresResponse parsePalmares(String content, PalmaresRequest request) {
        PalmaresResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements menus = doc.select(request.menuNavContent);
            if (menus != null && !menus.isEmpty()) {
                ret = new PalmaresResponse();
                Element menu = menus.first();
                Elements inputs = menu.select(request.linkPalmares);
                if (inputs != null && !inputs.isEmpty()) {
                    Element input = inputs.get(0);
                    ret.action = input.attr("href");
                    System.out.println("==============> menu href:" + ret.action);
                } else {
                    System.err.println("\r\n==============> menu palmares '"+request.linkPalmares+"' not found");
                }
            } else {
                System.err.println("\r\n==============> menu '"+request.menuNavContent+"' not found");
            }
        }
        return ret;
    }

    public static PalmaresMillesimeResponse parsePalmaresMillesime(String content, PalmaresMillesimeRequest request) {
        PalmaresMillesimeResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements forms = doc.select(request.formQuery);
            if (forms != null && !forms.isEmpty()) {
                ret = new PalmaresMillesimeResponse();
                Element form = forms.first();

                ret.action = form.attr("action");
                ret.method = form.attr("method");
                System.out.println("==============> Form Millesime action:" + ret.action);

                Elements selects = doc.select(request.selectMillesime);
                if (selects != null && !selects.isEmpty()) {
                    Element select = selects.first();
                    ret.select = new FormElement(select.attr("name"), null);
                    System.out.println("==============> Form Millesime select name:" + ret.select.name);
                    Elements options = select.select(request.option);
                    if (options != null && !options.isEmpty()) {
                        for (Element option : options) {
                            PalmaresMillesimeResponse.Millesime millesime = new PalmaresMillesimeResponse.Millesime(option.attr("value"), option.text());
                            ret.listMillesime.add(millesime);
                            System.out.println("==============> Form Millesime:" + millesime);
                        }
                    } else {
                        System.err.println("\r\n==============> Form Millesime option '"+request.option+"' not found");
                    }

                    Elements optionSelected = select.select(request.optionSelected);
                    if (optionSelected != null && !optionSelected.isEmpty()) {
                        Element option = optionSelected.get(0);
                        ret.millesimeSelected = new PalmaresMillesimeResponse.Millesime(option.attr("value"), option.text());
                        System.out.println("==============> Form Millesime selected:" + ret.millesimeSelected);
                    } else {
                        System.err.println("\r\n==============> Form Millesime selected '"+request.optionSelected+"' not found");
                    }
                } else {
                    System.err.println("\r\n==============> Form Millesime selects '"+request.selectMillesime+"' not found");
                }

                Elements inputs = form.select(request.hiddenQuery);
                if (inputs != null && !inputs.isEmpty()) {
                    for(int i=0 ; i<inputs.size() ; i++) {
                        Element input = inputs.get(i);
                        String name = input.attr("name");
                        String value = input.attr("value");
                        ret.input.put(name, value);
                        System.out.println("==============> Form Millesime hidden -" + name + ":" + value);
                    }
                } else {
                    System.err.println("\r\n==============> Form Millesime hidden '"+request.hiddenQuery+"' not found");
                }
            } else {
                System.err.println("\r\n==============> Form Millesime '"+request.formQuery+"' not found");
            }
        }
        return ret;
    }
}
