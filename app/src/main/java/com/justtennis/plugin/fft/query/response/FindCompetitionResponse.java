package com.justtennis.plugin.fft.query.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindCompetitionResponse {

    public List<CompetitionItem> competitionList = new ArrayList<>();

    public static class CompetitionItem implements Serializable {
        public String type;
        public String club;
        public String league;
        public String name;
        public String dateStart;
        public String dateEnd;
        public String linkTournament;

        public CompetitionItem() {
        }

        public CompetitionItem(String type, String club, String league, String name, String dateStart, String dateEnd, String linkTournament) {
            this.type = type;
            this.club = club;
            this.league = league;
            this.name = name;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.linkTournament = linkTournament;
        }

        @Override
        public String toString() {
            return "CompetitionItem{" +
                    "type='" + type + '\'' +
                    ", club='" + club + '\'' +
                    ", league='" + league + '\'' +
                    ", name='" + name + '\'' +
                    ", dateStart='" + dateStart + '\'' +
                    ", dateEnd='" + dateEnd + '\'' +
                    ", linkTournament='" + linkTournament + '\'' +
                    '}';
        }
    }
}