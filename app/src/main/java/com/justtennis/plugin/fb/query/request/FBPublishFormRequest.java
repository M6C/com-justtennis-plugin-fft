package com.justtennis.plugin.fb.query.request;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;

public class FBPublishFormRequest extends AbstractFormRequest {

    // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
//    private static final String QUERY_FORM_UPDATE_STATUS = "div[id$=pagelet_composer]";// > div  > div[role$=dialog]  > div  > div  > div[id$=feedx_sprouts_container]  > div > form";
    private static final String QUERY_FORM_UPDATE_STATUS = "form[action^=/ajax/updatestatus.php]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[type$=submit]";
    private static final String QUERY_MESSAGE = "textarea[name$=xhpc_message]";

    public final String messageQuery;

    public FBPublishFormRequest() {
        this.formQuery = QUERY_FORM_UPDATE_STATUS;
        this.hiddenQuery = QUERY_INPUT_TYPE_HIDDEN;
        this.submitQuery = QUERY_BUTTON;
        this.messageQuery = QUERY_MESSAGE;
    }
}
