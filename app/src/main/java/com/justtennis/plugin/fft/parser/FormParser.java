package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;

import org.jsoup.nodes.Element;

public class FormParser extends AbstractFormParser {

    private FormParser() {}

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
}
