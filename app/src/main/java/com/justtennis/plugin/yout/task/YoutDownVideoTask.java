package com.justtennis.plugin.yout.task;

import android.content.Context;
import android.os.AsyncTask;

import com.justtennis.plugin.yout.component.service.DownloadComponentService;
import com.justtennis.plugin.yout.dto.VideoDto;

import java.util.List;

public abstract class YoutDownVideoTask extends AsyncTask<Void, String, List<VideoDto>> {

    private static final String TAG = YoutDownVideoTask.class.getName();

    private Context context;
    private List<VideoDto> listDto;
//    private EasyYouTMp3Service service;

    protected YoutDownVideoTask(Context context, List<VideoDto> listDto) {
        this.context = context;
        this.listDto = listDto;
//        service = newEasyYouTMp3Service(context);
    }

    @Override
    protected List<VideoDto> doInBackground(Void... params) {
//        List<VideoDto> ret = new ArrayList<>();
//        for(VideoDto dto : listDto) {
//            if (dto.checked) {
//                boolean success = false;
//                ret.add(dto);
//                dto.downloadStatus = VideoDto.STATUS_DOWNLOAD.DOWNLOADING;
//                try {
//                    this.publishProgress("Info - Navigate to Video '" + dto.title + "'");
//                    ResponseHttp response = service.navigateToVideo(dto.id);
//
//                    if (response != null && response.body != null && response.statusCode == 200) {
//                        this.publishProgress("Successfull - Navigate to Video '" + dto.title + "' so Parsing Video");
//
//                        GenericResponse find = service.parseFind(response);
//                        if (find != null) {
//                            dto.downloadPath = service.downloadLink(find, dto.title + ".mp3");
//                            this.publishProgress("Successfull - Download '" + dto.title + "'");
//                        } else {
//                            this.publishProgress("Failed - Parsing Video '" + dto.title + "'");
//                        }
//                    } else {
//                        this.publishProgress("Failed - Navigate to HomePage '" + dto.title + "'");
//                        break;
//                    }
//                } finally {
//                    dto.downloadStatus = success ? VideoDto.STATUS_DOWNLOAD.DOWNLOADED : VideoDto.STATUS_DOWNLOAD.DOWNLOAD_ERROR;
//                }
//            }
//        }
//        return ret;

        DownloadComponentService.start(context, listDto);
        return listDto;
    }
//
//    private EasyYouTMp3Service newEasyYouTMp3Service(Context context) {
//        return EasyYouTMp3Service.newInstance(context);
//    }
}
