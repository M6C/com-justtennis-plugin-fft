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

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        System.out.println("testGetForm body:"+formRedirect.body);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
        assertNotNull(publishFormResponse.message.name);
    }

    @Test
    public void testSubmitForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        FBPublishFormResponse publishFormResponse = fbServicePublish.getForm(formRedirect);
        publishFormResponse.message.value = "https://www.youtube.com/watch?v=vyw7rK24F20";

        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

        System.out.println("testSubmitForm body:"+submitFormResponse.body);

        assertEquals(302, submitFormResponse.statusCode);
        assertEquals(15, submitFormResponse.header.size());
    }
}