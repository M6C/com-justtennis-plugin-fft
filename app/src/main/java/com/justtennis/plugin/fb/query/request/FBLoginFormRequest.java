package com.justtennis.plugin.fb.query.request;

import com.justtennis.plugin.shared.query.request.LoginFormRequest;

public class FBLoginFormRequest extends LoginFormRequest {

    private static final String QUERY_FORM_USER_LOGIN = "form[id$=login_form]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "input[type$=submit]";
    private static final String QUERY_LOGIN = "input[type$=email]";
    private static final String QUERY_PSWD = "input[type$=password]";

    public FBLoginFormRequest() {
        this.formQuery = QUERY_FORM_USER_LOGIN;
        this.hiddenQuery = QUERY_INPUT_TYPE_HIDDEN;
        this.submitQuery = QUERY_BUTTON;
        this.loginQuery = QUERY_LOGIN;
        this.passwordQuery = QUERY_PSWD;
    }
}
