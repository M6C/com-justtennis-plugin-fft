package com.justtennis.plugin.generic.parser;

import com.justtennis.plugin.generic.query.request.GenericRequest;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.parser.AbstractParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

public class GenericParser extends AbstractParser {

    private GenericParser() {}

    private static GenericParser instance;

    public static GenericParser getInstance() {
        if (instance == null) {
            instance = new GenericParser();
        }
        return instance;
    }

    public static GenericResponse parseFindWithActivateHtml(String content, GenericRequest request) {
        return parseFind(activateHtml(content), request);
    }

    public static GenericResponse parseFind(String content, GenericRequest request) {
        GenericResponse ret = null;
        Document doc = Jsoup.parse(content);
        if (doc != null) {
            Elements div = null;
            for(String divQ : request.divQuery) {
                div = (div == null) ? doc.select(divQ) : div.select(divQ);
                if (div == null || div.isEmpty()) {
                    System.err.println("\r\n==============> '" + divQ + "' not found");
                    div = null;
                    break;
                }
            }
            if (div != null) {
                ret = new GenericResponse();
                for (Element e : div) {
                    Map<String, GenericRequest.Query> dataQuery = request.dataQuery;
                    for(String key : dataQuery.keySet()) {
                        GenericRequest.Query v = dataQuery.get(key);
                        GenericResponse.Item item = new GenericResponse.Item();
                        switch (v.type) {
                            case LINK:
                                item.itemValue.put(key, getLink(e, v.path));
                                break;
                            case TEXT:
                            default:
                                item.itemValue.put(key, getText(e, v.path));
                        }

                        ret.data.add(item);
                        logItem("GenerigParser parseFind", item);
                    }
                }
            }
        }
        return ret;
    }
}
