package com.justtennis.plugin.fft.query.request;

public class FFTRankingListRequest {

    private static final String QUERY_DIV_MAIN = "div.tableau-annee";
    private static final String QUERY_TABLE_BODY_CONTENT = "table.dynatable > tbody > tr";
    private static final String QUERY_TD_YEAR = "td:eq(0)";
    private static final String QUERY_TD_RANKING = "td:eq(1)";
    private static final String QUERY_TD_DATE = "td:eq(2)";
    private static final String QUERY_TD_ORIGIN = "td:eq(3)";
    private static final String QUERY_TD_ID = "td:eq(4)";

    public String divRanking;
    public String tableBodyContent;
    public String year;
    public String ranking;
    public String date;
    public String origin;
    public String id;

    public FFTRankingListRequest() {
        this.divRanking = QUERY_DIV_MAIN;
        this.tableBodyContent = QUERY_TABLE_BODY_CONTENT;
        this.year = QUERY_TD_YEAR;
        this.ranking = QUERY_TD_RANKING;
        this.date = QUERY_TD_DATE;
        this.origin = QUERY_TD_ORIGIN;
        this.id = QUERY_TD_ID;
    }
}
