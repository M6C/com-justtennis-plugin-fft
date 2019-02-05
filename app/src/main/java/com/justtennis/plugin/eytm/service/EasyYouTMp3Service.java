package com.justtennis.plugin.eytm.service;

import android.content.Context;

import com.justtennis.plugin.eytm.query.request.EasyYouTMp3Request;
import org.cameleon.android.generic.parser.GenericParser;
import org.cameleon.android.generic.query.response.GenericResponse;
import org.cameleon.android.shared.network.model.ResponseHttp;
import org.cameleon.android.shared.tool.FileUtil;

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

    public String downloadLink(GenericResponse findResponse, String title) {
        for(GenericResponse.Item item : findResponse.data) {
            for(String link : item.itemValue.values()) {
                if (link != null) {
                    ResponseHttp resp = doGet(link);
                    try {
                        return FileUtil.writeBinaryFile(getClass().getClassLoader(), resp.raw, title);
                    } catch (IOException e) {
                        logMe(e);
                    }
                }
            }
        }
        return null;
    }

    private static void logMe(Exception e) {
        e.printStackTrace();
    }
}