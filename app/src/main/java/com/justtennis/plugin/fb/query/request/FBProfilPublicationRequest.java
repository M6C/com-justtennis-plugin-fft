package com.justtennis.plugin.fb.query.request;

public class FBProfilPublicationRequest {

    private static final String QUERY_DIV_MAIN = "div[class$=hidden_elem]";

    private static final String QUERY_TIMELINE = "code > div > div[data-ft*=mf_story_key]";

    private static final String QUERY_TIMELINE_ITEM_MESSAGE = "div[data-ad-preview$=message]";

    public final String divMain;
    public final String timeline;
    public final String timelineItemMessage;

    public FBProfilPublicationRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.timeline = QUERY_TIMELINE;
        this.timelineItemMessage = QUERY_TIMELINE_ITEM_MESSAGE;
    }
}
