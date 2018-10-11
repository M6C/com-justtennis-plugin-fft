package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;

import org.junit.Test;

public class FFTServiceRankingTest extends AbstractFFTServiceTest {

    @Test
    public static void testNavigateToRanking() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp ranking = fftService.navigateToRanking(form);

        assertNotNull(ranking);
        assertNotNull(ranking.body);
        assertNotNull(ranking.pathRedirect);
        assertEquals(ranking.header.size(), 0);
    }

    @Test
    public static void testGetRankingList() throws NotConnectedException {
        ResponseHttp form = doLogin();

        RankingListResponse ranking = fftService.getRankingList(form);

        assertNotNull(ranking);
        assertTrue("Ranking List must not be empty", ranking.rankingList.size() > 0);
        for (RankingListResponse.RankingItem item : ranking.rankingList) {
            assertNotNull(item.id);
            assertNotNull(item.year);
            assertNotNull(item.ranking);
            assertNotNull(item.date);
            assertNotNull(item.origin);
        }
    }

    @Test
    public static void testGetRankingMatch() throws NotConnectedException {
        ResponseHttp form = doLogin();

        RankingListResponse matchList = fftService.getRankingList(form);

        assertNotNull(matchList);
        assertTrue("Ranking List must not be empty", matchList.rankingList.size() > 0);
        RankingListResponse.RankingItem rank = matchList.rankingList.get(0);

        RankingMatchResponse ranking = fftService.getRankingMatch(form, rank.id);
        assertNotNull(ranking);
        assertTrue("Ranking List must not be empty", ranking.rankingList.size() > 0);
        for (RankingMatchResponse.RankingItem item : ranking.rankingList) {
            assertNotNull(item.name);
            assertNotNull(item.year);
            assertNotNull(item.ranking);
            assertNotNull(item.vicDef);
            assertNotNull(item.wo);
            assertNotNull(item.coef);
            assertNotNull(item.points);
            assertNotNull(item.tournament);
            assertNotNull(item.type);
            assertNotNull(item.date);
        }
    }
}