package com.justtennis.plugin.fb.service;

import android.content.Context;

import com.justtennis.plugin.fb.query.request.FBLoginFormRequest;
import org.cameleon.android.shared.converter.LoginFormResponseConverter;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.network.tool.NetworkTool;
import org.cameleon.android.shared.parser.FormLoginParser;
import org.cameleon.android.shared.preference.LoginSharedPref;
import org.cameleon.android.shared.query.response.LoginFormResponse;
import org.cameleon.android.shared.service.IServiceLogin;

import org.jsoup.helper.StringUtil;

import java.util.Map;

public class FBServiceLogin extends AbstractFBService implements IServiceLogin {

    private static final String TAG = FBServiceLogin.class.getName();

    private FBServiceLogin(Context context) {
        super(context);
        initializeProxy(this);
    }

    public static FBServiceLogin newInstance(Context context) {
        return new FBServiceLogin(context);
    }

    @Override
    public LoginFormResponse getLoginForm(String login, String password) {
        logMethod("getLoginForm");
        LoginFormResponse ret = null;
        System.out.println("\r\n" + URL_ROOT);

        ResponseHttp respRoot = doGet(URL_ROOT);

        if (!StringUtil.isBlank(respRoot.body)) {
            ret = FormLoginParser.getInstance().parseForm(respRoot.body, new FBLoginFormRequest());
            ret.login.value = login;
            ret.password.value = password;
        }

        return ret;
    }

    @Override
    public ResponseHttp submitFormLogin(LoginFormResponse form) {
        logMethod("submitFormLogin");
        ResponseHttp ret = null;

        System.out.println("\r\n==============> FB Form Login Action:" + form.action);

        Map<String, String> data = LoginFormResponseConverter.toDataMap(form);

        if (!StringUtil.isBlank(form.action)) {
            ret = doPost(URL_ROOT, form.action, data);

            String cookie = NetworkTool.getInstance().buildCookie(ret);
            if (!cookie.isEmpty()) {
                LoginSharedPref.setCookie(context, cookie);
                LoginSharedPref.setHomePage(context, form.action);
            } else {
                LoginSharedPref.cleanSecurity(context);
            }
        } else {
            LoginSharedPref.cleanSecurity(context);
        }

        return ret;
    }

//    public ResponseHttp navigateToFormRedirect(ResponseHttp loginFormResponse) throws NotConnectedException {
//        logMethod("navigateToFormRedirect");
//        if (loginFormResponse.pathRedirect != null && !loginFormResponse.pathRedirect.isEmpty()) {
//            ResponseHttp responseHttp = doGetConnected(URL_ROOT, loginFormResponse.pathRedirect, loginFormResponse);
//            if (NetworkTool.getInstance().isOk(responseHttp.statusCode)) {
//                LoginSharedPref.setHomePage(context, responseHttp.pathRedirect);
//            } else {
//                LoginSharedPref.cleanSecurity(context);
//            }
//            return responseHttp;
//        }
//        return null;
//    }
}
