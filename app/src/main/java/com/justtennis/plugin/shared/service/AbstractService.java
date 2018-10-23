package com.justtennis.plugin.shared.service;

import android.content.Context;

import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.HttpGetProxy;
import com.justtennis.plugin.shared.network.HttpPostProxy;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.preference.LoginSharedPref;
import com.justtennis.plugin.shared.preference.ProxySharedPref;
import com.justtennis.plugin.shared.skeleton.IProxy;

import java.util.Map;

public abstract class AbstractService implements IProxy {

    private static final String TAG = AbstractService.class.getName();

    protected Context context;
    protected String proxyHost;
    protected int    proxyPort;
    protected String proxyUser;
    protected String proxyPw;

    protected AbstractService(Context context) {
        this.context = context;
    }

    protected void initializeProxy(IProxy service) {
        if (ProxySharedPref.getUseProxy(context)) {
            service.setProxyHost(ProxySharedPref.getSite(context))
                    .setProxyPort(ProxySharedPref.getPort(context))
                    .setProxyUser(ProxySharedPref.getUser(context))
                    .setProxyPw(ProxySharedPref.getPwd(context));
        }
    }

    protected String format(String str) {
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

    protected ResponseHttp doGet(String root) {
        return newHttpGetProxy().get(root, "");
    }

    protected ResponseHttp doPost(String root, String path, Map<String, String> data) {
        String cookie = "reg_ext_ref=https%3A%2F%2Fwww.google.fr%2F; reg_fb_ref=https%3A%2F%2Ffr-fr.facebook.com%2F; reg_fb_gate=https%3A%2F%2Ffr-fr.facebook.com%2F";
        return newHttpPostProxy().post(root, path, data, cookie);
    }

    protected ResponseHttp doGetConnected(String root, String path, ResponseHttp http) throws NotConnectedException {
        return doGetConnected(root, path, null, http);
    }

    protected ResponseHttp doGetConnected(String root, String path, Map<String, String> data, ResponseHttp http) throws NotConnectedException {
        String cookie = LoginSharedPref.getCookie(context);
        if (cookie == null && http == null) {
            throw new NotConnectedException();
        } else if (cookie != null) {
            return newHttpGetProxy().get(root, path, data, cookie);
        } else {
            return newHttpGetProxy().get(root, path, data, http);
        }
    }

    protected ResponseHttp doPostConnected(String root, String path, Map<String, String> data, ResponseHttp http) throws NotConnectedException {
        String cookie = LoginSharedPref.getCookie(context);
        if (cookie == null && http == null) {
            throw new NotConnectedException();
        } else if (cookie != null) {
            return newHttpPostProxy().post(root, path, data, cookie);
        } else {
            return newHttpPostProxy().post(root, path, data, http);
        }
    }

    protected HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = HttpPostProxy.newInstance();
        setProxy(instance);
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

    protected void logMethod(String method) {
        System.out.println(
            "\r\n==========================================================================" +
            "\n==============> Method:" + method +
            "\r\n==========================================================================\r\n");

    }
}
