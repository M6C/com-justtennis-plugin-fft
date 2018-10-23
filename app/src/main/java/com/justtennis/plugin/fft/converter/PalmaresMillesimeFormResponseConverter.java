package com.justtennis.plugin.fft.converter;

import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;

import java.util.HashMap;
import java.util.Map;

public class PalmaresMillesimeFormResponseConverter {

    private PalmaresMillesimeFormResponseConverter() {}

    public static Map<String, String> toDataMap(PalmaresMillesimeResponse form) {
        Map<String, String> data = new HashMap<>();
        data.put(form.select.name, form.select.value);
        data.putAll(form.input);
        return data;
    }
}
