package com.justtennis.plugin.fft.model;

public class FFTLoginFormRequest extends LoginFormRequest {

    private static final String QUERY_FORM_USER_LOGIN = "form.user-login";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[name$=op]";
    private static final String QUERY_LOGIN = "input[name$=name]";
    private static final String QUERY_PSWD = "input[name$=pass]";

    public FFTLoginFormRequest() {
        this.formQuery = QUERY_FORM_USER_LOGIN;
        this.hiddenQuery = QUERY_INPUT_TYPE_HIDDEN;
        this.submitQuery = QUERY_BUTTON;
        this.loginQuery = QUERY_LOGIN;
        this.passwordQuery = QUERY_PSWD;
    }
}
