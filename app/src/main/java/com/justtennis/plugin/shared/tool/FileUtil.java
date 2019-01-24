package com.justtennis.plugin.shared.tool;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

public class FileUtil {
    private FileUtil() {}

    public static void writeResourceFile(ClassLoader classLoader, @NonNull String text, String filename) throws FileNotFoundException {
        URL resource = classLoader.getResource(".");
        if (resource != null) {
//            String expectedFilePath = "/storage/emulated/0/Download";
            String expectedFilePath = resource.getFile();
            File expected = new File(expectedFilePath, filename);
            writeResourceFile(classLoader, text, expected);
        }
    }

    public static void writeDownloadFile(ClassLoader classLoader, @NonNull String text, String filename) throws FileNotFoundException {
        File expectedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File expected = new File(expectedFilePath, filename);
        writeResourceFile(classLoader, text, expected);
    }

    public static void writeResourceFile(ClassLoader classLoader, @NonNull String text, File expected) throws FileNotFoundException {
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

    public static String writeBinaryFile(ClassLoader classLoader, @NonNull InputStream in, String filename) throws IOException {
        File expectedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File expected = new File(expectedFilePath, filename);

        OutputStream out = null;
        byte[] buffer = new byte[16384];

        try {
            out = new BufferedOutputStream(new FileOutputStream(expected));

            while((in.read(buffer)) != -1)
                out.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.flush();
                out.close();
            }
        }
        return expected.getAbsolutePath();
    }
}
