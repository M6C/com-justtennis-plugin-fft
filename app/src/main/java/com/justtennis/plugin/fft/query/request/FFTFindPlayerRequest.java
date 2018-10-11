package com.justtennis.plugin.fft.query.request;

public class FFTFindPlayerRequest {

    private static final String QUERY_DIV_MAIN = "div[id$=recherche-joueur-result-content]";
    private static final String QUERY_TABLE_BODY_CONTENT = "table > tbody > tr";
    private static final String QUERY_TD_CIVILITY = "td:eq(0)";
    private static final String QUERY_TD_LASTNAME = "td:eq(1)";
    private static final String QUERY_TD_FIRSTNAME = "td:eq(2)";
    private static final String QUERY_TD_YEAR = "td:eq(3)";
    private static final String QUERY_TD_RANKING = "td:eq(4)";
    private static final String QUERY_TD_BEST_RANKING = "td:eq(5)";
    private static final String QUERY_TD_LICENCE = "td:eq(6)";
    private static final String QUERY_TD_CLUB = "td:eq(7) > a:eq(1)";

    private static final String QUERY_LINK_RANKING = "td:eq(4) > a";
    private static final String QUERY_LINK_CLUB_FIND_PLAYER = "td:eq(7) > a:eq(0)";
    private static final String QUERY_LINK_CLUB = "td:eq(7) > a:eq(1)";
    private static final String QUERY_LINK_PALMARES = "td:eq(8) > a";

    public String divMain;
    public String tableBodyContent;
    public String civility;
    public String lastname;
    public String firstname;
    public String year;
    public String ranking;
    public String bestRanking;
    public String licence;
    public String club;
    public String linkRanking;
    public String linkClubFindPlayer;
    public String linkClub;
    public String linkPalmares;

    public FFTFindPlayerRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.tableBodyContent = QUERY_TABLE_BODY_CONTENT;
        this.civility = QUERY_TD_CIVILITY;
        this.lastname = QUERY_TD_LASTNAME;
        this.firstname = QUERY_TD_FIRSTNAME;
        this.year = QUERY_TD_YEAR;
        this.ranking = QUERY_TD_RANKING;
        this.bestRanking = QUERY_TD_BEST_RANKING;
        this.licence = QUERY_TD_LICENCE;
        this.club = QUERY_TD_CLUB;
        this.linkRanking = QUERY_LINK_RANKING;
        this.linkClubFindPlayer = QUERY_LINK_CLUB_FIND_PLAYER;
        this.linkClub = QUERY_LINK_CLUB;
        this.linkPalmares = QUERY_LINK_PALMARES;
    }
}
