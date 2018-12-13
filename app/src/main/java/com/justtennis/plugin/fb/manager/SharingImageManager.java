package com.justtennis.plugin.fb.manager;

import java.util.HashMap;
import java.util.Map;

public class SharingImageManager {
    private static SharingImageManager instance;
    private boolean log = false;

    private SharingImageManager() {
    }

    public static SharingImageManager getInstance() {
        if (instance == null) {
            instance = new SharingImageManager();
        }
        return instance;
    }

    public SharingImageManager log(boolean log) {
        this.log = log;
        return instance;
    }

    public Map<String, String> getData(String url, String title) {
        Map<String, String> data = new HashMap<>();
        data.put("attachment[params][urlInfo][canonical]", url);
        data.put("attachment[params][urlInfo][final]", url);
        data.put("attachment[params][urlInfo][user]", url);
        data.put("attachment[params][locale]", "en_US");
//        data.put("attachment[params][favicon]", "https://www.google.fr/favicon.ico");
        data.put("attachment[params][title]", title);
        data.put("attachment[params][lang]", "en");
        data.put("attachment[params][medium]", "106");
        data.put("attachment[params][url]", url);
//        data.put("attachment[params][global_share_id]", "1976208419160595");
//        data.put("attachment[params][url_scrape_id]", "2181138308880613");
//        data.put("attachment[params][hmac]", "AbfdcLTiK-Q4faYg");
        data.put("attachment[params][external_img]","");
        data.put("attachment[type]", "100");
        return data;
    }
}
