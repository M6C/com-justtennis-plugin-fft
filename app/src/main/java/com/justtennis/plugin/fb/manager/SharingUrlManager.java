package com.justtennis.plugin.fb.manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharingUrlManager {

    private static final String[] regex = new String[]{
            "(https?://)(\\S+)\\.(S*)(:\\d*)?(\\/)?(\\S*)"
    };
    private static SharingUrlManager instance;
    private boolean log = false;

    private SharingUrlManager() {
    }

    public static SharingUrlManager getInstance() {
        if (instance == null) {
            instance = new SharingUrlManager();
        }
        return instance;
    }

    public SharingUrlManager log(boolean log) {
        this.log = log;
        return instance;
    }

    public boolean check(String url) {
        boolean ret = false;
        for(int i=0 ; !ret && i < regex.length ; i++) {
            ret = parseRegex(url, regex[i]);
        }

        return ret;
    }

    private boolean parseRegex(String url, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        boolean ret = matcher.find();
        if (log) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
        }
        return ret;
    }
}
