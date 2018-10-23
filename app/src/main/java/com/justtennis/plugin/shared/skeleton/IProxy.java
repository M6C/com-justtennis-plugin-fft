package com.justtennis.plugin.shared.skeleton;

public interface IProxy {

    IProxy setProxyHost(String proxyHost);

    IProxy setProxyPort(int proxyPort);

    IProxy setProxyUser(String proxyUser);

    IProxy setProxyPw(String proxyPw);
}
