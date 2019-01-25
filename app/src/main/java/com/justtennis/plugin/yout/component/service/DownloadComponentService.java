package com.justtennis.plugin.yout.component.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.justtennis.plugin.eytm.service.EasyYouTMp3Service;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yout.dto.VideoDto;

import java.io.Serializable;
import java.util.List;

public class DownloadComponentService extends JobIntentService {

    public static final String PARAM_LIST_DTO = "list_dto";
    public static final int RSS_JOB_ID = 1000;
    private final EasyYouTMp3Service service;

    public DownloadComponentService() {
        super();
        service = newEasyYouTMp3Service(this);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Gets data from the incoming Intent
        Serializable data = intent.getSerializableExtra(PARAM_LIST_DTO);
        if (data != null && data instanceof List<?>) {
            List<VideoDto> listDto = (List<VideoDto>) data;

            for(VideoDto dto : listDto) {
                if (dto.checked) {
                    boolean success = false;
                    dto.downloadStatus = VideoDto.STATUS_DOWNLOAD.DOWNLOADING;
                    try {
                        this.publishProgress("Info - Navigate to Video '" + dto.title + "'");
                        ResponseHttp response = service.navigateToVideo(dto.id);

                        if (response != null && response.body != null && response.statusCode == 200) {
                            this.publishProgress("Successfull - Navigate to Video '" + dto.title + "' so Parsing Video");

                            GenericResponse find = service.parseFind(response);
                            if (find != null) {
                                dto.downloadPath = service.downloadLink(find, dto.title + ".mp3");
                                this.publishProgress("Successfull - Download '" + dto.title + "'");
                            } else {
                                this.publishProgress("Failed - Parsing Video '" + dto.title + "'");
                            }
                        } else {
                            this.publishProgress("Failed - Navigate to HomePage '" + dto.title + "'");
                            break;
                        }
                    } finally {
                        dto.downloadStatus = success ? VideoDto.STATUS_DOWNLOAD.DOWNLOADED : VideoDto.STATUS_DOWNLOAD.DOWNLOAD_ERROR;
                    }
                }
            }
        }
    }

    public static void start(Context context, List<VideoDto> listDto) {
        Intent mServiceIntent = new Intent();
        mServiceIntent.putExtra(DownloadComponentService.PARAM_LIST_DTO, (Serializable) listDto);
        enqueueWork(context, DownloadComponentService.class, RSS_JOB_ID, mServiceIntent);
    }

    private void publishProgress(String s) {
    }

    private EasyYouTMp3Service newEasyYouTMp3Service(Context context) {
        return EasyYouTMp3Service.newInstance(context);
    }
}
