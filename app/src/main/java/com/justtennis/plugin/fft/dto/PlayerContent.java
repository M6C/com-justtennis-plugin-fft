package com.justtennis.plugin.fft.dto;

import com.justtennis.plugin.fft.query.response.FindPlayerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerContent {

    private PlayerContent() {}

    public static List<PlayerDto> toDto(List<FindPlayerResponse.PlayerItem> list) {
        List<PlayerDto> ret = new ArrayList<>();
        for(FindPlayerResponse.PlayerItem item : list) {
            ret.add(createDao(item));
        }
        return ret;
    }

    public static void sortDefault(List<PlayerDto> list) {
        Collections.sort(list, (m1, m2) -> -(m1.lastname.compareTo(m2.lastname) + m1.firstname.compareTo(m2.firstname)));
    }

    private static PlayerDto createDao(FindPlayerResponse.PlayerItem item) {
        return new PlayerDto(item.civility, item.lastname, item.firstname, item.year, item.ranking, item.bestRanking, item.licence, item.club, item.linkRanking, item.linkClubFindPlayer, item.linkClub, item.linkPalmares);
    }
}
