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
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                a.append(inputLine);
            }
            in.close();
            return a.toString();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
