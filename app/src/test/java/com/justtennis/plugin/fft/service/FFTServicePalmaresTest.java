package com.justtennis.plugin.fft.service;

import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.query.response.PalmaresResponse;
import org.cameleon.android.shared.skeleton.IProxy;

import org.junit.Test;

import java.io.IOException;

public class FFTServicePalmaresTest extends AbstractFFTServiceTest {

    FFTServicePalmares fftServicePalmares;

    @Override
    IProxy initializeService() {
        fftServicePalmares = FFTServicePalmares.newInstance(null);
        return fftServicePalmares;
    }

    @Test
    public void testGetParmares() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp home = fftServiceLogin.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftServicePalmares.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);
    }

    @Test
    public void testGetParmaresMillesime() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp home = fftServiceLogin.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftServicePalmares.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);

        PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
        assertNotNull(palmaresMillesimeResponse.action);
        assertNotNull(palmaresMillesimeResponse.select);
        assertNotNull(palmaresMillesimeResponse.select.name);
        assertNotNull(palmaresMillesimeResponse.millesimeSelected);
        assertEquals(palmaresMillesimeResponse.listMillesime.size(), 4);
        assertEquals(palmaresMillesimeResponse.input.size(), 3);
        assertTrue(palmaresMillesimeResponse.listMillesime.contains(palmaresMillesimeResponse.millesimeSelected));
    }

    @Test
    public void testSubmitFormPalmaresMillesime() throws NotConnectedException, IOException {
        ResponseHttp form = doLogin();

        ResponseHttp home = fftServiceLogin.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftServicePalmares.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);

        PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
        assertTrue(palmaresMillesimeResponse.listMillesime.size() > 0);

        changeMillesimeSelected(palmaresMillesimeResponse);

        ResponseHttp submitForm = fftServicePalmares.submitFormPalmaresMillesime(form, palmaresMillesimeResponse);
        assertNotNull(submitForm.body);
//        assertNotNull(submitForm.pathRedirect);
//        assertEquals(13, submitForm.header.size());
    }

    @Test
    public void testGetPalmaresMillesimeMatch() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp home = fftServiceLogin.navigateToFormRedirect(form);

        PalmaresResponse palmaresResponse = fftServicePalmares.getPalmares(home);

        assertNotNull(palmaresResponse);
        assertNotNull(palmaresResponse.action);

        ResponseHttp palmares = fftServicePalmares.navigateToPalmares(form, palmaresResponse);
        assertNotNull(palmares.body);

        PalmaresMillesimeResponse palmaresMillesimeResponse = fftServicePalmares.getPalmaresMillesime(palmares);
        assertTrue(palmaresMillesimeResponse.listMillesime.size() > 0);

        changeMillesimeSelected(palmaresMillesimeResponse);

        ResponseHttp submitForm = fftServicePalmares.submitFormPalmaresMillesime(form, palmaresMillesimeResponse);
        assertNotNull(submitForm.body);

        MillesimeMatchResponse palmaresMillesimeMatch = fftServicePalmares.getPalmaresMillesimeMatch(submitForm);
        assertNotNull(palmaresMillesimeMatch);
        assertTrue("Palmares Millesime List must not be empty", palmaresMillesimeMatch.matchList.size() > 0);
       for (MillesimeMatchResponse.MatchItem item : palmaresMillesimeMatch.matchList) {
            assertNotNull(item.name);
            assertNotNull(item.year);
            assertNotNull(item.ranking);
            assertNotNull(item.vicDef);
            assertNotNull(item.score);
            assertNotNull(item.wo);
            assertNotNull(item.tournament);
            assertNotNull(item.type);
            assertNotNull(item.date);
            assertNotNull(item.linkPalmares);
//            assertNotNull(item.linkTournoi);
        }
    }
}