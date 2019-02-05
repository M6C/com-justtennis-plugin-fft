package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import org.cameleon.android.shared.exception.NotConnectedException;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.skeleton.IProxy;

public class FBServiceHomePageTest extends AbstractFBServiceTest {

    private FBServiceHomePage fbServiceHomePage;
    @Override
    protected IProxy initializeService() {
        fbServiceHomePage = FBServiceHomePage.newInstance(null);
        return fbServiceHomePage;
    }

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