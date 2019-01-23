package com.justtennis.plugin.eytm.query.request;

import com.justtennis.plugin.generic.query.request.GenericRequest;

import java.util.Arrays;

public class EasyYouTMp3Request extends GenericRequest {

    public EasyYouTMp3Request() {
        // https://jsoup.org/apidocs/org/jsoup/select/Selector.html
        divQuery.addAll(Arrays.asList("div[id$=Download]"));
        dataQuery.put("", new Query(Query.TYPE.LINK, "div[class*=btn-group] > a[class*=btn-success]"));
    }
}
