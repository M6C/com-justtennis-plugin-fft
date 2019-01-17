package com.justtennis.plugin.yout.query.request;

import com.justtennis.plugin.generic.query.request.GenericFormRequest;

public class YouTFindVideoFormRequest extends GenericFormRequest {

    // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
    private static final String QUERY_FORM = "ytd-masthead[id$=masthead]";//"form[id$=search-form]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = null;//"input[type$=hidden]";
    private static final String QUERY_BUTTON = null;//"button[id$=search-icon-legacy]";
    private static final String QUERY_SEARCH = "input[type$=text][id$=search]";

    public static final String KEY_FIELD_SEARCH = "FIELD_SEARCH";

    public YouTFindVideoFormRequest() {
        super(QUERY_FORM, QUERY_INPUT_TYPE_HIDDEN, QUERY_BUTTON);
        fieldQuery.put(KEY_FIELD_SEARCH, YouTFindVideoFormRequest.QUERY_SEARCH);
    }
}
