package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;

import org.junit.Test;

import java.io.IOException;

public class FFTServiceFindPlayerTest extends AbstractFFTServiceTest {

    @Test
    public static void testSubmitFormFindPlayer() throws NotConnectedException, IOException {
        ResponseHttp form = doLogin();

        ResponseHttp findPlayer = fftService.navigateToFindPlayer(form);

        FindPlayerFormResponse findPlayerForm = fftService.getFindPlayerForm(findPlayer, FFTService.PLAYER_SEX.WOMAN,"Leonie", "ROCA");

        assertNotNull(findPlayerForm);
        assertNotNull(findPlayerForm.action);
        assertNotNull(findPlayerForm.firstname.name);
        assertNotNull(findPlayerForm.lastname.name);
        assertNotNull(findPlayerForm.sex.name);

        ResponseHttp submitForm = fftService.submitFormFindPlayer(form, findPlayerForm);
        assertNotNull(submitForm.body);
        assertNotNull(submitForm.pathRedirect);
//        assertEquals(13, submitForm.header.size());

        FindPlayerResponse palmaresMillesimeMatch = fftService.getFindPlayer(submitForm);
        assertNotNull(palmaresMillesimeMatch);
        assertTrue("Palmares Millesime List must not be empty", palmaresMillesimeMatch.playerList.size() > 0);
       for (FindPlayerResponse.PlayerItem item : palmaresMillesimeMatch.playerList) {
            assertNotNull(item.civility);
            assertNotNull(item.firstname);
            assertNotNull(item.lastname);
            assertNotNull(item.year);
            assertNotNull(item.ranking);
            assertNotNull(item.bestRanking);
            assertNotNull(item.licence);
            assertNotNull(item.club);
            assertNotNull(item.linkRanking);
            assertNotNull(item.linkClubFindPlayer);
            assertNotNull(item.linkClub);
            assertNotNull(item.linkPalmares);
        }
    }
}