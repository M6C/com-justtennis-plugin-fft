package com.justtennis.plugin.eytm.service;

import android.content.Context;

import com.justtennis.plugin.eytm.query.request.EasyYouTMp3Request;
import com.justtennis.plugin.generic.parser.GenericParser;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.exception.NotConnectedException;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.tool.FileUtil;

import java.io.IOException;

public class EasyYouTMp3Service extends AbstractEasyYouTMp3Service {

    private static final String TAG = EasyYouTMp3Service.class.getName();

    private EasyYouTMp3Service(Context context) {
        super(context);
    }

    public static EasyYouTMp3Service newInstance(Context context) {
        return new EasyYouTMp3Service(context);
    }

    public ResponseHttp navigateToVideo(String id) {
        logMethod("navigateToVideo");
        return doGet(URL_ROOT + "/download.php?v=" + id);
    }

    public GenericResponse parseFind(ResponseHttp findResponseHttp) {
        GenericResponse ret = null;
        if (findResponseHttp.body != null && !findResponseHttp.body.isEmpty()) {
            ret = GenericParser.parseFindWithActivateHtml(findResponseHttp.body, new EasyYouTMp3Request());
        }
        return ret;
    }

    public void downloadLink(GenericResponse findResponse) throws NotConnectedException {
        for(GenericResponse.Item item : findResponse.data) {
            int i=0;
            for(String link : item.itemValue.values()) {
                ResponseHttp resp = doGet(link);
                try {
                    FileUtil.writeBinaryFile(getClass().getClassLoader(), resp.raw, "test"+(i++)+".mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}