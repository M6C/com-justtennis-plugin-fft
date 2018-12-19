package com.justtennis.plugin.fft.dto;

import com.justtennis.plugin.fft.common.FFTConfiguration;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchContent {

    private MatchContent() {}

    public static List<MatchDto> toDto(List<RankingMatchResponse.RankingItem> list) {
        List<MatchDto> ret = new ArrayList<>();
        int i=1;
        for(RankingMatchResponse.RankingItem item : list) {
            ret.add(createDto(i++, item));
        }
        return ret;
    }

    public static List<MatchDto> toDto2(List<MillesimeMatchResponse.MatchItem> list) {
        List<MatchDto> ret = new ArrayList<>();
        int i=1;
        for(MillesimeMatchResponse.MatchItem item : list) {
            ret.add(createDto(i++, item));
        }
        return ret;
    }

    public static String[] getFirstLastName(String name) {
        String[] ret = null;
        if (name != null) {
            String firstname = name;
            String lastname = "";
            int iSep = name.indexOf(' ');
            if (iSep > 0) {
                firstname = name.substring(0, iSep);
                lastname = name.substring(iSep + 1);
            }
            ret = new String[] {firstname, lastname};
        }
        return ret;
    }

    public static void sortDefault(List<MatchDto> list) {
        Collections.sort(list, (m1, m2) -> -(m1.vicDef.compareTo(m2.vicDef) + compareFFTDate(m1.date, m2.date)));
    }

    private static int compareFFTDate(String date1, String date2) {
        try {
            return FFTConfiguration.sdfFFT.parse(date1).compareTo(FFTConfiguration.sdfFFT.parse(date2));
        } catch (ParseException e) {
            logMe(e);
        }
        return 0;
    }

    private static MatchDto createDto(int position, RankingMatchResponse.RankingItem item) {
        return new MatchDto(String.valueOf(position), item.date, item.name, item.vicDef, item.ranking, null, item.points, item.wo, null);
    }

    private static MatchDto createDto(int position, MillesimeMatchResponse.MatchItem item) {
        return new MatchDto(String.valueOf(position), item.date, item.name, item.vicDef, item.ranking, item.score, null, item.wo, item.linkPalmares);
    }

    private static String makeDetails(RankingMatchResponse.RankingItem item) {
        return item.name + " " + item.ranking + " " + item.vicDef + " points:" + item.points;
    }

    private static String makeDetails(MillesimeMatchResponse.MatchItem item) {
        return item.name + " " + item.ranking + " " + item.vicDef + " score:" + item.score;
    }

    private static void logMe(Exception e) {
        e.printStackTrace();
    }
}
