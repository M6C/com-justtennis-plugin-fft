package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindPlayerRequest;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;
import org.cameleon.android.shared.parser.AbstractParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindPlayerParser extends AbstractParser {

    private FindPlayerParser() {}

    public static FindPlayerResponse parseFindPlayer(String content, FFTFindPlayerRequest request) {
        FindPlayerResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = doc.select(request.divMain);
            if (div != null && !div.isEmpty()) {
                ret = new FindPlayerResponse();
                Elements tbodys = div.first().select(request.tableBodyContent);
                if (tbodys != null && !tbodys.isEmpty()) {
                    for (Element e : tbodys) {
                        FindPlayerResponse.PlayerItem item = new FindPlayerResponse.PlayerItem();
                        item.civility = getText(e, request.civility);
                        item.lastname = getText(e, request.lastname);
                        item.firstname = getText(e, request.firstname);
                        item.year = getText(e, request.year);
                        item.ranking = getText(e, request.ranking);
                        item.bestRanking = getText(e, request.bestRanking);
                        item.licence = getText(e, request.licence);
                        item.club = getText(e, request.club);

                        item.linkRanking = getLink(e, request.linkRanking);
                        item.linkClubFindPlayer = getLink(e, request.linkClubFindPlayer);
                        item.linkClub = getLink(e, request.linkClub);
                        item.linkPalmares = getLink(e, request.linkPalmares);

                        ret.playerList.add(item);
                        logItem("Player", item);
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
