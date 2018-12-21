package com.justtennis.plugin.fft.converter;

import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;

import java.util.HashMap;
import java.util.Map;

public class FindCompetitionFormResponseConverter {

    private FindCompetitionFormResponseConverter() {}

    public static Map<String, String> toDataMap(FindCompetitionFormResponse form) {
        Map<String, String> data = new HashMap<>();
        data.put(form.type.name, form.type.value);
        data.put(form.city.name, form.city.value);
        data.put(form.dateStart.name, form.dateStart.value);
        data.put(form.dateEnd.name, form.dateEnd.value);
        data.putAll(form.input);
        return data;
    }
}
