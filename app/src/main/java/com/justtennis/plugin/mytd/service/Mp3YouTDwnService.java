package com.justtennis.plugin.mytd.service;

import android.content.Context;

import com.justtennis.plugin.generic.converter.GenericFormResponseConverter;
import com.justtennis.plugin.generic.parser.GenericFormParser;
import com.justtennis.plugin.generic.parser.GenericParser;
import com.justtennis.plugin.generic.query.response.GenericFormResponse;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.mytd.query.request.Mp3YouTDwnFormRequest;
import com.justtennis.plugin.mytd.query.request.Mp3YouTDwnRequest;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.FormElement;
import com.justtennis.plugin.shared.service.AbstractService;
import com.justtennis.plugin.shared.tool.FileUtil;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.Map;

public class Mp3YouTDwnService extends AbstractMp3YouTDwnService {

    private static final String TAG = Mp3YouTDwnService.class.getName();

    private Mp3YouTDwnService(Context context) {
        super(context);
    }

    public static Mp3YouTDwnService newInstance(Context context) {
        return new Mp3YouTDwnService(context);
    }

    public GenericFormResponse getFindForm(ResponseHttp responseHttp, String search) {
        AbstractService.logMethod("getFindForm");
        GenericFormResponse ret = null;

        if (!StringUtil.isBlank(responseHttp.body)) {
            ret = GenericFormParser.getInstance().parseForm(responseHttp.body, new Mp3YouTDwnFormRequest());
            Map<String, FormElement> fieldValue = ret.fieldValue;
            fieldValue.get(Mp3YouTDwnFormRequest.KEY_FIELD_SEARCH).value = search;
            GenericFormParser.getInstance().showResponse(ret);
        }

        return ret;
    }

    public ResponseHttp submitFindForm(ResponseHttp form, GenericFormResponse response) throws NotConnectedException {
        Map<String, String> data = GenericFormResponseConverter.toDataMap(response);
        String action = URL_ROOT + response.fieldValue.get(Mp3YouTDwnFormRequest.KEY_FIELD_SEARCH).value;
        return doPostConnected(URL_ROOT, action, data, form);
    }

    public GenericResponse parseFind(ResponseHttp findResponseHttp) {
        GenericResponse ret = null;
        if (findResponseHttp.body != null && !findResponseHttp.body.isEmpty()) {
            ret = GenericParser.parseFindWithActivateHtml(findResponseHttp.body, new Mp3YouTDwnRequest());
        }
        return ret;
    }

    public void downloadLink(ResponseHttp form, GenericResponse findResponse) throws NotConnectedException {
        for(GenericResponse.Item item : findResponse.data) {
            int i=0;
            for(String link : item.itemValue.values()) {
//                link = "https://savetomp3.cc/fr/@api/json/mp3/V0gd52_3EUU";
                ResponseHttp resp = doGetConnected(formatLink(link), "", form);
                try {
                    FileUtil.writeResourceFile(getClass().getClassLoader(), resp.body, "test"+(i++)+".mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String formatLink(String link) {
        String ret = link;
        String low = link.toLowerCase();
        if (!low.startsWith("http") && !low.startsWith("https")) {
            ret = "https";
            if (!low.startsWith("://")) {
               ret += (low.startsWith("//")) ? ":" : "://";
            }
            ret += link;

        }
        return ret;
    }
}