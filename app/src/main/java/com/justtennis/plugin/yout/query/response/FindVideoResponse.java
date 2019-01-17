package com.justtennis.plugin.yout.query.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindVideoResponse {

    public List<VideoItem> videoList = new ArrayList<>();

    public static class VideoItem implements Serializable {
        public String videoId;
        public String channelId;
        public String playlistId;
        public String title;
        public String subTitle;
        public String length;
        public String publishedTime;
        public String url;
        public List<String> thumbnails = new ArrayList<>();

        public VideoItem() {
        }

        public VideoItem(String videoId, String title, String subTitle, String url) {
            this.videoId = videoId;
            this.title = title;
            this.subTitle = subTitle;
            this.url = url;
        }

        @Override
        public String toString() {
            return "VideoItem{" +
                    "id='" + videoId + '\'' +
                    ", title='" + title + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", url='" + url + '\'' +
                    ", thumbnails size=" + thumbnails.size() +
                    '}';
        }
    }
}