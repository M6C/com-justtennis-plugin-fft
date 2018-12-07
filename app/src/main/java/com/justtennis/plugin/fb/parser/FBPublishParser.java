package com.justtennis.plugin.fb.parser;

import com.justtennis.plugin.fb.query.request.FBPublishFormRequest;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.parser.AbstractFormParser;
import com.justtennis.plugin.shared.query.request.AbstractFormRequest;
import com.justtennis.plugin.shared.query.response.AbstractFormResponse;

import org.jsoup.nodes.Element;

public class FBPublishParser extends AbstractFormParser {

    private static FBPublishParser instance;

    private FBPublishParser() {}

    public static FBPublishParser getInstance() {
        if (instance == null) {
            instance = new FBPublishParser();
        }
        return instance;
    }

    @Override
    protected void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form) {
        FBPublishFormResponse ret = (FBPublishFormResponse)response;
        FBPublishFormRequest req = (FBPublishFormRequest)request;
        ret.message = parseElement(form, req.messageQuery);
        ret.audience = parseElement(form, req.audienceQuery, null, req.audienceAttrQuery);
    }

    public FBPublishFormResponse parseForm(String content, FBPublishFormRequest request) {
        FBPublishFormResponse ret = new FBPublishFormResponse();
        parseForm(activateHtml(content), request, ret);
        return ret;
    }
}
