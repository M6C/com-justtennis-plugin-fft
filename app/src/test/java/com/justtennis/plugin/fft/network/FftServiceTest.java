package com.justtennis.plugin.fft.network;

import com.justtennis.plugin.fft.service.FftService;

import junit.framework.TestCase;

import java.io.IOException;

public class FftServiceTest extends TestCase {

//    public static void testConnect() throws IOException {
//        FftService.connect("", "");
//    }

    public static void testSubmitForm() throws IOException {
        FftService.submitForm("leandre.roca2006", "lR123456789");
    }
}
