package com.justtennis.plugin.fft.model;

public class LoginFormRequest {

    public String formQuery;
    public String hiddenQuery;
    public String submitQuery;
    public String loginQuery;
    public String passwordQuery;

    public LoginFormRequest() {}

    public LoginFormRequest(String formQuery, String hiddenQuery, String submitQuery, String loginQuery, String passwordQuery) {
        this.formQuery = formQuery;
        this.hiddenQuery = hiddenQuery;
        this.submitQuery = submitQuery;
        this.loginQuery = loginQuery;
        this.passwordQuery = passwordQuery;
    }
}