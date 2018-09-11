package com.justtennis.plugin.fft.model;

import java.util.ArrayList;
import java.util.List;

public class RankingListResponse {

    public List<RankingItem> rankingList = new ArrayList<>();

    public static class RankingItem {
        public String id;
        public String year;
        public String ranking;
        public String date;
        public String origin;

        @Override
        public String toString() {
            return "RankingItem{" +
                    "id=" + id +
                    ", year='" + year + '\'' +
                    ", ranking='" + ranking + '\'' +
                    ", date='" + date + '\'' +
                    ", origin='" + origin + '\'' +
                    '}';
        }
    }
}