package com.justtennis.plugin.fft.service;

import android.content.Context;
import android.util.Log;

import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.exception.NotConnectedException;
import com.justtennis.plugin.fft.manager.InviteManager;
import com.justtennis.plugin.fft.network.HttpGetProxy;
import com.justtennis.plugin.fft.network.HttpPostProxy;
import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.preference.FFTSharedPref;
import com.justtennis.plugin.fft.preference.ProxySharedPref;
import com.justtennis.plugin.fft.skeleton.IProxy;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public abstract class AbstracFFTService implements IProxy {

    private static final String TAG = AbstracFFTService.class.getName();

    protected static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    protected static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    protected static final int    LOGON_PORT = 80;
    protected static final String LOGON_METHOD = "https";

    protected Context context;
    protected String proxyHost;
    protected int    proxyPort;
    protected String proxyUser;
    protected String proxyPw;

    public enum PLAYER_SEX {
        MAN("H", "Man"),
        WOMAN("F", "Woman");

        public String value;
        public String label;
        PLAYER_SEX(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public static PLAYER_SEX findByValue(String value) {
            for(PLAYER_SEX playerSex : PLAYER_SEX.values()) {
                if (value.equalsIgnoreCase(playerSex.value)) {
                    return playerSex;
                }
            }
            return PLAYER_SEX.MAN;
        }

        public static PLAYER_SEX findByLabel(String label) {
            for(PLAYER_SEX playerSex : PLAYER_SEX.values()) {
                if (label.equalsIgnoreCase(playerSex.label)) {
                    return playerSex;
                }
            }
            return PLAYER_SEX.MAN;
        }
    }

    AbstracFFTService(Context context) {
        this.context = context;
    }

    void initializeProxy(IProxy service) {
        if (ProxySharedPref.getUseProxy(context)) {
            service.setProxyHost(ProxySharedPref.getSite(context))
                    .setProxyPort(ProxySharedPref.getPort(context))
                    .setProxyUser(ProxySharedPref.getUser(context))
                    .setProxyPw(ProxySharedPref.getPwd(context));
        }
    }

    String format(String str) {
        String ret = decode(str);
        ret = ret.replaceAll("\\\\n", "");
        ret = ret.replaceAll("\\\\/", "/");
        return ret;
    }

    String decode(final String in) {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1) {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }

    HttpGetProxy newHttpGetProxy() {
        HttpGetProxy instance = HttpGetProxy.newInstance();
        setProxy(instance);
        return instance;
    }

    ResponseHttp doGet(String root, String path) {
        return newHttpGetProxy().get(root, path);
    }

    ResponseHttp doPost(String root, String path, Map<String, String> data) {
        return newHttpPostProxy().post(root, path, data);
    }

    ResponseHttp doGetConnected(String root, String path, ResponseHttp http) throws NotConnectedException {
        return doGetConnected(root, path, null, http);
    }

    ResponseHttp doGetConnected(String root, String path, Map<String, String> data, ResponseHttp http) throws NotConnectedException {
        String cookie = FFTSharedPref.getCookie(context);
        if (cookie == null && http == null) {
            throw new NotConnectedException();
        } else if (cookie != null) {
            return newHttpGetProxy().get(root, path, data, cookie);
        } else {
            return newHttpGetProxy().get(root, path, data, http);
        }
    }

    ResponseHttp doPostConnected(String root, String path, Map<String, String> data, ResponseHttp http) throws NotConnectedException {
        String cookie = FFTSharedPref.getCookie(context);
        if (cookie == null && http == null) {
            throw new NotConnectedException();
        } else if (cookie != null) {
            return newHttpPostProxy().post(root, path, data, cookie);
        } else {
            return newHttpPostProxy().post(root, path, data, http);
        }
    }

    HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = HttpPostProxy.newInstance();
        setProxy(instance);
        instance.setSite(LOGON_SITE)
                .setPort(LOGON_PORT)
                .setMethod(LOGON_METHOD);
        return instance;
    }

    void setProxy(IProxy instance) {
        instance.setProxyHost(proxyHost)
                .setProxyPort(proxyPort)
                .setProxyUser(proxyUser)
                .setProxyPw(proxyPw);
    }

    @Override
    public IProxy setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    @Override
    public IProxy setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    @Override
    public IProxy setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    @Override
    public IProxy setProxyPw(String proxyPw) {
        this.proxyPw = proxyPw;
        return this;
    }


    public static Date getDateFromFFT(String date) {
        Date ret = new Date();
        try {
            ret = MatchContent.sdfFFT.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, MessageFormat.format("Formatting match.ret:{0}", date), e);
        }
        return ret;
    }

    public static InviteManager.SCORE_RESULT getScoreResultFromFFT(String vicDef) {
        return vicDef.startsWith("D") ? InviteManager.SCORE_RESULT.DEFEAT :  InviteManager.SCORE_RESULT.VICTORY;
    }

    void logMethod(String method) {
        System.out.println(
            "\r\n==========================================================================" +
            "\n==============> Method:" + method +
            "\r\n==========================================================================\r\n");

    }
}
