package com.justtennis.plugin.yt.manager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YoutubeManagerTest {

    @Test
    public void getIdFromUrl() {
        String urlId = "mKG8BR292oo";
        String[] url = new String[]{
                "https://www.youtube.com/watch?v="+urlId+"&fbclid=IwAR3bgfViD0sj9x8HqNH5A1m3HBx_WoCr6LJ6142JfQFDifKATmztQAJgG1A",
                "https://www.youtu.be/"+urlId};
        for(String u : url) {
            String youtubeId = YoutubeManager.getInstance(true).getIdFromUrl(u);

            assertNotNull(u, urlId);
            assertEquals(u, urlId, youtubeId);
        }
    }
}