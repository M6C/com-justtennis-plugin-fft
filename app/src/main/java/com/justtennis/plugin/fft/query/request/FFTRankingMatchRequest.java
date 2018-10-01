package com.justtennis.plugin.fft.query.request;

public class FFTRankingMatchRequest {

    private static final String QUERY_DIV_MAIN = "div.table_adversaire";
    private static final String QUERY_TABLE_BODY_CONTENT = "table > tbody > tr";
    private static final String QUERY_TD_NAME = "td:eq(0)";
    private static final String QUERY_TD_YEAR = "td:eq(1)";
    private static final String QUERY_TD_RANKING = "td:eq(2)";
    private static final String QUERY_TD_VIC_DEF = "td:eq(3) > span";
    private static final String QUERY_TD_WO = "td:eq(4)";
    private static final String QUERY_TD_COEF = "td:eq(5)";
    private static final String QUERY_TD_POINTS = "td:eq(6)";
    private static final String QUERY_TD_TOURNAMENT = "td:eq(7)";
    private static final String QUERY_TD_TYPE = "td:eq(8)";
    private static final String QUERY_TD_DATE = "td:eq(9)";

    public String divMain;
    public String tableBodyContent;
    public String name;
    public String year;
    public String ranking;
    public String vicDef;
    public String wo;
    public String coef;
    public String points;
    public String tournament;
    public String type;
    public String date;

    public FFTRankingMatchRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.tableBodyContent = QUERY_TABLE_BODY_CONTENT;
        this.name = QUERY_TD_NAME;
        this.year = QUERY_TD_YEAR;
        this.ranking = QUERY_TD_RANKING;
        this.vicDef = QUERY_TD_VIC_DEF;
        this.wo = QUERY_TD_WO;
        this.coef = QUERY_TD_COEF;
        this.points = QUERY_TD_POINTS;
        this.tournament = QUERY_TD_TOURNAMENT;
        this.type = QUERY_TD_TYPE;
        this.date = QUERY_TD_DATE;
    }
}
