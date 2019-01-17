package com.justtennis.plugin.generic.query.response;

import com.justtennis.plugin.shared.query.response.AbstractFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

import java.util.HashMap;
import java.util.Map;

public class GenericFormResponse extends AbstractFormResponse {
    public Map<String, FormElement> fieldValue = new HashMap<>();
}