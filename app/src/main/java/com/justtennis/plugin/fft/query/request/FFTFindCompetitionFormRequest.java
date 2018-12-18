package com.justtennis.plugin.fft.query.request;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;

public class FFTFindCompetitionFormRequest extends AbstractFormRequest {

    // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
    private static final String QUERY_FORM = "form[id$=actency-maps-tournoi-form]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[name$=op]";
    private static final String QUERY_TYPE = "input[type$=radio][name*=competition]";
    private static final String QUERY_CITY = "input[name$=geocoding]";
    private static final String QUERY_DATE_START = "input[type$=text][name*=start_date]";
    private static final String QUERY_DATE_END = "input[type$=text][name*=end_date]";

    public String typeQuery;
    public String cityQuery;
    public String dateStartQuery;
    public String dateEndQuery;

    public FFTFindCompetitionFormRequest() {
        super(QUERY_FORM, QUERY_INPUT_TYPE_HIDDEN, QUERY_BUTTON);
        this.typeQuery = QUERY_TYPE;
        this.cityQuery = QUERY_CITY;
        this.dateStartQuery = QUERY_DATE_START;
        this.dateEndQuery = QUERY_DATE_END;
    }
}
