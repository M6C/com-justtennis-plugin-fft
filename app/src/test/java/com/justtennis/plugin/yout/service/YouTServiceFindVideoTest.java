package com.justtennis.plugin.yout.service;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.FormElement;
import com.justtennis.plugin.shared.skeleton.IProxy;
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
}