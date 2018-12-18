package com.justtennis.plugin.fft.model.enums;

public class EnumCompetition {

    private EnumCompetition() {
    }

    public enum TYPE {
        TOURNAMENT("T", "Tournament"),
        CHAMPIONSHIP("C", "Championnat");

        public String value;
        public String label;
        TYPE(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public static TYPE findByValue(String value) {
            for(TYPE playerGenre : TYPE.values()) {
                if (value.equalsIgnoreCase(playerGenre.value)) {
                    return playerGenre;
                }
            }
            return TYPE.TOURNAMENT;
        }

        public static TYPE findByLabel(String label) {
            for(TYPE playerGenre : TYPE.values()) {
                if (label.equalsIgnoreCase(playerGenre.label)) {
                    return playerGenre;
                }
            }
            return TYPE.TOURNAMENT;
        }
    }
}
