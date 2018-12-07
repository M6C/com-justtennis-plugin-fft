package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class FBServiceHomePageTest extends AbstractFBServiceTest {

    private FBServiceHomePage fbServiceHomePage;
    @Override
    protected IProxy initializeService() {
        fbServiceHomePage = FBServiceHomePage.newInstance(null);
        return fbServiceHomePage;
    }

    @Test
    public void testNavigateToHomePage() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        writeResourceFile(formRedirect.body, "FBServiceHomePageTest_navigateToHomePage.html");

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertEquals(200, formRedirect.statusCode);

        System.out.println("Test body:"+formRedirect.body);
    }

//    @Test
//    public void testNavigateToPathRedirect() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = fbServiceHomePage.navigateToPathRedirect(form);
//
//        writeResourceFile(formRedirect.body, "testGetHomePage_navigateToPathRedirect.html");
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//        assertEquals(200, formRedirect.statusCode);
////        assertEquals(16, formRedirect.header.size());
//
//        System.out.println("testNavigateToPathRedirect body:"+formRedirect.body);
//    }

    @Test
    public void testGetHomePage() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);
//        ResponseHttp formRedirect = responseHttpFromResourceFile("testGetHomePage_navigateToHomePage.html");

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        writeResourceFile(formRedirect.body, "FBServiceHomePageTest_testGetHomePage.html");

        FBHomePageResponse homePageResponse = FBServiceHomePage.getHomePage(formRedirect);
        assertNotNull(homePageResponse.name);
        assertNotNull(homePageResponse.linkProfil);
    }
}