package com.justtennis.plugin.shared.parser;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractParser {

    private static boolean doLogItem = false;

    protected static String getText(Element e, String selectText) {
        int iAttr = selectText.indexOf('#');
        if (iAttr > 0) {
            Elements select = e.select(selectText.substring(0, iAttr));
            return (select == null) ? null : select.attr(selectText.substring(iAttr+1));
        }
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
        if (content.isEmpty()) {
            return content;
        }
        // Uncomment htlm
        return content
                .replaceAll("<!--\\s*<", "<")
                .replaceAll(">\\s*-->", ">");
    }

    protected static void logItem(String title, Object item) {
        if (doLogItem) {
            System.out.println("\r\n==============> "+title+":" + item);
        }
    }
}
