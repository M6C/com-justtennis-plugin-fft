package com.justtennis.plugin.fft.dto;

public class MatchDto {
    public boolean selected;
    public final String id;
    public final String content;
    public final String details;

    public MatchDto(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
