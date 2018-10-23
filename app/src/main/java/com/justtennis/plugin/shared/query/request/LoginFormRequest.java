package com.justtennis.plugin.shared.query.request;

public class LoginFormRequest extends AbstractFormRequest {

    public String loginQuery;
    public String passwordQuery;

    public LoginFormRequest() {}

    public LoginFormRequest(String formQuery, String hiddenQuery, String submitQuery, String loginQuery, String passwordQuery) {
        super(formQuery, hiddenQuery, submitQuery);
        this.loginQuery = loginQuery;
        this.passwordQuery = passwordQuery;
    }
}