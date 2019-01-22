package com.justtennis.plugin.generic.query.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericRequest {

    public List<String> divQuery = new ArrayList<>();
    public Map<String, Query> dataQuery = new HashMap<>();

    public static class Query {
        public enum TYPE {TEXT, LINK}

        public TYPE type;
        public String path;

        public Query(TYPE type, String path) {
            this.type = type;
            this.path = path;
        }
    }
}
