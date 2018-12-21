package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTRankingListRequest;
import com.justtennis.plugin.fft.query.request.FFTRankingMatchRequest;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.shared.parser.AbstractParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RankingParser extends AbstractParser {

    private RankingParser() {}

    public static RankingListResponse parseRankingList(String content, FFTRankingListRequest request) {
        RankingListResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = doc.select(request.divRanking);
            if (div != null && !div.isEmpty()) {
                ret = new RankingListResponse();
                Elements tbodys = div.first().select(request.tableBodyContent);
                if (tbodys != null && !tbodys.isEmpty()) {
                    for (Element e : tbodys) {
                        RankingListResponse.RankingItem item = new RankingListResponse.RankingItem();
                        item.year = getText(e, request.year);
                        item.ranking = getText(e, request.ranking);
                        item.date = getText(e, request.date);
                        item.origin = getText(e, request.origin);
                        item.id = getText(e, request.id);
                        ret.rankingList.add(item);
                        logItem("Ranking", item);
                    }
                } else {
                    System.err.println("\r\n==============> table body '"+request.tableBodyContent +"' not found");
                }
            } else {
                System.err.println("\r\n==============> div '"+request.divRanking +"' not found");
            }
        }
        return ret;
    }

    public static RankingMatchResponse parseRankingMatch(String content, FFTRankingMatchRequest request) {
        RankingMatchResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = doc.select(request.divMain);
            if (div != null && !div.isEmpty()) {
                ret = new RankingMatchResponse();
                Elements tbodys = div.first().select(request.tableBodyContent);
                if (tbodys != null && !tbodys.isEmpty()) {
                    for (Element e : tbodys) {
                        RankingMatchResponse.RankingItem item = new RankingMatchResponse.RankingItem();
                        item.name = getText(e, request.name);
                        item.year = getText(e, request.year);
                        item.ranking = getText(e, request.ranking);
                        item.vicDef = getText(e, request.vicDef);
                        item.wo = getText(e, request.wo);
                        item.coef = getText(e, request.coef);
                        item.points = getText(e, request.points);
                        item.tournament = getText(e, request.tournament);
                        item.type = getText(e, request.type);
                        item.date = getText(e, request.date);

                        ret.rankingList.add(item);
                        logItem("Ranking", item);
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
