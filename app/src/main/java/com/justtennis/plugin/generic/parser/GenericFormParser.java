package com.justtennis.plugin.generic.parser;

import com.justtennis.plugin.generic.query.request.GenericFormRequest;
import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;
import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

import org.jsoup.nodes.Element;

import java.util.Map;

public class GenericFormParser extends AbstractFormParser {

    private GenericFormParser() {}

    private static GenericFormParser instance;

    public static GenericFormParser getInstance() {
        if (instance == null) {
            instance = new GenericFormParser();
        }
        return instance;
    }

    @Override
    protected void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form) {
        GenericFormResponse ret = (GenericFormResponse)response;
        GenericFormRequest req = (GenericFormRequest)request;
        Map<String, String> fieldQuery = ((GenericFormRequest) request).fieldQuery;
        for(String key : fieldQuery.keySet()) {
            ret.fieldValue.put(key, parseElement(form, req.fieldQuery.get(key)));
        }
    }

    public GenericFormResponse parseForm(String content, GenericFormRequest request) {
        return (GenericFormResponse) parseForm(content, request, new GenericFormResponse());
    }

    public void showResponse(GenericFormResponse response) {
        Map<String, FormElement> fieldValue = response.fieldValue;
        for(String key : fieldValue.keySet()) {
            FormElement val = fieldValue.get(key);
            System.out.println("==============> new form element key:" + key + "  name:" + val.name + " value:" + val.value);
        }
    }
}
