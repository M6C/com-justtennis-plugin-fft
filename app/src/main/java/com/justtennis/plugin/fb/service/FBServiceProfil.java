package com.justtennis.plugin.fb.service;

import android.content.Context;

import com.justtennis.plugin.fb.parser.FBProfilParser;
import com.justtennis.plugin.fb.query.request.FBProfilPublicationRequest;
import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;

public class FBServiceProfil extends AbstractFBService {

    private static final String TAG = FBServiceProfil.class.getName();

    private FBServiceProfil(Context context) {
        super(context);
    }

    public static FBServiceProfil newInstance(Context context) {
        return new FBServiceProfil(context);
    }

    public ResponseHttp navigateToProfil(ResponseHttp loginFormResponse, FBHomePageResponse homePageResponse) throws NotConnectedException {
        logMethod("navigateToProfil");
        return doGetConnected(URL_ROOT, homePageResponse.linkProfil, loginFormResponse);
    }

    public FBProfilPublicationResponse getProfilPublication(ResponseHttp profilResponse) {
        logMethod("getProfilPublication");
        return FBProfilParser.parsePublication(profilResponse.body, new FBProfilPublicationRequest());
    }
}
