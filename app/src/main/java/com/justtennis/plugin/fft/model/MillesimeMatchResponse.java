package com.justtennis.plugin.fft.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MillesimeMatchResponse {

    public List<MatchItem> matchList = new ArrayList<>();

    public static class MatchItem implements Serializable {
        public String name;
        public String year;
        public String ranking;
        public String vicDef;
        public String score;
        public String wo;
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
                    ", score='" + score + '\'' +
                    ", wo='" + wo + '\'' +
                    ", tournament='" + tournament + '\'' +
                    ", type='" + type + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}