package com.justtennis.plugin.converter;

import com.justtennis.plugin.fft.query.response.LoginFormResponse;

import java.util.HashMap;
import java.util.Map;

public class LoginFormResponseConverter {

    private LoginFormResponseConverter() {}

    public static Map<String, String> toDataMap(LoginFormResponse form) {
        Map<String, String> data = new HashMap<>();
        data.put(form.button.name, form.button.value);
        data.put(form.login.name, form.login.value);
        data.put(form.password.name, form.password.value);
        data.putAll(form.input);
        return data;
    }
}
