package com.justtennis.plugin.yt.manager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YoutubeManagerTest {

    @Test
    public void getIdFromUrl() {
        String urlId = "TQjf8_S6Sho";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://youtu.be/"+urlId};
        for(String u : url) {
            String youtubeId = YoutubeManager.getInstance(true).getIdFromUrl(u);

            assertNotNull(u, youtubeId);
            assertEquals(u, urlId, youtubeId);
        }
    }

    @Test
    public void cleanUrl() {
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

        assertEquals(messageD + messageF, YoutubeManager.getInstance(true).cleanUrl(message.toString()));
    }
}