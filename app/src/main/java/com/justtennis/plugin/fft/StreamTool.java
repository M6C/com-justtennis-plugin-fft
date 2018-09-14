package com.justtennis.plugin.fft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamTool {

    private StreamTool() {}

    public static String readStream(InputStream inputStream) throws IOException {
        BufferedReader in = null;
        try {
//            System.out.println("StreamTool readStream => START");
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                a.append(inputLine);
//                System.out.println(inputLine);
            }
            in.close();
//            System.out.println("StreamTool readStream => END");
            return a.toString();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
