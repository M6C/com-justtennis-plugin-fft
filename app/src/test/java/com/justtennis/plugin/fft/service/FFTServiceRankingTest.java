package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.RankingListResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class FFTServiceRankingTest extends AbstractFFTServiceTest {

    FFTServiceRanking fftServiceRanking;

    @Override
    IProxy initializeService() {
        fftServiceRanking = FFTServiceRanking.newInstance(null);
        return fftServiceRanking;
    }

    @Test
    public void testNavigateToRanking() throws NotConnectedException {
        ResponseHttp form = doLogin();
        writeResourceFile(form.body, "FFTServiceRankingTest_testNavigateToRanking_login.html");
        assertTrue(form.headerCookie.size() > 0);

        ResponseHttp ranking = fftServiceRanking.navigateToRanking(form);
        writeResourceFile(ranking.body, "FFTServiceRankingTest_testNavigateToRanking_ranking.html");

        assertNotNull(ranking);
        assertNotNull(ranking.body);
        assertEquals(200, ranking.statusCode);
//        assertNotNull(ranking.pathRedirect);
//        assertEquals(ranking.header.size(), 0);
    }

//    @Test
//    public void testGetRankingList() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        RankingListResponse ranking = fftServiceRanking.getRankingList(form);
//
//        assertNotNull(ranking);
//        assertTrue("Ranking List must not be empty", ranking.rankingList.size() > 0);
//        for (RankingListResponse.RankingItem item : ranking.rankingList) {
//            assertNotNull(item.id);
//            assertNotNull(item.year);
//            assertNotNull(item.ranking);
//            assertNotNull(item.date);
//            assertNotNull(item.origin);
//        }
//    }
//
//    @Test
//    public void testGetRankingMatch() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        RankingListResponse matchList = fftServiceRanking.getRankingList(form);
//
//        assertNotNull(matchList);
//        assertTrue("Ranking List must not be empty", matchList.rankingList.size() > 0);
//        RankingListResponse.RankingItem rank = matchList.rankingList.get(0);
//
//        RankingMatchResponse ranking = fftServiceRanking.getRankingMatch(form, rank.id);
//        assertNotNull(ranking);
//        assertTrue("Ranking List must not be empty", ranking.rankingList.size() > 0);
//        for (RankingMatchResponse.RankingItem item : ranking.rankingList) {
//            assertNotNull(item.name);
//            assertNotNull(item.year);
//            assertNotNull(item.ranking);
//            assertNotNull(item.vicDef);
//            assertNotNull(item.wo);
//            assertNotNull(item.coef);
//            assertNotNull(item.points);
//            assertNotNull(item.tournament);
//            assertNotNull(item.type);
//            assertNotNull(item.date);
//        }
//    }
}