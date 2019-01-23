package com.justtennis.plugin.eytm.service;

import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.skeleton.IProxy;

import org.junit.Test;

public class EasyYouTMp3ServiceTest extends AbstractEasyYouTMp3ServiceTest {

    private EasyYouTMp3Service service;

    @Override
    IProxy initializeService() {
        service = EasyYouTMp3Service.newInstance(null);
        return service;
    }

    @Test
    public void testSubmitFindForm() throws NotConnectedException {
        doLogin();

        ResponseHttp response = service.navigateToVideo("V0gd52_3EUU");
        assertNotNull(response);
        assertEquals(200, response.statusCode);
        writeResourceFile(response.body, "EasyYouTMp3ServiceTest_testSubmitFindForm.html");
        assertNotNull(response.body);
        assertNotSame("", response.body);

        GenericResponse find = service.parseFind(response);
        assertNotNull(find);
        assertTrue("Donwload List must be not empty", !find.data.isEmpty());
        for (GenericResponse.Item item : find.data) {
            for (String key : item.itemValue.keySet()) {
                assertNotNull(item.itemValue.get(key));
            }
        }

        service.downloadLink(find);
    }
}