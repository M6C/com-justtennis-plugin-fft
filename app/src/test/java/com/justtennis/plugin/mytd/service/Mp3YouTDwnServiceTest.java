package com.justtennis.plugin.mytd.service;

import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.FormElement;
import com.justtennis.plugin.shared.skeleton.IProxy;
import com.justtennis.plugin.yout.query.request.YouTFindVideoFormRequest;

import org.junit.Test;

import java.util.Map;

public class Mp3YouTDwnServiceTest extends AbstractMp3YouTDwnServiceTest {

    private Mp3YouTDwnService mp3YouTDwnService;

    @Override
    IProxy initializeService() {
        mp3YouTDwnService = Mp3YouTDwnService.newInstance(null);
        return mp3YouTDwnService;
    }

    @Test
    public void testFindForm() {
        ResponseHttp form = doLogin();
        writeResourceFile(form.body, "Mp3YouTDwnServiceTest_testFindForm.html");

        GenericFormResponse response = mp3YouTDwnService.getFindForm(form, "https://youtu.be/V0gd52_3EUU");
        assertNotNull(response);
        assertNotNull(response.action);
        Map<String, FormElement> fieldValue = response.fieldValue;
        for(String key : fieldValue.keySet()) {
            FormElement val = fieldValue.get(key);
            assertNotNull(val.name);
        }
        assertNotNull(fieldValue.get(YouTFindVideoFormRequest.KEY_FIELD_SEARCH).value);
    }

    @Test
    public void testSubmitFindForm() throws NotConnectedException {
        ResponseHttp form = doLogin();

        GenericFormResponse response = mp3YouTDwnService.getFindForm(form, "https://youtu.be/V0gd52_3EUU");

        assertNotNull(response);

        ResponseHttp submitForm = mp3YouTDwnService.submitFindForm(form, response);
        writeResourceFile(submitForm.body, "Mp3YouTDwnServiceTest_testSubmitFindForm.html");
        assertNotNull(submitForm.body);

        GenericResponse find = mp3YouTDwnService.parseFind(submitForm);
        assertNotNull(find);
        assertTrue("Donwload List must be not empty", !find.data.isEmpty());
        for (GenericResponse.Item item : find.data) {
            for (String key : item.itemValue.keySet()) {
                assertNotNull(item.itemValue.get(key));
            }
        }

        mp3YouTDwnService.downloadLink(form, find);
    }
}