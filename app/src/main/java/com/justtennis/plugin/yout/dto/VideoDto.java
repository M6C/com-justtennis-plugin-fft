package com.justtennis.plugin.yout.dto;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.justtennis.plugin.yout.viewholder.VideoViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public transient VideoViewHolder viewHolder;

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
                "checked=" + checked +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", length='" + length + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                ", type=" + type +
                ", thumbnails=" + thumbnails +
                ", downloadStatus=" + downloadStatus +
                ", downloadPath='" + downloadPath + '\'' +
                ", viewHolder=" + viewHolder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoDto videoDto = (VideoDto) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(id, videoDto.id);
        } else {
            return videoDto.id.equals(id);
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(id);
        } else {
            return Arrays.hashCode(new String[]{id});
        }
    }
}
