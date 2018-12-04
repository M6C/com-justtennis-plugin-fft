package com.justtennis.plugin.fb.service;

import com.justtennis.plugin.fb.query.response.FBHomePageResponse;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class FBServiceProfilEncodingTest extends AbstractFBServiceTest {

    private FBServiceHomePage fbServiceHomePage;
    private FBServiceProfil fbServiceProfil;

    @Override
    protected IProxy initializeService() {
        fbServiceHomePage = FBServiceHomePage.newInstance(null);
        initializeProxy(fbServiceHomePage);
        fbServiceProfil = FBServiceProfil.newInstance(null);
        return fbServiceProfil;
    }

    @Test
    public void testGetProfilPublication() throws NotConnectedException {
        ResponseHttp form = doLogin();

        ResponseHttp formRedirect = fbServiceHomePage.navigateToHomePage(form);

        assertNotNull(formRedirect);
        assertNotNull(formRedirect.body);

        FBHomePageResponse homePageResponse = FBServiceHomePage.getHomePage(formRedirect);
        assertNotNull(homePageResponse.linkProfil);

        ResponseHttp profileHttpResponse = fbServiceProfil.navigateToProfil(form, homePageResponse);
        writeResourceFile(profileHttpResponse.body, "FBServiceProfilEncodingTest_testGetProfilPublication_navigateToProfil.html");
        assertNotNull(profileHttpResponse.body);
        assertEquals(200, profileHttpResponse.statusCode);

        System.out.println("Test body:"+profileHttpResponse.body);

        FBProfilPublicationResponse profilPublicationResponse = fbServiceProfil.getProfilPublication(profileHttpResponse);
        assertNotNull(profilPublicationResponse);
        assertNotNull(profilPublicationResponse.timeLineList);
        assertTrue(profilPublicationResponse.timeLineList.size() > 0);

        for(FBProfilPublicationResponse.TimeLineItem item : profilPublicationResponse.timeLineList) {
            StringBuilder log = new StringBuilder("profilPublicationResponse.timeLineItem:").append(item);
            System.out.println(log);
            String text = item.text;
            char[] chars = text.toCharArray();
            for (int i=0 ; i<chars.length ; i++) {
                char c = chars[i];
                log.append("\r\n").append(i).append(" char:").append(c).append(" codePointAt:").append(text.codePointAt(i));
            }
            System.out.println(log.toString());
//            showText(text);
            showTextUnicode(text);
        }

        String text = "... 😘😘😘😘😘😘😘";
        StringBuilder log = new StringBuilder("profilPublicationResponse.timeLineItem:").append(text);
        char[] chars = text.toCharArray();
        for (int i=0 ; i<chars.length ; i++) {
            char c = chars[i];
            log.append("\r\n").append(i).append(" char:").append(c).append(" codePointAt:").append(text.codePointAt(i));
        }
        System.out.println(log.toString());
            showText(text);
//        showTextUnicode(text);
    }

    private void showText(String text) {
        String[] encodings = { "Cp1252", // Windows-1252
                "UTF-8", // Unicode UTF-8
                "UTF-16BE" // Unicode UTF-16, big endian
        };

        for (String encoding : encodings) {
            System.out.format("showText %10s%3s  ", encoding, text);
            try {
                byte[] encoded = text.getBytes(encoding);
                for (byte b : encoded) {
                    System.out.format("%02x", b);
                }
                System.out.println();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTextUnicode(String text) {
        System.out.format("showTextUnicode %3s  ", text);
        char[] encoded = text.toCharArray();
        for (char c : encoded) {
            System.out.format("%s", unicodeEscaped(c));
        }
        System.out.println();
    }
//--------------------------------------------------------------------------
    /**
     * Converts the string to the unicode format '\u0020'.
     *
     * This format is the Java source code format.
     *
     * <pre>
     *   CharUtils.unicodeEscaped(' ') = "\u0020"
     *   CharUtils.unicodeEscaped('A') = "\u0041"
     * </pre>
     *
     * @param ch  the character to convert
     * @return the escaped unicode string
     */
    public static String unicodeEscaped(char ch) {
//        if (ch < 0x10) {
//            return "\\u000" + Integer.toHexString(ch);
//        } else if (ch < 0x100) {
//            return "\\u00" + Integer.toHexString(ch);
//        } else if (ch < 0x1000) {
//            return "\\u0" + Integer.toHexString(ch);
//        }
        if (ch < 0x1000) {
            return String.valueOf(ch);
        }
//        return "\\u" + Integer.toHexString(ch);
        return "%" + ch;
    }

    /**
     * Converts the string to the unicode format '\u0020'.
     *
     * This format is the Java source code format.
     *
     * If <code>null</code> is passed in, <code>null</code> will be returned.
     *
     * <pre>
     *   CharUtils.unicodeEscaped(null) = null
     *   CharUtils.unicodeEscaped(' ')  = "\u0020"
     *   CharUtils.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch  the character to convert, may be null
     * @return the escaped unicode string, null if null input
     */
    public static String unicodeEscaped(Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }
}