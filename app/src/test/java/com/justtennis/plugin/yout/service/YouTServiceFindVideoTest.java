package com.justtennis.plugin.yout.service;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;
import com.justtennis.plugin.yout.query.response.FindVideoResponse;

import org.junit.Test;

public class YouTServiceFindVideoTest extends AbstractYouTServiceTest {

    private YouTServiceFindVideo youTServiceFindVideo;

    @Override
    IProxy initializeService() {
        youTServiceFindVideo = YouTServiceFindVideo.newInstance(null);
        return youTServiceFindVideo;
    }

//    @Test
//    public void testFindForm() {
//        ResponseHttp form = doLogin();
//
//        GenericFormResponse response = youTServiceFindVideo.getFindForm(form, "in flame");
//
//        assertNotNull(response);
//        assertNotNull(response.action);
//        Map<String, FormElement> fieldValue = response.fieldValue;
//        for(String key : fieldValue.keySet()) {
//            FormElement val = fieldValue.get(key);
//            assertNotNull(val.name);
//        }
//        assertNotNull(fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value);
//    }

    @Test
    public void testSubmitFindForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

            GenericFormResponse response = youTServiceFindVideo.getFindForm(form, "in flame");

            assertNotNull(response);

            ResponseHttp submitForm = youTServiceFindVideo.submitFindForm(form, response);
            writeResourceFile(submitForm.body, "YouTServiceFindVideoTest_testSubmitFindForm.html");
            assertNotNull(submitForm.body);

            FindVideoResponse findCompetitionResponse = youTServiceFindVideo.getFind(submitForm);
            assertNotNull(findCompetitionResponse);
            assertTrue("Video List must be not empty", findCompetitionResponse.videoList.size() > 0);
            for (FindVideoResponse.VideoItem item : findCompetitionResponse.videoList) {
                if(item.videoId != null) {
                    assertNotNull(item.url);
                    assertNotNull(item.title);
                    assertNotNull(item.subTitle);
                    assertNotNull(item.length);
                    assertNotNull(item.publishedTime);
                    assertTrue(item.videoThumbnails.size() > 0);
                } else if (item.channelId != null) {
                    assertNotNull(item.url);
                    assertNotNull(item.title);
                    assertNotNull(item.subTitle);
                    assertNotNull(item.length);
                    assertTrue(item.channelThumbnails.size()> 0);
                } else if (item.playlistId != null) {
                    assertNotNull(item.url);
                    assertNotNull(item.title);
                    assertNotNull(item.subTitle);
                    assertTrue(item.playlistThumbnails.size() > 0);
                } else {
                    assertNotNull("One id must be not null", null);
                }
            }
    }
}