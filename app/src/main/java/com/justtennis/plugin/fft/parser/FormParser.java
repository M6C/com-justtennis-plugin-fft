package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;
import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;

import org.jsoup.nodes.Element;

public class FormParser extends AbstractFormParser {

    private FormParser() {}

    private static FormParser instance;

    public static FormParser getInstance() {
        if (instance == null) {
            instance = new FormParser();
        }
        return instance;
    }

    @Override
    protected void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form) {
        FindPlayerFormResponse ret = (FindPlayerFormResponse)response;
        FFTFindPlayerFormRequest req = (FFTFindPlayerFormRequest)request;
        ret.genre = parseElement(form, req.genreQuery);
        ret.firstname = parseElement(form, req.firstnameQuery);
        ret.lastname = parseElement(form, req.lastnameQuery);
    }

    public FindPlayerFormResponse parseForm(String content, FFTFindPlayerFormRequest request) {
        return (FindPlayerFormResponse) parseForm(content, request, new FindPlayerFormResponse());
    }
}
