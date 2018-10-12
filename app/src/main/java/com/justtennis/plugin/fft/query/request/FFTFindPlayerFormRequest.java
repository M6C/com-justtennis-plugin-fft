package com.justtennis.plugin.fft.query.request;

public class FFTFindPlayerFormRequest extends AbstractFormRequest {

    private static final String QUERY_FORM = "form[id$=-actency-recherche-joueur-form]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[name$=op]";
    private static final String QUERY_GENRE = "select[name$=sexe]";
    private static final String QUERY_FIRSTNAME = "input[name$=prenom]";
    private static final String QUERY_LASTNAME = "input[name$=nom]";

    public String genreQuery;
    public String firstnameQuery;
    public String lastnameQuery;

    public FFTFindPlayerFormRequest() {
        super(QUERY_FORM, QUERY_INPUT_TYPE_HIDDEN, QUERY_BUTTON);
        this.genreQuery = QUERY_GENRE;
        this.firstnameQuery = QUERY_FIRSTNAME;
        this.lastnameQuery = QUERY_LASTNAME;
    }
}
