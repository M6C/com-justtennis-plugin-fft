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

    protected YoutDownVideoTask(Context context, List<VideoDto> listDto) {
        this.context = context;
        this.listDto = listDto;
    }

    @Override
    protected List<VideoDto> doInBackground(Void... params) {
        DownloadComponentService.start(context, listDto);
        return listDto;
    }
}
