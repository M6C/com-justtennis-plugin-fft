package com.justtennis.plugin.shared.tool;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;

public class FileUtil {
    private FileUtil() {}

    public static void writeResourceFile(ClassLoader classLoader, @NonNull String text, String filename) throws FileNotFoundException {
        URL resource = classLoader.getResource(".");
        if (resource != null) {
            String expectedFilePath = resource.getFile();
            File expected = new File(expectedFilePath, filename);
            System.err.println("==========> writeResourceFile:" + expected.getAbsolutePath());
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(expected);
                pw.print(text);
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }
    }
}
