package com.justtennis.plugin.fft.model.enums;

public class EnumPlayer {

    private EnumPlayer() {
    }

    public enum GENRE {
        MAN("H", "Man"),
        WOMAN("F", "Woman");

        public String value;
        public String label;
        GENRE(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public static GENRE findByValue(String value) {
            for(GENRE playerGenre : GENRE.values()) {
                if (value.equalsIgnoreCase(playerGenre.value)) {
                    return playerGenre;
                }
            }
            return GENRE.MAN;
        }

        public static GENRE findByLabel(String label) {
            for(GENRE playerGenre : GENRE.values()) {
                if (label.equalsIgnoreCase(playerGenre.label)) {
                    return playerGenre;
                }
            }
            return GENRE.MAN;
        }
    }
}
