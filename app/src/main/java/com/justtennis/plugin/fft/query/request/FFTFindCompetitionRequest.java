package com.justtennis.plugin.fft.query.request;

public class FFTFindCompetitionRequest {

    private static final String QUERY_DIV_MAIN = "table[id$=tournoi-list-table]";
    private static final String QUERY_TABLE_BODY_CONTENT = "tbody > tr";
    private static final String QUERY_TD_TYPE = "td:eq(0)";
    private static final String QUERY_TD_CLUB = "td:eq(2)";
    private static final String QUERY_TD_LEAGUE = "td:eq(3)";
    private static final String QUERY_TD_NAME = "td:eq(4)";
    private static final String QUERY_TD_DATE_START = "td:eq(5)";
    private static final String QUERY_TD_DATE_END = "td:eq(7)";

    private static final String QUERY_LINK_TOURNAMENT = "td:eq(4) > a";

    public String divMain;
    public String tableBodyContent;
    public String type;
    public String club;
    public String league;
    public String name;
    public String dateStart;
    public String dateEnd;
    public String linkTournament;

    public FFTFindCompetitionRequest() {
        this.divMain = QUERY_DIV_MAIN;
        this.tableBodyContent = QUERY_TABLE_BODY_CONTENT;
        this.type = QUERY_TD_TYPE;
        this.club = QUERY_TD_CLUB;
        this.league = QUERY_TD_LEAGUE;
        this.name = QUERY_TD_NAME;
        this.dateStart = QUERY_TD_DATE_START;
        this.dateEnd = QUERY_TD_DATE_END;
        this.linkTournament = QUERY_LINK_TOURNAMENT;
    }
}
