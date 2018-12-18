package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.model.enums.EnumPlayer;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.FindPlayerFormResponse;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

import java.io.IOException;

public class FFTServiceFindPlayerTest extends AbstractFFTServiceTest {

    private FFTServiceFindPlayer fftServiceFindPlayer;

    @Override
    IProxy initializeService() {
        fftServiceFindPlayer = FFTServiceFindPlayer.newInstance(null);
        return fftServiceFindPlayer;
    }

    @Test
    public void testSubmitFormFindPlayer() throws NotConnectedException, IOException {
        ResponseHttp form = doLogin();

        ResponseHttp findPlayer = fftServiceFindPlayer.navigateToFindPlayer(form);

        FindPlayerFormResponse findPlayerForm = fftServiceFindPlayer.getFindForm(findPlayer, EnumPlayer.GENRE.WOMAN,"Leonie", "ROCA");

        assertNotNull(findPlayerForm);
        assertNotNull(findPlayerForm.action);
        assertNotNull(findPlayerForm.firstname.name);
        assertNotNull(findPlayerForm.lastname.name);
        assertNotNull(findPlayerForm.genre.name);

        ResponseHttp submitForm = fftServiceFindPlayer.submitFormFindPlayer(form, findPlayerForm);
        writeResourceFile(submitForm.body, "FFTServiceFindPlayerTest_testSubmitFormFindPlayer.html");
        assertNotNull(submitForm.body);
//        assertNotNull(submitForm.pathRedirect);
//        assertEquals(13, submitForm.header.size());

        FindPlayerResponse palmaresMillesimeMatch = fftServiceFindPlayer.getFindPlayer(submitForm);
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