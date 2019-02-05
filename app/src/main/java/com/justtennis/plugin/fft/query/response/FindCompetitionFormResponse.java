package com.justtennis.plugin.fft.query.response;

import org.cameleon.android.shared.query.response.AbstractFormResponse;
import org.cameleon.android.shared.query.response.FormElement;

public class FindCompetitionFormResponse extends AbstractFormResponse {
    public FormElement type = new FormElement();
    public FormElement city = new FormElement();
    public FormElement dateStart = new FormElement();
    public FormElement dateEnd = new FormElement();
}