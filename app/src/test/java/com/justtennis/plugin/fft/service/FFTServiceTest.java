package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.model.LoginFormResponse;
import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.model.PalmaresResponse;
import com.justtennis.plugin.fft.model.RankingListResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.network.model.ResponseHttp;

import junit.framework.TestCase;

import java.io.IOException;

public class FFTServiceTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    private static final boolean useProxy = true;

    public static void testGetLoginForm(String login, String password) {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        assertNotNull(response.action);
        assertNotNull(response.button.name);
        assertNotNull(response.login.name);
        assertNotNull(response.password.name);
        assertEquals(response.login.value, LOGIN);
        assertEquals(response.password.value, PASWD);
        assertEquals(response.input.size(), 16);
    }

    public static void testSubmitFormLogin() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        assertNotNull(form.body);
        assertNotNull(form.pathRedirect);
        assertEquals(form.header.size(), 16);
    }

    public static void testNavigateToFormRedirect() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        ResponseHttp formRedirect = fftService.navigateToFormRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(formRedirect.header.size(), 0);
    }

    public static void testNavigateToRanking() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        ResponseHttp ranking = fftService.navigateToRanking(form);

        assertNotNull(ranking);
        assertNotNull(ranking.body);
        assertNotNull(ranking.pathRedirect);
        assertEquals(ranking.header.size(), 0);
    }

    public static void testGetRankingList() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

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

    public static void testGetRankingMatch() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        RankingListResponse rankingList = fftService.getRankingList(form);

        assertNotNull(rankingList);
        assertTrue("Ranking List must not be empty", rankingList.rankingList.size() > 0);
        RankingListResponse.RankingItem rank = rankingList.rankingList.get(0);

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

    public static void testGetParmares() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        ResponseHttp home = fftService.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftService.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftService.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);
    }

    public static void testGetParmaresMillesime() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        ResponseHttp home = fftService.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftService.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftService.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);

        PalmaresMillesimeResponse palmaresMillesimeResponse = fftService.getPalmaresMillesime(palmares);
        assertNotNull(palmaresMillesimeResponse.action);
        assertNotNull(palmaresMillesimeResponse.select);
        assertNotNull(palmaresMillesimeResponse.select.name);
        assertNotNull(palmaresMillesimeResponse.millesimeSelected);
        assertEquals(palmaresMillesimeResponse.listMillesime.size(), 4);
        assertEquals(palmaresMillesimeResponse.input.size(), 3);
        assertTrue(palmaresMillesimeResponse.listMillesime.contains(palmaresMillesimeResponse.millesimeSelected));
    }

    public static void testSubmitFormPalmaresMillesime() throws IOException {
        FFTService fftService = newFFTService();
        LoginFormResponse response = fftService.getLoginForm(LOGIN, PASWD);

        ResponseHttp form = fftService.submitFormLogin(response);

        ResponseHttp home = fftService.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftService.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftService.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);

        PalmaresMillesimeResponse palmaresMillesimeResponse = fftService.getPalmaresMillesime(palmares);
        assertTrue(palmaresMillesimeResponse.listMillesime.size() > 0);

        for(PalmaresMillesimeResponse.Millesime millesime : palmaresMillesimeResponse.listMillesime) {
            if (!millesime.equals(palmaresMillesimeResponse.millesimeSelected)) {
                palmaresMillesimeResponse.millesimeSelected = millesime;
                break;
            }
        }

        ResponseHttp submitForm = fftService.submitFormPalmaresMillesime(form, palmaresMillesimeResponse);
        assertNotNull(submitForm.body);
        assertNotNull(submitForm.pathRedirect);
        assertEquals(submitForm.header.size(), 13);
    }

    private static FFTService newFFTService() {
        FFTService instance = FFTService.newInstance();
        if (useProxy) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
        return instance;
    }
}