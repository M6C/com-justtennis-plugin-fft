package com.justtennis.plugin.mytd.query.request;

import org.cameleon.android.generic.query.request.GenericRequest;

import java.util.Arrays;

public class Mp3YouTDwnRequest extends GenericRequest {

    public Mp3YouTDwnRequest() {
        // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
        divQuery.addAll(Arrays.asList("div[id$=download]","div[class$=videolist]","div[class$=videoInfo]"));
        dataQuery.put("", new Query(Query.TYPE.LINK, "h3 > a"));
    }
}
