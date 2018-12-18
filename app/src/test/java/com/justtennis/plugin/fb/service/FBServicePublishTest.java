package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.manager.SharingYoutubeManager;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import java.util.Map;

public class FBServicePublishTest extends AbstractFBServiceTest {

    private FBServicePublish fbServicePublish;

    @Override
    protected IProxy initializeService() {
        fbServicePublish = FBServicePublish.newInstance(null);
        return fbServicePublish;
    }

    public void testGetForm() {
        ResponseHttp form = doLogin();

        writeResourceFile(form.body, "FBServicePublishTest_testGetForm_login.html");

        assertNotNull(form);
        assertNotNull(form.body);

        System.out.println("testGetForm body:"+ form.body);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(form);
        assertNotNull(publishFormResponse);
        assertNotNull(publishFormResponse.message);
        assertNotNull(publishFormResponse.message.name);
        assertNotNull(publishFormResponse.audience);
    }

    public void testSubmitForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(form);
        publishFormResponse.message.value = "tête... 😘😘😘😘😘😘😘";

        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

        assertNotNull(submitFormResponse);

        writeResourceFile(submitFormResponse.body, "FBServicePublishTest_testSubmitForm.html");

        assertEquals(200, submitFormResponse.statusCode);
    }

    public void testSubmitYoutubeForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(form);
        publishFormResponse.message.value = "tête";

        SharingYoutubeManager youtubeManager = SharingYoutubeManager.getInstance();

        String urlId = "mKG8BR292oo";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://youtu.be/"+urlId};
        for(String u : url) {
            String youtubeId = youtubeManager.getIdFromUrl(u);
            System.out.println("youtubeId:" + youtubeId);
            Map<String, String> data = youtubeManager.getData(youtubeId, "A Video from " + publishFormResponse.message.value);
            publishFormResponse.message.value = youtubeManager.cleanUrl(String.format("%s %s", publishFormResponse.message.value, u));
            ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse, data);

            assertNotNull(submitFormResponse);

            writeResourceFile(submitFormResponse.body, "FBServicePublishTest_testSubmitForm.html");

            assertEquals(200, submitFormResponse.statusCode);
        }
    }
}