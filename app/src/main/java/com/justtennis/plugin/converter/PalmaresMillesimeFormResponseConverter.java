package com.justtennis.plugin.converter;

import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;

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
