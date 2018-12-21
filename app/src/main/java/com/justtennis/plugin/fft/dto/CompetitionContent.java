package com.justtennis.plugin.fft.dto;

import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompetitionContent {

    private CompetitionContent() {}

    public static CompetitionDto toDto(FindCompetitionResponse.CompetitionItem item) {
        return createDao(item);
    }

    public static List<CompetitionDto> toDto(List<FindCompetitionResponse.CompetitionItem> list) {
        List<CompetitionDto> ret = new ArrayList<>();
        for(FindCompetitionResponse.CompetitionItem item : list) {
            ret.add(createDao(item));
        }
        return ret;
    }

    public static void sortDefault(List<CompetitionDto> list) {
        Collections.sort(list, (m1, m2) -> -(m1.league.compareTo(m2.league) + m1.club.compareTo(m2.club) + m1.name.compareTo(m2.name)));
    }

    private static CompetitionDto createDao(FindCompetitionResponse.CompetitionItem item) {
        return new CompetitionDto(item.type, item.club, item.league, item.name, item.dateStart, item.dateEnd, item.linkTournament);
    }
}
