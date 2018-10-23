package com.justtennis.plugin.fb.parser;

import com.justtennis.plugin.fb.query.request.FBHomePageRequest;
import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.shared.parser.AbstractParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FBHomePageParser extends AbstractParser {

    private FBHomePageParser() {}

    public static FBHomePageResponse parseHomePage(String content, FBHomePageRequest request) {
        FBHomePageResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = doc.select(request.divMain);
            if (div != null && !div.isEmpty()) {
                ret = new FBHomePageResponse();
                Element d = div.first();
                ret.name = getText(d, request.name);
                ret.linkProfil = getLink(d, request.linkProfil);
            } else {
                System.err.println("\r\n==============> divMain '"+request.divMain +"' not found");
            }
        }
        return ret;
    }
}
