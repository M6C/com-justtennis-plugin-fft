package com.justtennis.plugin.fft.dto;

import java.io.Serializable;

public class MatchDto implements Serializable {
    public boolean selected;
    public final String id;
    public final String date;
    public final String name;
    public final String vicDef;
    public final String ranking;
    public final String score;
    public final String points;
    public final String wo;
    public final String linkPalmares;

    public MatchDto(String id, String date, String name, String vicDef, String ranking, String score, String points, String wo, String linkPalmares) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.vicDef = vicDef;
        this.ranking = ranking;
        this.score = score;
        this.points = points;
        this.wo = wo;
        this.linkPalmares = linkPalmares;
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
                ", linkPalmares='" + linkPalmares + '\'' +
                '}';
    }
}
