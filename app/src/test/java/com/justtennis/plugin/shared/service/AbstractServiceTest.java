package org.cameleon.android.shared.service;

import android.support.annotation.NonNull;

import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.skeleton.IProxy;
import org.cameleon.android.shared.tool.FileUtil;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

public abstract class AbstractServiceTest extends TestCase {

    private static final String PROXY_USER = "pckh146";
    private static final String PROXY_PW = "k5F+n7S!";
    private static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    private static final int PROXY_PORT = 8080;

    private static final boolean useProxy = true;

    protected void initializeProxy(IProxy instance) {
        if (useProxy && instance != null) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
    }

    public static void initializeProxy(OkHttpClient.Builder clientBuilder) {
        if (useProxy) {
            clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)));

            Authenticator proxyAuthenticator = (route, response) -> {
                String credential = Credentials.basic(PROXY_USER, PROXY_PW);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            };
            clientBuilder.proxyAuthenticator(proxyAuthenticator);
        }
    }

    protected void writeResourceFile(@NonNull String text, String filename) {
        try {
            FileUtil.writeResourceFile(getClass().getClassLoader(), text, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readResourceFile(String filename) {
        StringBuffer ret = new StringBuffer();
        BufferedReader br = null;
        try {
            URL resource = getClass().getClassLoader().getResource(".");
            String expectedFilePath = resource.getFile();
            File expected = new File(expectedFilePath, filename);
            System.err.println("==========> readResourceFile:" + expected.getAbsolutePath());
            br = new BufferedReader(new InputStreamReader(new FileInputStream(expected)));
            String line;
            while((line = br.readLine()) != null) {
                ret.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret.toString();
    }

    protected ResponseHttp responseHttpFromResourceFile(String filename) {
        ResponseHttp ret = new ResponseHttp();
        ret.body = readResourceFile(filename);
        return ret;
    }
}