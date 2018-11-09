package com.justtennis.plugin.shared.parser;

import android.support.annotation.NonNull;

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

    @NonNull
    protected static String activateHtml(String content) {
        // Uncomment htlm
        return content
                .replaceAll("<!-- <", "<")
                .replaceAll("> -->", ">");
    }
}
