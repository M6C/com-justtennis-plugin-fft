package com.justtennis.plugin.fb.query.response;

import org.cameleon.android.shared.query.response.AbstractFormResponse;
import org.cameleon.android.shared.query.response.FormElement;

public class FBPublishFormResponse extends AbstractFormResponse {

    public FormElement message = new FormElement();
    public FormElement audience = new FormElement();

    public String publishId;
    public String publishTitle;
}