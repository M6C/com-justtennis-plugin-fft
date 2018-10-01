package com.justtennis.plugin.fft.query.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RankingMatchResponse {

    public List<RankingItem> rankingList = new ArrayList<>();

    public static class RankingItem implements Serializable {
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

        @Override
        public String toString() {
            return "MatchItem{" +
                    "name='" + name + '\'' +
                    ", year='" + year + '\'' +
                    ", ranking='" + ranking + '\'' +
                    ", vicDef='" + vicDef + '\'' +
                    ", wo='" + wo + '\'' +
                    ", coef='" + coef + '\'' +
                    ", points='" + points + '\'' +
                    ", tournament='" + tournament + '\'' +
                    ", type='" + type + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}