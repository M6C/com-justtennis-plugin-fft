package com.justtennis.plugin.shared.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractParser {

    protected static String getText(Element e, String selectText) {
        return e.select(selectText).text();
    }

    protected static String getLink(Element e, String selectLink) {
        Elements linkPalmares = e.select(selectLink);
        if (linkPalmares != null && !linkPalmares.isEmpty()) {
            return linkPalmares.attr("href");
        }
        return null;
    }
}
