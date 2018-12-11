package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class FBServiceProfilTest extends AbstractFBServiceTest {

    private FBServiceHomePage fbServiceHomePage;
    private FBServiceProfil fbServiceProfil;

    @Override
    protected IProxy initializeService() {
        fbServiceHomePage = FBServiceHomePage.newInstance(null);
        initializeProxy(fbServiceHomePage);
        fbServiceProfil = FBServiceProfil.newInstance(null);
        return fbServiceProfil;
    }

//    @Test
//    public void testNavigateToProfil() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//
//        FBHomePageResponse homePageResponse = FBServiceHomePage.getHomePage(formRedirect);
//        assertNotNull(homePageResponse.linkProfil);
//
//        ResponseHttp homePageHttpResponse = fbServiceProfil.navigateToProfil(form, homePageResponse);
//        assertNotNull(homePageHttpResponse);
//        assertNotNull(homePageHttpResponse.body);
//
//        assertEquals(200, homePageHttpResponse.statusCode);
//        assertEquals(16, homePageHttpResponse.header.size());
//
//        System.out.println("Test body:"+homePageHttpResponse.body);
//    }

    @Test
    public void testGetProfilPublication() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        FBHomePageResponse homePageResponse = FBServiceHomePage.getHomePage(formRedirect);
        assertNotNull(homePageResponse.linkProfil);

        ResponseHttp profileHttpResponse = fbServiceProfil.navigateToProfil(form, homePageResponse);
        writeResourceFile(profileHttpResponse.body, "FBServiceProfilTest_testGetProfilPublication_navigateToProfil.html");
        assertNotNull(profileHttpResponse.body);
        assertEquals(200, profileHttpResponse.statusCode);

        System.out.println("Test body:"+profileHttpResponse.body);

        FBProfilPublicationResponse profilPublicationResponse = fbServiceProfil.getProfilPublication(profileHttpResponse);
        assertNotNull(profilPublicationResponse);
        assertNotNull(profilPublicationResponse.timeLineList);
        assertTrue(profilPublicationResponse.timeLineList.size() > 0);

        for(FBProfilPublicationResponse.TimeLineItem item : profilPublicationResponse.timeLineList) {
            System.out.println("profilPublicationResponse.timeLineItem:"+item);
        }
    }
}