package com.justtennis.plugin.fft.query.response;

import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

public class LoginFormResponse extends AbstractFormResponse {
    public FormElement login = new FormElement();
    public FormElement password = new FormElement();
}