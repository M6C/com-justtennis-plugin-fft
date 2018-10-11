package com.justtennis.plugin.fft.dto;

import java.io.Serializable;

public class PlayerDto implements Serializable {
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

    public PlayerDto(String civility, String lastname, String firstname, String year, String ranking, String bestRanking, String licence, String club, String linkRanking, String linkClubFindPlayer, String linkClub, String linkPalmares) {
        this.civility = civility;
        this.lastname = lastname;
        this.firstname = firstname;
        this.year = year;
        this.ranking = ranking;
        this.bestRanking = bestRanking;
        this.licence = licence;
        this.club = club;
        this.linkRanking = linkRanking;
        this.linkClubFindPlayer = linkClubFindPlayer;
        this.linkClub = linkClub;
        this.linkPalmares = linkPalmares;
    }

    @Override
    public String toString() {
        return "PlayerDto{" +
                "civility='" + civility + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", year='" + year + '\'' +
                ", ranking='" + ranking + '\'' +
                ", bestRanking='" + bestRanking + '\'' +
                ", licence='" + licence + '\'' +
                ", club='" + club + '\'' +
                ", linkRanking='" + linkRanking + '\'' +
                ", linkClubFindPlayer='" + linkClubFindPlayer + '\'' +
                ", linkClub='" + linkClub + '\'' +
                ", linkPalmares='" + linkPalmares + '\'' +
                '}';
    }
}
