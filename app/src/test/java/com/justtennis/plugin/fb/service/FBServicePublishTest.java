package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;
import com.justtennis.plugin.yt.manager.YoutubeManager;

import org.junit.Test;

import java.util.Map;

public class FBServicePublishTest extends AbstractFBServiceTest {

    private FBServiceHomePage fbServiceHomePage;
    private FBServicePublish fbServicePublish;

    @Override
    protected IProxy initializeService() {
        fbServiceHomePage = FBServiceHomePage.newInstance(null);
        initializeProxy(fbServiceHomePage);
        fbServicePublish = FBServicePublish.newInstance(null);
        return fbServicePublish;
    }

    @Test
    public void testGetForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        writeResourceFile(form.body, "FBServicePublishTest_testGetForm_login.html");
        ResponseHttp formRedirect = form;//fbServiceHomePage.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        System.out.println("testGetForm body:"+formRedirect.body);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
        assertNotNull(publishFormResponse);
        assertNotNull(publishFormResponse.message);
        assertNotNull(publishFormResponse.message.name);
        assertNotNull(publishFormResponse.audience);
    }

    @Test
    public void testSubmitForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = form;//fbServiceHomePage.navigateToHomePage(form);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
        publishFormResponse.message.value = "tête... 😘😘😘😘😘😘😘";

        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

        assertNotNull(submitFormResponse);

        writeResourceFile(submitFormResponse.body, "FBServicePublishTest_testSubmitForm.html");

        assertEquals(200, submitFormResponse.statusCode);
    }

    @Test
    public void testSubmitYoutubeForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = form;//fbServiceHomePage.navigateToHomePage(form);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
        publishFormResponse.message.value = "tête";

        YoutubeManager youtubeManager = YoutubeManager.getInstance();

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