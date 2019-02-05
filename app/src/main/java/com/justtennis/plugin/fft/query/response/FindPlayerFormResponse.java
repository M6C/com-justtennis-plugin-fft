package com.justtennis.plugin.fft.query.response;

import org.cameleon.android.shared.query.response.AbstractFormResponse;
import org.cameleon.android.shared.query.response.FormElement;

public class FindPlayerFormResponse extends AbstractFormResponse {
    public FormElement genre = new FormElement();
    public FormElement firstname = new FormElement();
    public FormElement lastname = new FormElement();
}