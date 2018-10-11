package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTMillessimeMatchRequest;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MillesimeMatchParser extends AbstractParser {

    private MillesimeMatchParser() {}

    public static MillesimeMatchResponse parseMillesimeMatch(String content, FFTMillessimeMatchRequest request) {
        MillesimeMatchResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = doc.select(request.divMain);
            if (div != null && !div.isEmpty()) {
                ret = new MillesimeMatchResponse();
                Elements tbodys = div.first().select(request.tableBodyContent);
                if (tbodys != null && !tbodys.isEmpty()) {
                    for (Element e : tbodys) {
                        MillesimeMatchResponse.MatchItem item = new MillesimeMatchResponse.MatchItem();
                        item.name = getText(e, request.name);
                        item.year = getText(e, request.year);
                        item.ranking = getText(e, request.ranking);
                        item.vicDef = getText(e, request.vicDef);
                        item.score = getText(e, request.score);
                        item.wo = getText(e, request.wo);
                        item.tournament = getText(e, request.tournament);
                        item.type = getText(e, request.type);
                        item.date = getText(e, request.date);


                        item.linkTournoi = getLink(e, request.linkTournoi);
                        item.linkPalmares = getLink(e, request.linkPalmares);

                        ret.matchList.add(item);
                        System.out.println("\r\n==============> Match:" + item);
                    }
                } else {
                    System.err.println("\r\n==============> table body '"+request.tableBodyContent +"' not found");
                }
            } else {
                System.err.println("\r\n==============> div '"+request.divMain +"' not found");
            }
        }
        return ret;
    }
}
