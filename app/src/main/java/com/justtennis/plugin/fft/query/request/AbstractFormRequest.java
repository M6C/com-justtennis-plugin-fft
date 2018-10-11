package com.justtennis.plugin.fft.query.request;

public class AbstractFormRequest {

    public String formQuery;
    public String hiddenQuery;
    public String submitQuery;

    public AbstractFormRequest() {}

    public AbstractFormRequest(String formQuery, String hiddenQuery, String submitQuery) {
        this.formQuery = formQuery;
        this.hiddenQuery = hiddenQuery;
        this.submitQuery = submitQuery;
    }
}
