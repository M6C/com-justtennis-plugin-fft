package com.justtennis.plugin.fft.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

abstract class AbstractParser {

    static String getText(Element e, String selectText) {
        return e.select(selectText).text();
    }

    static String getLink(Element e, String selectLink) {
        Elements linkPalmares = e.select(selectLink);
        if (linkPalmares != null && !linkPalmares.isEmpty()) {
            return linkPalmares.attr("href");
        }
        return null;
    }
}
