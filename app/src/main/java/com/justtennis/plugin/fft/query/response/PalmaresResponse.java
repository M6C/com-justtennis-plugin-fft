package com.justtennis.plugin.fft.query.response;

public class PalmaresResponse {

    public String action;
    public String millesime;

    public String getUrlAction() {
        String ret = action;
        if (millesime != null && !millesime.isEmpty()) {
            ret += "?millesime=" + millesime;
        }
        return ret;
    }
}