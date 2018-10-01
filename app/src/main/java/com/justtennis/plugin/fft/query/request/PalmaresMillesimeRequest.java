package com.justtennis.plugin.fft.query.request;

public class PalmaresMillesimeRequest {

    private static final String QUERY_FORM_MILLESIME = "form[id$=actency-page-palmares-tennis-filters-form]";
    private static final String QUERY_SELECT_MILLESIME = "select[id$=edit-millesime]";
    private static final String QUERY_OPTION = "option";
    private static final String QUERY_OPTION_SELECTED = "option[selected$=selected]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";

    public final String formQuery;
    public final String selectMillesime;
    public final String option;
    public final String optionSelected;
    public final String hiddenQuery;

    public PalmaresMillesimeRequest() {
        this.formQuery = QUERY_FORM_MILLESIME;
        this.selectMillesime = QUERY_SELECT_MILLESIME;
        this.option = QUERY_OPTION;
        this.optionSelected = QUERY_OPTION_SELECTED;
        this.hiddenQuery = QUERY_INPUT_TYPE_HIDDEN;
    }
}
