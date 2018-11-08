package com.justtennis.plugin.fb.parser;

import android.support.annotation.NonNull;

import com.justtennis.plugin.fb.query.request.FBPublishFormRequest;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;

import org.jsoup.nodes.Element;

public class FBPublishParser extends AbstractFormParser {

    private FBPublishParser() {}

    public static FBPublishFormResponse parseForm(String content, FBPublishFormRequest request) {
        FBPublishFormResponse ret = new FBPublishFormResponse();
        Element form = parseForm(activateForm(content), request, ret);
        if (form != null) {
            ret.message = parseElement(form, request.messageQuery);
            ret.audience = parseElement(form, request.audienceQuery, null, request.audienceAttrQuery);
            return ret;
        } else {
            return null;
        }
    }

    @NonNull
    private static String activateForm(String content) {
        // Uncomment form
        return content
                .replaceAll("<!-- <", "")
                .replaceAll("> -->", "");
    }
}
