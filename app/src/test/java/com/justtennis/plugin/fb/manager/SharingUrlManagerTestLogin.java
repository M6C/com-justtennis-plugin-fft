package com.justtennis.plugin.fb.manager;

import com.justtennis.plugin.shared.network.tool.NetworkTool;
import com.justtennis.plugin.shared.service.AbstractServiceTest;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SharingUrlManagerTestLogin extends AbstractServiceTest {

    public void testCheck() {
        String[] url = new String[]{
            "https://fr.wikipedia.org/wiki/Logo_TV",
            "https://www.google.fr",
            "https://www.google.fr/search?q=logo&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj82P2t7ZffAhVRI1AKHTMkAeEQ_AUIDigB&biw=1333&bih=752#imgrc=ZQyoA-cRwjPpLM:"
        };
        for(String u : url) {
            System.out.println();
            assertTrue(SharingUrlManager.getInstance().log(true).check(u));
        }
    }

    public void testCheckContentType() {
        String[] url = new String[]{
//                "https://fr.wikipedia.org/wiki/Logo_TV",
//                "https://commons.wikimedia.org/wiki/File:Logo_TV_2015.png?uselang=fr",
//                "https://www.google.fr",
//                "https://www.google.fr/search?q=logo&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj82P2t7ZffAhVRI1AKHTMkAeEQ_AUIDigB&biw=1333&bih=752#imgrc=ZQyoA-cRwjPpLM:",
//                "https://www.youtube.com/watch?v=X0tjziAQfNQ",
                "https://www.classesandworkshops.com/wp-content/uploads/2013/02/LOOMIS-HEAD-34.jpg",
                "data:image/jpeg;base64,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"
        };
        for(String u : url) {
            try {
                OkHttpClient.Builder client = new OkHttpClient.Builder();
                AbstractServiceTest.initializeProxy(client);
                Request request = new Request.Builder()
                        .url(u)
                        .build();

                Call call = client.build().newCall(request);
                Response response = call.execute();
                response.close();
                MediaType mediaType = getContentType(response.headers());
                if ((mediaType!=null)) {
                    System.out.printf("type:%s subType:%s toString:%s url:%s%n", mediaType.type(), mediaType.subtype(), mediaType.toString(), u);
                } else {
                    System.err.printf("NULL url:%s%n", u);
                }
                showRequestHeaders(response.headers());
                System.out.println("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private MediaType getContentType(Headers headers) {
        List<String> list = headers.values("Content-Type");
        String rawContentType = (list != null && list.size() > 0) ? list.get(0) : null;
        return (rawContentType != null && !rawContentType.isEmpty()) ? MediaType.parse(rawContentType) : null;
    }

    private void showRequestHeaders(Headers headers) {
        int size = headers.size();
        for (int i=0 ; i<size ; i++) {
            System.out.println("- " + headers.name(i) + ":" + headers.value(i));
        }
    }
}