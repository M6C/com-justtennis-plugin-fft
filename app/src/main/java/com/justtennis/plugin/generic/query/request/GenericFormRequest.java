package com.justtennis.plugin.generic.query.request;

import com.justtennis.plugin.shared.query.request.AbstractFormRequest;

import java.util.HashMap;
import java.util.Map;

public class GenericFormRequest extends AbstractFormRequest {

    public Map<String, String> fieldQuery = new HashMap<>();

    public GenericFormRequest(String formQuery, String hiddenQuery, String submitQuery) {
        super(formQuery, hiddenQuery, submitQuery);
    }
}
