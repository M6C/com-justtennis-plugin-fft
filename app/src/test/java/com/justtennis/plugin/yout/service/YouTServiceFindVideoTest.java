package com.justtennis.plugin.yout.service;

import org.cameleon.android.generic.query.response.GenericFormResponse;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.query.response.FormElement;
import org.cameleon.android.shared.skeleton.IProxy;
import com.justtennis.plugin.yout.query.request.YouTFindVideoFormRequest;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;

import org.junit.Test;

import java.util.Map;

public class YouTServiceFindVideoTest extends AbstractYouTServiceTest {

    private YouTServiceFindVideo youTServiceFindVideo;

    @Override
    IProxy initializeService() {
        youTServiceFindVideo = YouTServiceFindVideo.newInstance(null);
        return youTServiceFindVideo;
    }

    @Test
    public void testFindForm() {
        ResponseHttp form = doLogin();

        GenericFormResponse response = youTServiceFindVideo.getFindForm(form, "in flame");

        assertNotNull(response);
        assertNotNull(response.action);
        Map<String, FormElement> fieldValue = response.fieldValue;
        for(String key : fieldValue.keySet()) {
            FormElement val = fieldValue.get(key);
            assertNotNull(val.name);
        }
        assertNotNull(fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value);
    }

    @Test
    public void testSubmitFindForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        GenericFormResponse response = youTServiceFindVideo.getFindForm(form, "in flame");

        assertNotNull(response);

        ResponseHttp submitForm = youTServiceFindVideo.submitFindForm(form, response);
        writeResourceFile(submitForm.body, "YouTServiceFindVideoTest_testSubmitFindForm.html");
        assertNotNull(submitForm.body);

        YoutFindVideoResponse findCompetitionResponse = youTServiceFindVideo.getFind(submitForm);
        assertNotNull(findCompetitionResponse);
        assertTrue("Video List must be not empty", findCompetitionResponse.videoList.size() > 0);
        for (YoutFindVideoResponse.VideoItem item : findCompetitionResponse.videoList) {
            if(item.videoId != null) {
                assertNotNull(item.url);
                assertNotNull(item.title);
                assertNotNull(item.subTitle);
                assertNotNull(item.length);
                assertNotNull(item.publishedTime);
            } else if (item.channelId != null) {
                assertNotNull(item.url);
                assertNotNull(item.title);
                assertNotNull(item.subTitle);
                assertNotNull(item.length);
            } else if (item.playlistId != null) {
                assertNotNull(item.url);
                assertNotNull(item.title);
                assertNotNull(item.subTitle);
            } else {
                assertNotNull("One id must be not null", null);
            }
            assertTrue(item.thumbnails.size() > 0);
        }
    }

    @Test
    public void testGotoUrlPlaylist() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = youTServiceFindVideo.gotoUrl(form, "/watch?v=pAgnJDJN4VA&start_radio=1&list=RDEMDs8vWIQKMflBG8QUQQaUrw");
        checkGotoUrl(formRedirect);
    }

    @Test
    public void testGotoUrlChannel() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = youTServiceFindVideo.gotoUrl(form, "/user/acdc");
        checkGotoUrl(formRedirect);
    }

    private void checkGotoUrl(ResponseHttp formRedirect) {
        writeResourceFile(formRedirect.body, "YouTServiceFindVideoTest_testGotoUrl.html");
        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertEquals(200, formRedirect.statusCode);

        YoutFindVideoResponse findResponse = youTServiceFindVideo.getFindPlaylist(formRedirect);
        assertNotNull(findResponse);
        assertTrue(findResponse.videoList.size() > 0);
    }
}