package com.justtennis.plugin.fb.manager;

import org.cameleon.android.shared.service.AbstractServiceTest;

import java.util.Map;

public class SharingImageManagerTest extends AbstractServiceTest {

    public void testGetData() {
        String url = "my_url";
        String title = "my_title";
        Map<String, String> data = SharingImageManager.getInstance().log(true).getData(url, title);

        assertEquals(url, data.get("attachment[params][url]"));
        assertEquals(title, data.get("attachment[params][title]"));
    }
}