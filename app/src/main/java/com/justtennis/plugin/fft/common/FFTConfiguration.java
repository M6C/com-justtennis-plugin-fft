package com.justtennis.plugin.fft.common;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FFTConfiguration {

    public static SimpleDateFormat sdfFFT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    public static SimpleDateFormat sdfAjaxS = new SimpleDateFormat("'du' dd MMM yyyy", Locale.FRANCE);
    public static SimpleDateFormat sdfAjaxE = new SimpleDateFormat("'au' dd MMM yyyy", Locale.FRANCE);

    private FFTConfiguration() {
    }
}
