package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindCompetitionAjaxRequest;
import com.justtennis.plugin.fft.query.request.FFTFindCompetitionRequest;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import org.cameleon.android.shared.parser.AbstractParser;
import org.cameleon.android.shared.tool.JsonUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FindCompetitionParser extends AbstractParser {

    private FindCompetitionParser() {}

    public static FindCompetitionResponse parseFindCompetition(String content, FFTFindCompetitionRequest request) {
        FindCompetitionResponse ret = null;
        Document doc = Jsoup.parse(content);
        int cnt = 0;
        if (doc != null) {
            Elements divs = doc.select(request.divMain);
            if (divs != null && !divs.isEmpty()) {
                ret = new FindCompetitionResponse();
                for (Element div : divs) {
                    Elements tbodys = div.select(request.tableBodyContent);
                    if (tbodys != null && !tbodys.isEmpty()) {
                        for (Element e : tbodys) {
                            FindCompetitionResponse.CompetitionItem item = new FindCompetitionResponse.CompetitionItem();
                            item.type = getText(e, request.type);
                            item.club = getText(e, request.club);
                            item.league = getText(e, request.league);
                            item.name = getText(e, request.name);
                            item.dateStart = getText(e, request.dateStart);
                            item.dateEnd = getText(e, request.dateEnd);
                            item.club = getText(e, request.club);

                            item.linkTournament = getLink(e, request.linkTournament);

                            ret.competitionList.add(item);
                            logItem("Competition", item);
                            cnt++;
                        }
                    } else {
                        System.err.println("\r\n==============> table body '" + request.tableBodyContent + "' not found");
                    }
                }
            } else {
                System.err.println("\r\n==============> div '"+request.divMain +"' not found");
            }
        }
        System.out.println("\r\n==============> parseFindCompetition Competition nb:" + cnt);
        return ret;
    }

    public static FindCompetitionResponse parseFindCompetition(String content, FFTFindCompetitionAjaxRequest request) {
        FindCompetitionResponse ret = null;
        String body = JsonUtil.getInstance().extratAttributValue(content, "custom_bubble");
        if (body.isEmpty()) {
            return ret;
        }
        String html = String.format("<html><head/><body>%s</body></html>", body);
        return parseFindCompetition(html, (FFTFindCompetitionRequest)request);
    }
}
