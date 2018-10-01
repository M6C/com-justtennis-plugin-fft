package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTMillessimeMatchRequest;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MillesimeMatchParser {

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
                        item.name = e.select(request.name).text();
                        item.year = e.select(request.year).text();
                        item.ranking = e.select(request.ranking).text();
                        item.vicDef = e.select(request.vicDef).text();
                        item.score = e.select(request.score).text();
                        item.wo = e.select(request.wo).text();
                        item.tournament = e.select(request.tournament).text();
                        item.type = e.select(request.type).text();
                        item.date = e.select(request.date).text();

                        Elements linkPalmares = e.select(request.linkPalmares);
                        if (linkPalmares != null && !linkPalmares.isEmpty()) {
                            item.linkPalmares = linkPalmares.attr("href");
                        }

                        Elements linkTournoi = e.select(request.linkTournoi);
                        if (linkTournoi != null && !linkTournoi.isEmpty()) {
                            item.linkTournoi = linkTournoi.attr("href");
                        }

                        ret.matchList.add(item);
                        System.err.println("\r\n==============> ranking " + item);
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
