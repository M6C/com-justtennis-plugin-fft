package com.justtennis.plugin.fb.service;

import android.content.Context;

import com.justtennis.plugin.fb.parser.FBHomePageParser;
import com.justtennis.plugin.fb.query.request.FBHomePageRequest;
import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

import java.util.HashMap;

public class FBServiceHomePage extends AbstractFBService {

    private static final String TAG = FBServiceHomePage.class.getName();

    private FBServiceHomePage(Context context) {
        super(context);
        initializeProxy(this);
    }

    public static FBServiceHomePage newInstance(Context context) {
        return new FBServiceHomePage(context);
    }

    public ResponseHttp navigateToPathRedirect(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToPathRedirect");
        return doPostConnected(URL_ROOT, loginFormResponse.pathRedirect, new HashMap<>(), loginFormResponse);
    }

    public ResponseHttp navigateToHomePage(ResponseHttp loginFormResponse) throws NotConnectedException {
        logMethod("navigateToHomePage");
        return doGetConnected(URL_ROOT, "", loginFormResponse);
    }

    public FBHomePageResponse getHomePage(ResponseHttp homePageResponse) {
        logMethod("getHomePage");
        return FBHomePageParser.parseHomePage(homePageResponse.body, new FBHomePageRequest());
    }

    public ResponseHttp navigateToProfil(ResponseHttp loginFormResponse, ResponseHttp homePageResponse) throws NotConnectedException {
        logMethod("navigateToProfil");
        return navigateToProfil(loginFormResponse, getHomePage(homePageResponse));
    }

    public ResponseHttp navigateToProfil(ResponseHttp loginFormResponse, FBHomePageResponse homePageResponse) throws NotConnectedException {
        logMethod("navigateToProfil");
        return doGetConnected(URL_ROOT, homePageResponse.linkProfil, loginFormResponse);
    }
}
