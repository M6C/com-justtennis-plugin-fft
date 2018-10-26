package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindPlayerFormRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;
import com.justtennis.plugin.shared.query.request.LoginFormRequest;

import org.jsoup.nodes.Element;

public class FormParser extends AbstractFormParser {

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
}
