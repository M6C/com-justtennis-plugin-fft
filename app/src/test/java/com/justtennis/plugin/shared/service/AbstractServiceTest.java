package com.justtennis.plugin.shared.service;

import android.support.annotation.NonNull;

import com.justtennis.plugin.shared.query.response.LoginFormResponse;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public abstract class AbstractServiceTest extends TestCase {

    protected static final String PROXY_USER = "pckh146";
    protected static final String PROXY_PW = "k5F+n7S!";
    protected static final String PROXY_HOST = "proxy-internet.net-courrier.extra.laposte.fr";
    protected static final int PROXY_PORT = 8080;

    private static final boolean useProxy = true;

    protected abstract String getPaswd();
    protected abstract String getLogin();


    protected abstract LoginFormResponse getLoginForm();
    protected abstract ResponseHttp doLogin();
    protected abstract void initializeFFTService();

    protected void testLogin(LoginFormResponse response) {
        assertEquals(getLogin(), response.login.value);
        assertEquals(getPaswd(), response.password.value);
    }

    protected void initializeProxy(IProxy instance) {
        if (useProxy && instance != null) {
            instance.setProxyHost(PROXY_HOST)
                    .setProxyPort(PROXY_PORT)
                    .setProxyUser(PROXY_USER)
                    .setProxyPw(PROXY_PW);
        }
    }

    protected void writeResourceFile(@NonNull String text, String filename) {
        try {
            URL resource = getClass().getClassLoader().getResource(".");
            String expectedFilePath = resource.getFile();
            File expected = new File(expectedFilePath, filename);
            System.err.println("==========> writeResourceFile:" + expected.getAbsolutePath());
            PrintWriter pw = new PrintWriter(expected);
            pw.print(text);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String readResourceFile(String filename) {
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