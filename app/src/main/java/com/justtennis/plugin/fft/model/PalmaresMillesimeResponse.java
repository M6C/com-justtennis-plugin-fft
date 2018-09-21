package com.justtennis.plugin.fft.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PalmaresMillesimeResponse {

    public String action;
    public List<Millesime> listMillesime = new ArrayList<>();
    public Millesime millesimeSelected;
    public Map<String, String> input = new HashMap<>();
    public FormElement select;

    public static class Millesime {
        public String value;
        public String text;

        public Millesime(String value, String text) {
            this.value = value;
            this.text = text;
        }

        @Override
        public String toString() {
            return "Millesime{" +
                    "value='" + value + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Millesime millesime = (Millesime) o;
            return Objects.equals(value, millesime.value) &&
                    Objects.equals(text, millesime.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, text);
        }
    }
}