package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.RankingListResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.network.model.ResponseHttp;

import junit.framework.TestCase;

import java.io.IOException;

public class FFTServiceTest extends TestCase {

    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    public static void testGetLoginForm(String login, String password) {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        assertNotNull(response.action);
        assertNotNull(response.button.name);
        assertNotNull(response.login.name);
        assertNotNull(response.password.name);
        assertEquals(response.login.value, LOGIN);
        assertEquals(response.password.value, PASWD);
        assertEquals(response.input.size(), 16);
    }

    public static void testSubmitFormLogin() throws IOException {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FFTService.submitFormLogin(response);

        assertNotNull(form.body);
        assertNotNull(form.pathRedirect);
        assertEquals(form.header.size(), 16);
    }

    public static void testNavigateToFormRedirect() throws IOException {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FFTService.submitFormLogin(response);

        ResponseHttp formRedirect = FFTService.navigateToFormRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.header.size(), 0);
    }

    public static void testNavigateToRanking() throws IOException {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FFTService.submitFormLogin(response);

        ResponseHttp ranking = FFTService.navigateToRanking(form);

        assertNotNull(ranking);
        assertNotNull(ranking.body);
        assertNotNull(ranking.pathRedirect);
        assertEquals(ranking.header.size(), 0);
    }

    public static void testGetRankingList() throws IOException {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FFTService.submitFormLogin(response);

        RankingListResponse ranking = FFTService.getRankingList(form);

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

    public static void testGetRankingMatch() throws IOException {
        LoginFormResponse response = FFTService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = FFTService.submitFormLogin(response);

        RankingListResponse rankingList = FFTService.getRankingList(form);

        assertNotNull(rankingList);
        assertTrue("Ranking List must not be empty", rankingList.rankingList.size() > 0);
        RankingListResponse.RankingItem rank = rankingList.rankingList.get(0);

        RankingMatchResponse ranking = FFTService.getRankingMatch(form, rank.id);
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