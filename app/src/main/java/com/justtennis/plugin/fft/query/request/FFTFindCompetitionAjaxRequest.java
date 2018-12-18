package com.justtennis.plugin.fft.query.request;

public class FFTFindCompetitionAjaxRequest extends FFTFindCompetitionRequest {

    private static final String QUERY_DIV_MAIN = "body > div[id$=carousel-tournoi]";
    private static final String QUERY_TABLE_BODY_CONTENT = "div[class$=carousel-inner] > div";
    private static final String QUERY_TD_TYPE = "td:eq(0)";
    private static final String QUERY_TD_CLUB = "div[class$=tournoi-nom-club]";
    private static final String QUERY_TD_LEAGUE = "div[class$=tournoi-nom-ligue]";
    private static final String QUERY_TD_NAME = "div[class$=title-tournoi]";
    private static final String QUERY_TD_DATE_START = "div[class$=tournoi-date-debut]";
    private static final String QUERY_TD_DATE_END = "div[class$=tournoi-date-fin]";

    private static final String QUERY_LINK_TOURNAMENT = "div[class$=title-tournoi] > a";

    public FFTFindCompetitionAjaxRequest() {
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
