package com.justtennis.plugin.mytd.query.request;

import com.justtennis.plugin.generic.query.request.GenericFormRequest;

public class Mp3YouTDwnFormRequest extends GenericFormRequest {

    // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
    private static final String QUERY_FORM = "form[id$=searchForm]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = null;
    private static final String QUERY_BUTTON = "button[name$=submitForm][type$=submit]";
    private static final String QUERY_SEARCH = "input[name$=videoURL][type$=text]";

    public static final String KEY_FIELD_SEARCH = "FIELD_SEARCH";

    public Mp3YouTDwnFormRequest() {
        super(QUERY_FORM, QUERY_INPUT_TYPE_HIDDEN, QUERY_BUTTON);
        fieldQuery.put(KEY_FIELD_SEARCH, Mp3YouTDwnFormRequest.QUERY_SEARCH);
    }
}
