package com.justtennis.plugin.fft.query.request;

public class PalmaresRequest {

    private static final String QUERY_MENU_NAV_CONTENT = "nav.mmenu-nav[id$=mmenu_right] > ul  > ul > li.mmenu-block-wrap > span > ul.mmenu-mm-list-level-1";
    private static final String QUERY_LINK_PALMARES = "a.menu-link-Palmarès";

    public final String menuNavContent;
    public final String linkPalmares;

    public PalmaresRequest() {
        this.menuNavContent = QUERY_MENU_NAV_CONTENT;
        this.linkPalmares = QUERY_LINK_PALMARES;
    }
}
