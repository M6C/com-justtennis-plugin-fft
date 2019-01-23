package com.justtennis.plugin.eytm.service;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.service.YouTServiceFindVideo;
import com.justtennis.plugin.yout.service.YoutServiceLogin;

import org.junit.Test;

public class EasyYouTMp3ServiceTest extends AbstractEasyYouTMp3ServiceTest {

    private EasyYouTMp3Service service;
    private YouTServiceFindVideo youTServiceVideo;
    private YoutServiceLogin youTServiceLogin;

    @Override
    IProxy initializeService() {
        service = EasyYouTMp3Service.newInstance(null);

        youTServiceLogin = YoutServiceLogin.newInstance(null);
        initializeProxy(youTServiceLogin);

        youTServiceVideo = YouTServiceFindVideo.newInstance(null);
        initializeProxy(youTServiceVideo);

        return service;
    }

    @Test
    public void testSubmitFindForm() {
        doLogin();

        ResponseHttp response = service.navigateToVideo("V0gd52_3EUU");
        assertNotNull(response);
        assertEquals(200, response.statusCode);
        writeResourceFile(response.body, "EasyYouTMp3ServiceTest_testSubmitFindForm.html");
        assertNotNull(response.body);
        assertNotSame("", response.body);

        GenericResponse find = service.parseFind(response);
        assertNotNull(find);
        assertTrue("Donwload List must be not empty", !find.data.isEmpty());
        for (GenericResponse.Item item : find.data) {
            for (String key : item.itemValue.keySet()) {
                assertNotNull(item.itemValue.get(key));
            }
        }

        service.downloadLink(find, "test.mp3");
    }

    @Test
    public void testDownloadWithYoutubeForm() throws NotConnectedException {
        doLogin();

        ResponseHttp form = youTServiceLogin.navigateToHomePage();

        GenericFormResponse response = youTServiceVideo.getFindForm(form, "acdc T.N.T");
        assertNotNull(response);

        ResponseHttp submitForm = youTServiceVideo.submitFindForm(form, response);
        assertNotNull(submitForm.body);

        YoutFindVideoResponse findCompetitionResponse = youTServiceVideo.getFind(submitForm);
        assertNotNull(findCompetitionResponse);
        assertTrue("Video List must be not empty", findCompetitionResponse.videoList.size() > 0);
        for (YoutFindVideoResponse.VideoItem item : findCompetitionResponse.videoList) {
            if(item.videoId != null) {

                ResponseHttp responseVideo = service.navigateToVideo(item.videoId);
                assertNotNull(response);
                assertEquals(200, responseVideo.statusCode);
                writeResourceFile(responseVideo.body, "EasyYouTMp3ServiceTest_testSubmitFindForm.html");
                assertNotNull(responseVideo.body);
                assertNotSame("", responseVideo.body);

                GenericResponse find = service.parseFind(responseVideo);
                assertNotNull(find);
                assertTrue("Donwload List must be not empty", !find.data.isEmpty());
                GenericResponse.Item d = find.data.get(0);
                for (String key : d.itemValue.keySet()) {
                    assertNotNull(d.itemValue.get(key));
                }

                service.downloadLink(find, item.title + ".mp3");
                break;
            }
        }
    }
}