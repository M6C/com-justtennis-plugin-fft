package com.justtennis.plugin.generic.query.response;

import com.justtennis.plugin.shared.query.response.AbstractFormResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericResponse extends AbstractFormResponse {

    public List<Item> data = new ArrayList<>();

    public static class Item {
        public Map<String, String> itemValue = new HashMap<>();
    }
}