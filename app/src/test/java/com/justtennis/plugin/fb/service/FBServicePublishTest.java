package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

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

        writeResourceFile(form.body, "testGetForm_login.html");
        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

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

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
//        publishFormResponse.message.value = "https://www.youtube.com/watch?v=vyw7rK24F20";
        publishFormResponse.message.value = "tête";

        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

        System.out.println("testSubmitForm body:"+submitFormResponse.body);

        writeResourceFile(submitFormResponse.body, "FBServicePublishTest_testSubmitForm.html");

        assertEquals(200, submitFormResponse.statusCode);
//        assertEquals(16, submitFormResponse.header.size());
    }
}