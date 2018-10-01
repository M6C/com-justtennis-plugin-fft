package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTRankingListRequest;
import com.justtennis.plugin.fft.query.request.FFTRankingMatchRequest;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RankingParser {

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
                        item.year = e.select(request.year).text();
                        item.ranking = e.select(request.ranking).text();
                        item.date = e.select(request.date).text();
                        item.origin = e.select(request.origin).text();
                        item.id = e.select(request.id).text();
                        ret.rankingList.add(item);
                        System.err.println("\r\n==============> ranking " + item);
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
                        item.name = e.select(request.name).text();
                        item.year = e.select(request.year).text();
                        item.ranking = e.select(request.ranking).text();
                        item.vicDef = e.select(request.vicDef).text();
                        item.wo = e.select(request.wo).text();
                        item.coef = e.select(request.coef).text();
                        item.points = e.select(request.points).text();
                        item.tournament = e.select(request.tournament).text();
                        item.type = e.select(request.type).text();
                        item.date = e.select(request.date).text();

                        ret.rankingList.add(item);
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
