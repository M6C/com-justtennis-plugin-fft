package com.justtennis.plugin.fft.query.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindPlayerResponse {

    public List<PlayerItem> playerList = new ArrayList<>();

    public static class PlayerItem implements Serializable {
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

        @Override
        public String toString() {
            return "PlayerItem{" +
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
}