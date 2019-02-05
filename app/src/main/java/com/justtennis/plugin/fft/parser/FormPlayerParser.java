package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import org.cameleon.android.shared.parser.AbstractFormParser;
import org.cameleon.android.shared.query.request.AbstractFormRequest;
import org.cameleon.android.shared.query.response.AbstractFormResponse;

import org.jsoup.nodes.Element;

public class FormPlayerParser extends AbstractFormParser {

    private FormPlayerParser() {}

    private static FormPlayerParser instance;

    public static FormPlayerParser getInstance() {
        if (instance == null) {
            instance = new FormPlayerParser();
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
