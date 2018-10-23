package com.justtennis.plugin.shared.query.response;

import java.util.HashMap;
import java.util.Map;

public class AbstractFormResponse {
    public String action;
    public Map<String, String> input = new HashMap<>();
    public FormElement button = new FormElement();
}