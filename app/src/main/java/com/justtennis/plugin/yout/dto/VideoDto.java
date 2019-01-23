package com.justtennis.plugin.yout.dto;

import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.justtennis.plugin.yout.viewholder.VideoViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Publication resource
 * used by 'Publication' feature
 */
public class VideoDto implements Serializable {

    public enum STATUS_DOWNLOAD {
        NO,
        PENDING,
        DOWNLOADING,
        DOWNLOADED,
        DOWNLOAD_ERROR
    }

    public boolean checked;
    public final String id;
    public final String url;
    public final String title;
    public final String subTitle;
    public String length;
    public String publishedTime;
    public MEDIA_TYPE type;
    public List<String> thumbnails = new ArrayList<>();

    public STATUS_DOWNLOAD downloadStatus = STATUS_DOWNLOAD.NO;
    public String downloadPath;

    public VideoViewHolder viewHolder;

    public VideoDto(String id, String url, String title, String subTitle, String length, String publishedTime, MEDIA_TYPE type) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.subTitle = subTitle;
        this.length = length;
        this.publishedTime = publishedTime;
        this.type = type;
    }

    public void updateDownloadStatus(STATUS_DOWNLOAD status) {
        downloadStatus = status;
        viewHolder.updateDownloadStatus(status);
    }

    @Override
    public String toString() {
        return "VideoDto{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", length='" + length + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                ", type=" + type +
                '}';
    }
}
