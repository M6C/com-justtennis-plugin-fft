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

//    @Test
//    public void testNavigateToHomePage() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//        assertNotNull(formRedirect.pathRedirect);
//        assertEquals(200, formRedirect.statusCode);
//        assertEquals(16, formRedirect.header.size());
//
//        System.out.println("Test body:"+formRedirect.body);
//    }

    @Test
    public void testNavigateToPathRedirect() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToPathRedirect(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);
        assertNotNull(formRedirect.pathRedirect);
        assertEquals(200, formRedirect.statusCode);
        assertEquals(16, formRedirect.header.size());

        System.out.println("testNavigateToPathRedirect body:"+formRedirect.body);
    }

//    @Test
//    public void testGetHomePage() throws NotConnectedException {
//        ResponseHttp form = doLogin();
//
//        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);
//
//        assertNotNull(formRedirect);
//        assertNotNull(formRedirect.body);
//
//        FBHomePageResponse homePageResponse = fbServiceHomePage.getHomePage(formRedirect);
//        assertNotNull(homePageResponse.name);
//        assertNotNull(homePageResponse.linkProfil);
//    }

    @Test
    public void testGetHomePage() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        System.out.println("testGetHomePage body:"+formRedirect.body);

        FBHomePageResponse homePageResponse = fbServiceHomePage.getHomePage(formRedirect);
        assertNotNull(homePageResponse.name);
        assertNotNull(homePageResponse.linkProfil);
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
//        ResponseHttp homePageResponse = fbServiceHomePage.navigateToProfil(form, formRedirect);
//        assertNotNull(homePageResponse);
//        assertNotNull(homePageResponse.body);
//        assertEquals(200, homePageResponse.statusCode);
//        assertEquals(16, homePageResponse.header.size());
//
//        System.out.println("Test body:"+homePageResponse.body);
//    }
}