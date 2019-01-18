package com.justtennis.plugin.yout.query.response;

import com.justtennis.plugin.yout.enums.MEDIA_TYPE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class YoutFindVideoResponse {

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

        public String getId() {
            return videoId!=null ? videoId : channelId!=null ? channelId : playlistId;
        }

        public MEDIA_TYPE getType() {
            return videoId!=null ? MEDIA_TYPE.VIDEO : channelId!=null ? MEDIA_TYPE.CHANNEL : MEDIA_TYPE.PLAYLIST;
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