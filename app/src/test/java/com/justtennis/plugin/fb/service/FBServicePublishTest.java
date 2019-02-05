package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.manager.SharingYoutubeManager;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.skeleton.IProxy;

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

        SharingYoutubeManager youtubeManager = SharingYoutubeManager.getInstance();

        String urlId = "mKG8BR292oo";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://youtu.be/"+urlId};
        for(String u : url) {
            FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(form);
            publishFormResponse.message.value = "tête";
            publishFormResponse.publishId = youtubeManager.getIdFromUrl(u);
            publishFormResponse.publishTitle = "A Video from " + publishFormResponse.message.value;
            publishFormResponse.message.value = String.format("%s %s", publishFormResponse.message.value, u);
            System.out.println("youtubeId:" + publishFormResponse.publishId);
            ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

            assertNotNull(submitFormResponse);

            writeResourceFile(submitFormResponse.body, "FBServicePublishTest_testSubmitForm.html");

            assertEquals(200, submitFormResponse.statusCode);
        }
    }
}