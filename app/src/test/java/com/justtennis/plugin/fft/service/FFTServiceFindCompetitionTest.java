package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.model.enums.EnumCompetition;
import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

import java.io.IOException;

public class FFTServiceFindCompetitionTest extends AbstractFFTServiceTest {

    private FFTServiceFindCompetition fftServiceFindCompetition;

    @Override
    IProxy initializeService() {
        fftServiceFindCompetition = FFTServiceFindCompetition.newInstance(null);
        return fftServiceFindCompetition;
    }

    @Test
    public void testSubmitFormFindCompetition() throws NotConnectedException, IOException {
        ResponseHttp form = doLogin();

        ResponseHttp findCompetition = fftServiceFindCompetition.navigateToFindCompetition(form);

        for (EnumCompetition.TYPE type : EnumCompetition.TYPE.values()) {
            FindCompetitionFormResponse findCompetitionForm = fftServiceFindCompetition.getFindForm(findCompetition, type, "");

            assertNotNull(findCompetitionForm);
            assertNotNull(findCompetitionForm.action);
            assertNotNull(findCompetitionForm.city.name);
            assertNotNull(findCompetitionForm.dateStart.name);
            assertNotNull(findCompetitionForm.dateEnd.name);

            ResponseHttp submitForm = fftServiceFindCompetition.submitFindForm(form, findCompetitionForm);
            writeResourceFile(submitForm.body, "FFTServiceFindCompetitionTest_testSubmitFormFindCompetition_"+type.label+".html");
            assertNotNull(submitForm.body);

            FindCompetitionResponse findCompetitionResponse = fftServiceFindCompetition.getFindCompetition(submitForm);
            assertNotNull(findCompetitionResponse);
            assertTrue("Palmares Millesime List must not be empty", findCompetitionResponse.competitionList.size() > 0);
            for (FindCompetitionResponse.CompetitionItem item : findCompetitionResponse.competitionList) {
                assertNotNull(item.type);
                assertNotNull(item.league);
                assertNotNull(item.club);
                assertNotNull(item.name);
                assertNotNull(item.dateStart);
                assertNotNull(item.dateEnd);
                assertNotNull(item.club);
                assertNotNull(item.linkTournament);
            }
        }
    }
}