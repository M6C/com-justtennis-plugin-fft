package com.justtennis.plugin.fft.dto;

public class MatchDto {
    public boolean selected;
    public final String id;
    public final String date;
    public final String name;
    public final String vicDef;
    public final String ranking;
    public final String score;
    public final String points;
    public final String wo;

    public MatchDto(String id, String date, String name, String vicDef, String ranking, String score, String points, String wo) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.vicDef = vicDef;
        this.ranking = ranking;
        this.score = score;
        this.points = points;
        this.wo = wo;
    }

    @Override
    public String toString() {
        return "MatchDto{" +
                "selected=" + selected +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", vicDef='" + vicDef + '\'' +
                ", ranking='" + ranking + '\'' +
                ", score='" + score + '\'' +
                ", points='" + points + '\'' +
                ", wo='" + wo + '\'' +
                '}';
    }
}
