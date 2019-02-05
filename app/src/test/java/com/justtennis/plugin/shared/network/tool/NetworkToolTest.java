package org.cameleon.android.shared.network.tool;

import junit.framework.TestCase;

import org.junit.Test;

public class NetworkToolTest extends TestCase {

    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;

    private static final boolean useProxy = true;

//    @Test
//    public static void testShowCookies() {
//        HttpClient client = new HttpClient();
//        NetworkTool.getInstance().setDoLog(true);
//        NetworkTool.getInstance().showCookies(client, LOGON_SITE, LOGON_PORT);
//    }

    @Test
    public static void testIsRedirect() {
        NetworkTool.getInstance().setDoLog(true);
        assertTrue(NetworkTool.getInstance().isRedirect(HttpStatus.SC_MOVED_TEMPORARILY));
        assertTrue(NetworkTool.getInstance().isRedirect(HttpStatus.SC_MOVED_PERMANENTLY));
        assertTrue(NetworkTool.getInstance().isRedirect(HttpStatus.SC_SEE_OTHER));
        assertTrue(NetworkTool.getInstance().isRedirect(HttpStatus.SC_TEMPORARY_REDIRECT));
        assertFalse(NetworkTool.getInstance().isRedirect(HttpStatus.SC_OK));
    }

    @Test
    public static void testIsOk() {
        NetworkTool.getInstance().setDoLog(true);
        assertTrue(NetworkTool.getInstance().isOk(HttpStatus.SC_OK));
        assertFalse(NetworkTool.getInstance().isOk(HttpStatus.SC_MOVED_PERMANENTLY));
    }
}