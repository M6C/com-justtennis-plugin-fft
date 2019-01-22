package com.justtennis.plugin.fb.parser;

import com.justtennis.plugin.fb.query.request.FBProfilPublicationRequest;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.shared.parser.AbstractParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FBProfilParser extends AbstractParser {

    private FBProfilParser() {}

    public static FBProfilPublicationResponse parsePublication(String content, FBProfilPublicationRequest request) {
        FBProfilPublicationResponse ret = null;
        Document doc = Jsoup.parse(activateHtml(content));
        if (doc != null) {
            Elements div = doc.select(request.divMain);
            if (div != null && !div.isEmpty()) {
                ret = new FBProfilPublicationResponse();
                boolean mainFind = false;
                for (Element d : div) {
                    Elements timeline = d.select(request.timeline);
                    if (timeline != null && !timeline.isEmpty()) {
                        mainFind = true;
                        for (Element e : timeline) {
                            FBProfilPublicationResponse.TimeLineItem item = new FBProfilPublicationResponse.TimeLineItem();
                            item.date = getText(e, request.timelineItemDate);
                            item.text = getText(e, request.timelineItemMessage);

                            ret.timeLineList.add(item);
                            System.out.println("==============> TimeLine item:" + item);
                        }
                        break;
                    }
                }
                if (!mainFind) {
                    System.err.println("\r\n==============> Time Line '"+request.timeline +"' not found");
                }

            } else {
                System.err.println("\r\n==============> divMain '"+request.divMain +"' not found");
            }
        }
        return ret;
    }
}
