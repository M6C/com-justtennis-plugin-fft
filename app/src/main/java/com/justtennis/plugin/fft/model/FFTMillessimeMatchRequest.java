package com.justtennis.plugin.fft.model;

public class FFTMillessimeMatchRequest {

    private static final String QUERY_DIV_MAIN = "div.content_tabs > div.table-wrapper";
    private static final String QUERY_TABLE_BODY_CONTENT = "table > tbody > tr";
    private static final String QUERY_TD_NAME = "td:eq(0)";
    private static final String QUERY_TD_YEAR = "td:eq(1)";
    private static final String QUERY_TD_RANKING = "td:eq(2)";
    private static final String QUERY_TD_VIC_DEF = "td:eq(3) > span";
    private static final String QUERY_TD_SCORE = "td:eq(4)";
    private static final String QUERY_TD_WO = "td:eq(5)";
    private static final String QUERY_TD_TOURNAMENT = "td:eq(5)";
    private static final String QUERY_TD_TYPE = "td:eq(6)";
    private static final String QUERY_TD_DATE = "td:eq(7)";

    private static final String QUERY_LINK_PALMARES = "td:eq(0) > a";
    private static final String QUERY_LINK_TOURNOI = "td:eq(5) > a";

    public String divMain;
    public String tableBodyContent;
    public String name;
    public String year;
    public String ranking;
    public String vicDef;
    public String score;
    public String wo;
    public String tournament;
    public String type;
    public String date;
    public String linkPalmares;
    public String linkTournoi;

    public FFTMillessimeMatchRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.tableBodyContent = QUERY_TABLE_BODY_CONTENT;
        this.name = QUERY_TD_NAME;
        this.year = QUERY_TD_YEAR;
        this.ranking = QUERY_TD_RANKING;
        this.vicDef = QUERY_TD_VIC_DEF;
        this.score = QUERY_TD_SCORE;
        this.wo = QUERY_TD_WO;
        this.tournament = QUERY_TD_TOURNAMENT;
        this.type = QUERY_TD_TYPE;
        this.date = QUERY_TD_DATE;
        this.linkPalmares = QUERY_LINK_PALMARES;
        this.linkTournoi = QUERY_LINK_TOURNOI;
    }
}
