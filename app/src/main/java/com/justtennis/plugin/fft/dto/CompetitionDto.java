package com.justtennis.plugin.fft.dto;

import java.io.Serializable;

public class CompetitionDto implements Serializable {
    public String type;
    public String club;
    public String league;
    public String name;
    public String dateStart;
    public String dateEnd;
    public String linkTournament;

    public CompetitionDto(String type, String club, String league, String name, String dateStart, String dateEnd, String linkTournament) {
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
        return "CompetitionDto{" +
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
