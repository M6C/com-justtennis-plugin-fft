package com.justtennis.plugin.shared.network.model;

import java.util.Objects;

public class ResponseElement {

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String name;
    public String value;

    public ResponseElement() {
    }

    public ResponseElement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof ResponseElement) {
            ResponseElement that = (ResponseElement) o;
            return Objects.equals(name, that.name) && Objects.equals(value, that.value);
        } else if (o instanceof String) {
            return Objects.equals(name, o);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ResponseElement{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}