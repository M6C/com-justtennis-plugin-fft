package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class FBServicePublishEncodingTest extends AbstractFBServiceTest {

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
//        publishFormResponse.message.value = encodeToUnicode("https://www.youtube.com/watch?v=vyw7rK24F20");
        publishFormResponse.message.value = encodeToUnicode("... 😘😘😘😘😘😘😘");

        ResponseHttp submitFormResponse = fbServicePublish.submitForm(form, publishFormResponse);

//        System.out.println("testSubmitForm body:"+submitFormResponse.body);

        assertEquals(302, submitFormResponse.statusCode);
        assertEquals(15, submitFormResponse.header.size());
    }


    private String encodeToUnicode(String text) {
        StringBuilder ret = new StringBuilder();
        char[] encoded = text.toCharArray();
        for (char c : encoded) {
            ret.append(unicodeEscaped(c));
        }
        return ret.toString();
    }
    public static String unicodeEscaped(char ch) {
//        if (ch < 0x10) {
//            return "\\u000" + Integer.toHexString(ch);
//        } else if (ch < 0x100) {
//            return "\\u00" + Integer.toHexString(ch);
//        } else if (ch < 0x1000) {
//            return "\\u0" + Integer.toHexString(ch);
//        }
        if (ch < 0x1000) {
            return String.valueOf(ch);
        }
//        return "\\u" + Integer.toHexString(ch);
        return "%" + ch;//Integer.toHexString(ch);
    }
    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }
}