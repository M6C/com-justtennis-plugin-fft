package com.justtennis.plugin.fb.converter;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;

import java.util.HashMap;
import java.util.Map;

public class PublishFormResponseConverter {

    private PublishFormResponseConverter() {}

    public static Map<String, String> toDataMap(FBPublishFormResponse form) {
        Map<String, String> data = new HashMap<>();
        data.put(form.message.name, form.message.value);
        data.putAll(form.input);
        return data;
    }
}
