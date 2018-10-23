package com.justtennis.plugin.fft.query.response;

import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

public class FindPlayerFormResponse extends AbstractFormResponse {
    public FormElement genre = new FormElement();
    public FormElement firstname = new FormElement();
    public FormElement lastname = new FormElement();
}