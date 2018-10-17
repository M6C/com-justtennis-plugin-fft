package com.justtennis.plugin.fft.service;

import android.content.Context;

import com.justtennis.plugin.fft.network.HttpPostProxy;

public abstract class AbstractFFTService extends AbstractService {

    private static final String TAG = AbstractFFTService.class.getName();

    static final String URL_ROOT = "https://mon-espace-tennis.fft.fr";
    private static final String LOGON_SITE = "mon-espace-tennis.fft.fr";
    private static final int    LOGON_PORT = 80;
    private static final String LOGON_METHOD = "https";

    public enum PLAYER_GENRE {
        MAN("H", "Man"),
        WOMAN("F", "Woman");

        public String value;
        public String label;
        PLAYER_GENRE(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public static PLAYER_GENRE findByValue(String value) {
            for(PLAYER_GENRE playerSex : PLAYER_GENRE.values()) {
                if (value.equalsIgnoreCase(playerSex.value)) {
                    return playerSex;
                }
            }
            return PLAYER_GENRE.MAN;
        }

        public static PLAYER_GENRE findByLabel(String label) {
            for(PLAYER_GENRE playerSex : PLAYER_GENRE.values()) {
                if (label.equalsIgnoreCase(playerSex.label)) {
                    return playerSex;
                }
            }
            return PLAYER_GENRE.MAN;
        }
    }

    AbstractFFTService(Context context) {
        super(context);
    }

    @Override
    HttpPostProxy newHttpPostProxy() {
        HttpPostProxy instance = super.newHttpPostProxy();
        instance.setSite(LOGON_SITE)
                .setPort(LOGON_PORT)
                .setMethod(LOGON_METHOD);
        return instance;
    }
}
