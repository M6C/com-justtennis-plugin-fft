package com.justtennis.plugin.yout.component.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.justtennis.plugin.common.MainActivity;
import com.justtennis.plugin.eytm.service.EasyYouTMp3Service;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.generic.query.response.GenericResponse;
import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yout.dto.VideoDto;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

public class DownloadComponentService extends JobIntentService {

    public static final String PARAM_LIST_DTO = "list_dto";
    public static final int RSS_JOB_ID = 1000;
    private EasyYouTMp3Service service;

    public DownloadComponentService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = newEasyYouTMp3Service(getBaseContext());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Gets data from the incoming Intent
        Serializable data = intent.getSerializableExtra(PARAM_LIST_DTO);
        if (data != null && data instanceof List<?>) {
            List<VideoDto> listDto = (List<VideoDto>) data;

            int cnt = 0;
            for(VideoDto dto : listDto) {
                cnt += dto.checked ? 1 : 0;
            }

            int i = 1;
            for(VideoDto dto : listDto) {
                if (dto.checked) {
                    boolean success = false;
                    int id = 0;
                    dto.downloadStatus = VideoDto.STATUS_DOWNLOAD.DOWNLOADING;
                    notifyProgress(id, getString(R.string.app_name), MessageFormat.format(getString(R.string.yout_notification_downloading), i++, cnt, dto.title));
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
                        notifyResume(id);
                    }
                }
            }
            notifyProgress(0, getString(R.string.app_name), MessageFormat.format(getString(R.string.yout_notification_downloading_finished), cnt));
        }
    }

    public static void start(Context context, List<VideoDto> listDto) {
        Intent mServiceIntent = new Intent();
        mServiceIntent.putExtra(DownloadComponentService.PARAM_LIST_DTO, (Serializable) listDto);
        enqueueWork(context, DownloadComponentService.class, RSS_JOB_ID, mServiceIntent);
    }

    private void publishProgress(String s) {
    }

    private void notifyResume(int id) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.cancel(id);
    }

    private void notifyProgress(int id, String title, String subject) {
        // prepare intent which is triggered if the
        // notification is selected

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        File expectedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setDataAndType(Uri.fromFile(expectedFilePath), "*/*");
//        intent = Intent.createChooser(intent, "Select a File to Upload");
//        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
//        intent.setType("*/*");
//        intent.setDataAndType(Uri.fromFile(expectedFilePath), "*/*");

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(subject)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_menu_camera, "Call", pIntent)
                .addAction(R.drawable.ic_menu_share, "More", pIntent)
                .addAction(R.drawable.ic_menu_send, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(id, n);
    }

    private EasyYouTMp3Service newEasyYouTMp3Service(Context context) {
        return EasyYouTMp3Service.newInstance(context);
    }
}
