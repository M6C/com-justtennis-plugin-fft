package com.justtennis.plugin.fb.manager;

import org.cameleon.android.shared.service.AbstractServiceTest;

public class SharingYoutubeManagerTest extends AbstractServiceTest {

    public void testGetIdFromUrl() {
        String urlId = "TQjf8_S6Sho";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://youtu.be/"+urlId};
        for(String u : url) {
            String youtubeId = SharingYoutubeManager.getInstance().log(true).getIdFromUrl(u);

            assertNotNull(u, youtubeId);
            assertEquals(u, urlId, youtubeId);
        }
    }

    public void testCleanUrl() {
        String urlId = "TQjf8_S6Sho";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://youtu.be/"+urlId};
        String messageD = "mon message ";
        String messageF = " fin du message";
        StringBuilder message = new StringBuilder(messageD);
        for(String u : url) {
            message.append(u);
        }
        message.append(messageF);

        assertEquals(messageD + messageF, SharingYoutubeManager.getInstance().log(true).cleanUrl(message.toString()));
    }
}