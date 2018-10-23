package com.justtennis.plugin.fb.query.request;

public class FBHomePageRequest {

    private static final String QUERY_DIV_MAIN = "div[id$=pagelet_bluebar] > div > div > div[role$=banner]";

    private static final String QUERY_TD_NAME = "div[role$=navigation] > div > div > div > a[title$=Profil] > span > span";
    private static final String QUERY_LINK_PROFILE = "div[role$=navigation] > div > div > div > a[title$=Profil]";

    public String divMain;
    public String name;
    public String linkProfil;

    public FBHomePageRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.name = QUERY_TD_NAME;
        this.linkProfil = QUERY_LINK_PROFILE;
    }
}
