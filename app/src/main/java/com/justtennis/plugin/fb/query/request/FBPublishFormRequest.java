package com.justtennis.plugin.fb.query.request;

import org.cameleon.android.shared.query.request.AbstractFormRequest;

public class FBPublishFormRequest extends AbstractFormRequest {

    // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
    private static final String QUERY_FORM_UPDATE_STATUS = "form[action^=/ajax/updatestatus.php]";
    private static final String QUERY_INPUT_TYPE_HIDDEN = "input[type$=hidden]";
    private static final String QUERY_BUTTON = "button[type$=submit]";
    private static final String QUERY_MESSAGE = "textarea[name$=xhpc_message]";
//    private static final String QUERY_AUDIENCE = "div[class*=composerAudienceWrapper] a[role=button] span:eq(1)";
    private static final String QUERY_AUDIENCE = "div[class*=composerAudienceWrapper] a[role=button]";
    private static final String QUERY_AUDIENCE_ATTR = "data-tooltip-content";

    public final String messageQuery;
    public final String audienceQuery;
    public final String audienceAttrQuery;

    public FBPublishFormRequest() {
        this.formQuery = QUERY_FORM_UPDATE_STATUS;
        this.hiddenQuery = QUERY_INPUT_TYPE_HIDDEN;
        this.submitQuery = QUERY_BUTTON;
        this.messageQuery = QUERY_MESSAGE;
        this.audienceQuery = QUERY_AUDIENCE;
        this.audienceAttrQuery = QUERY_AUDIENCE_ATTR;
    }
}
