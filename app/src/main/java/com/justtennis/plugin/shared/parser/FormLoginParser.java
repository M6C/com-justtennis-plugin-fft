package com.justtennis.plugin.shared.parser;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.request.LoginFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;

import org.jsoup.nodes.Element;

public class FormLoginParser extends AbstractFormParser {

    private static FormLoginParser instance;

    public static FormLoginParser getInstance() {
        if (instance == null) {
            instance = new FormLoginParser();
        }
        return instance;
    }

    @Override
    protected void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form) {
        LoginFormResponse ret = (LoginFormResponse)response;
        LoginFormRequest req = (LoginFormRequest)request;
        ret.login = parseElement(form, req.loginQuery);
        ret.password = parseElement(form, req.passwordQuery);
    }

    public LoginFormResponse parseForm(String content, LoginFormRequest request) {
        return (LoginFormResponse) parseForm(content, request, new LoginFormResponse());
    }
}
