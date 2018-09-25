package com.justtennis.plugin.fft.dto;

import com.justtennis.plugin.fft.model.MillesimeMatchResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatchContent {

    private MatchContent() {}

    public static List<MatchDto> toDto(List<RankingMatchResponse.RankingItem> list) {
        List<MatchDto> ret = new ArrayList<>();
        int i=1;
        for(RankingMatchResponse.RankingItem item : list) {
            ret.add(createDao(i++, item));
        }
        return ret;
    }

    public static List<MatchDto> toDto2(List<MillesimeMatchResponse.MatchItem> list) {
        List<MatchDto> ret = new ArrayList<>();
        int i=1;
        for(MillesimeMatchResponse.MatchItem item : list) {
            ret.add(createDao(i++, item));
        }
        return ret;
    }

    private static MatchDto createDao(int position, RankingMatchResponse.RankingItem item) {
        return new MatchDto(String.valueOf(position), item.date, makeDetails(item));
    }

    private static MatchDto createDao(int position, MillesimeMatchResponse.MatchItem item) {
        return new MatchDto(String.valueOf(position), item.date, makeDetails(item));
    }

    private static String makeDetails(RankingMatchResponse.RankingItem item) {
        return item.name + " " + item.ranking + " " + item.vicDef + " points:" + item.points;
    }

    private static String makeDetails(MillesimeMatchResponse.MatchItem item) {
        return item.name + " " + item.ranking + " " + item.vicDef + " score:" + item.score;
    }
}
