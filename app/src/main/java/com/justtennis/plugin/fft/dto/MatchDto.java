package com.justtennis.plugin.fft.dto;

public class MatchDto {
    public boolean selected;
    public final String id;
    public final String date;
    public final String details;
    public final String name;
    public final String vicDef;

    public MatchDto(String id, String date, String details, String name, String vicDef) {
        this.id = id;
        this.date = date;
        this.details = details;
        this.name = name;
        this.vicDef = vicDef;
    }

    @Override
    public String toString() {
        return "MatchDto{" +
                "selected=" + selected +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", details='" + details + '\'' +
                ", name='" + name + '\'' +
                ", vicDef='" + vicDef + '\'' +
                '}';
    }
}
