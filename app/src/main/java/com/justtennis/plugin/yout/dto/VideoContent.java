package com.justtennis.plugin.yout.dto;

import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;

import java.util.ArrayList;
import java.util.List;

public class VideoContent {

    private VideoContent() {}

    public static List<VideoDto> toDto(List<YoutFindVideoResponse.VideoItem> list) {
        List<VideoDto> ret = new ArrayList<>();
        long i=1;
        for(YoutFindVideoResponse.VideoItem item : list) {
            ret.add(createDto(item));
        }
        return ret;
    }

    private static VideoDto createDto(YoutFindVideoResponse.VideoItem item) {
        VideoDto dto = new VideoDto(item.getId(), item.url, item.title, item.subTitle, item.length, item.publishedTime, item.getType());
        dto.thumbnails.addAll(item.thumbnails);
        return dto;
    }

    private static void logMe(Exception e) {
        e.printStackTrace();
    }
}
