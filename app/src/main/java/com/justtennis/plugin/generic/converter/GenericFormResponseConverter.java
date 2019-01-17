package com.justtennis.plugin.generic.converter;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.query.response.FormElement;

import java.util.HashMap;
import java.util.Map;

public class GenericFormResponseConverter {

    private GenericFormResponseConverter() {}

    public static Map<String, String> toDataMap(GenericFormResponse form) {
        Map<String, String> data = new HashMap<>();
        Map<String, FormElement> fieldValue = form.fieldValue;

        for(String key : fieldValue.keySet()) {
            FormElement val = fieldValue.get(key);
            data.put(val.name, val.value);
        }
        data.putAll(form.input);
        return data;
    }
}
