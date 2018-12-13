package com.justtennis.plugin.fb.manager;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharingYoutubeManager {

    private static final String[] regex = new String[]{
            "(https?://)(www\\.youtube\\.com)(:\\d*)?(/watch)(\\?|(.*)&)((v)(=)(\\S{11}))(&\\S*)?",
            "(https?://)(youtu\\.be)(:\\d*)?(/)?(\\S{11})(&\\S*)?"
    };
    private static SharingYoutubeManager instance;
    private boolean log = false;

    private SharingYoutubeManager() {
    }

    public static SharingYoutubeManager getInstance() {
        if (instance == null) {
            instance = new SharingYoutubeManager();
        }
        return instance;
    }

    public SharingYoutubeManager log(boolean log) {
        this.log = log;
        return instance;
    }

    public String getIdFromUrl(String url) {
        String ret = null;
        int[] groupIdx = new int[]{10, 5};
        for(int i=0 ; ret == null && i < regex.length ; i++) {
            ret = parseRegex(url, regex[i], groupIdx[i]);
        }

        return ret;
    }

    public String cleanUrl(@NonNull String url) {
        String ret = url;
        for (String aRegex : regex) {
            ret = ret.replaceAll(aRegex, "");
        }

        return ret;
    }

    private String parseRegex(String url, String regex, int groupIdx) {
        String ret = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find() && matcher.groupCount() >= groupIdx) {
            for (int i = 1; log && i <= matcher.groupCount(); i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
            ret = matcher.group(groupIdx);
        }
        return ret;
    }

    public Map<String, String> getData(String youtubeId, String title) {
        Map<String, String> data = new HashMap<>();
        data.put("attachment[params][urlInfo][canonical]", "https://www.youtube.com/watch?v=" + youtubeId);
        data.put("attachment[params][urlInfo][final]", "https://www.youtube.com/watch?v=" + youtubeId);
        data.put("attachment[params][urlInfo][user]", "https://www.youtube.com/watch?v=" + youtubeId);
//        data.put("attachment[params][favicon]", "https://www.youtube.com/yts/img/favicon_144-vfliLAfaB.png");
//        data.put("attachment[params][external_author_id]", "UCjgNwPMZbXJTZGTEA80r5KA");
        data.put("attachment[params][title]", title);
        data.put("attachment[params][ranked_images][images][0]", "https://i.ytimg.com/vi/" + youtubeId + "/hqdefault.jpg");
        data.put("attachment[params][ranked_images][ranking_model_version]", "11");
        data.put("attachment[params][ranked_images][specified_og]", "1");
//        data.put("attachment[params][medium]", "103");
        data.put("attachment[params][url]", "https://www.youtube.com/watch?v=" + youtubeId);
//        data.put("attachment[params][global_share_id]", "1862837243842213");
        data.put("attachment[params][video][0][type]", "text/html");
        data.put("attachment[params][video][0][secure_url]", "https://www.youtube.com/embed/" + youtubeId + "?autoplay=1");
        data.put("attachment[params][video][0][width]", "640");
        data.put("attachment[params][video][0][height]", "360");
        data.put("attachment[params][video][0][src]", "https://www.youtube.com/embed/" + youtubeId + "?autoplay=1");
        data.put("attachment[params][video][1][type]", "application/x-shockwave-flash");
        data.put("attachment[params][video][1][secure_url]", "https://www.youtube.com/v/" + youtubeId + "?version=3&amp;autohide=1&amp;autoplay=1");
        data.put("attachment[params][video][1][width]", "640");
        data.put("attachment[params][video][1][height]", "360");
        data.put("attachment[params][video][1][src]", "http://www.youtube.com/v/" + youtubeId + "?version=3&amp;autohide=1&amp;autoplay=1");
        data.put("attachment[params][amp_url]", "");
//        data.put("attachment[params][url_scrape_id]", "328046974686480");
//        data.put("attachment[params][hmac]", "AbcCRY3B64F8rMxK");
        data.put("attachment[params][locale]", "");
        data.put("attachment[params][external_img]", "{&quot;src&quot;:&quot;https:\\/\\/i.ytimg.com\\/vi\\/" + youtubeId + "\\/hqdefault.jpg&quot;,&quot;width&quot;:480,&quot;height&quot;:360}");
        data.put("attachment[type]", "100");
        data.put("attachment[params][images][0]", "https://i.ytimg.com/vi/" + youtubeId + "/hqdefault.jpg");
        return data;
    }
}
