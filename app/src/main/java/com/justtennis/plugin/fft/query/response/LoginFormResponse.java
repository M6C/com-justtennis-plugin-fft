package com.justtennis.plugin.fft.query.response;

import java.util.HashMap;
import java.util.Map;

public class LoginFormResponse {
    public String action;
    public Map<String, String> input = new HashMap<>();
    public FormElement login = new FormElement();
    public FormElement password = new FormElement();
    public FormElement button = new FormElement();
}