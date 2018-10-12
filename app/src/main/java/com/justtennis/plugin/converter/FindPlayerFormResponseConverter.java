package com.justtennis.plugin.converter;

import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;

import java.util.HashMap;
import java.util.Map;

public class FindPlayerFormResponseConverter {

    private FindPlayerFormResponseConverter() {}

    public static Map<String, String> toDataMap(FindPlayerFormResponse form) {
        Map<String, String> data = new HashMap<>();
        data.put(form.firstname.name, form.firstname.value);
        data.put(form.lastname.name, form.lastname.value);
        data.put(form.genre.name, form.genre.value);
        data.putAll(form.input);
        return data;
    }
}
