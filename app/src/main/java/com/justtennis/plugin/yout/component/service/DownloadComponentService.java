package com.justtennis.plugin.yout.component.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import org.cameleon.android.common.MainActivity;
import com.justtennis.plugin.eytm.service.EasyYouTMp3Service;
import com.justtennis.plugin.fft.R;
import org.cameleon.android.generic.query.response.GenericResponse;
import org.cameleon.android.shared.network.model.ResponseHttp;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.rxjava.RxFindVideo;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
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
        if (data instanceof List<?>) {
            List<VideoDto> listDto = (List<VideoDto>) data;

            int cnt = 0;
            for(VideoDto dto : listDto) {
                if (dto.checked) {
                    cnt++;
                    updateDownloadStatus(dto, VideoDto.STATUS_DOWNLOAD.PENDING);
                }
            }

            if (cnt==0) {
                return;
            }

            int i = 1;
            int id = (int) Calendar.getInstance().getTimeInMillis();
            VideoDto dto2 = null;
            for(VideoDto dto : listDto) {
                if (dto.checked) {
                    dto2 = dto;
                    boolean success = false;
                    updateDownloadStatus(dto, VideoDto.STATUS_DOWNLOAD.DOWNLOADING);
                    String message = cnt > 1 ? MessageFormat.format(getString(R.string.yout_notification_downloading_multi), i++, cnt, dto.title) : MessageFormat.format(getString(R.string.yout_notification_downloading), dto.title);
                    notifyProgress(id, getString(R.string.app_name), message, createIntentVlc(id+1,dto.title + ".mp3"));
                    try {
                        this.publishProgress("Info - Navigate to Video '" + dto.title + "'");
                        ResponseHttp response = service.navigateToVideo(dto.id);

                        if (response != null && response.body != null && response.statusCode == 200) {
                            this.publishProgress("Successfull - Navigate to Video '" + dto.title + "' so Parsing Video");

                            GenericResponse find = service.parseFind(response);
                            if (find != null) {
                                dto.downloadPath = service.downloadLink(find, dto.title + ".mp3");
                                this.publishProgress("Successfull - Download '" + dto.title + "'");
                                success = true;
                            } else {
                                this.publishProgress("Failed - Parsing Video '" + dto.title + "'");
                            }
                        } else {
                            this.publishProgress("Failed - Navigate to HomePage '" + dto.title + "'");
                            break;
                        }
                    } finally {
                        updateDownloadStatus(dto, success ? VideoDto.STATUS_DOWNLOAD.DOWNLOADED : VideoDto.STATUS_DOWNLOAD.DOWNLOAD_ERROR);
                        notifyResume(id);
                    }
                }
            }
            notifyProgress(id, getString(R.string.app_name), MessageFormat.format(getString(R.string.yout_notification_downloading_finished), cnt), createIntentVlc(id+1,dto2.title + ".mp3"));
        }
    }

    private void updateDownloadStatus(VideoDto dto, VideoDto.STATUS_DOWNLOAD status) {
        dto.downloadStatus = status;
        RxFindVideo.publish(RxFindVideo.SUBJECT_UPDATE_DOWNLOAD_STATUS_VIDEO, dto);
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

    private void notifyProgress(int id, String title, String subject, PendingIntent vlcIntent) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification.Builder nB = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(subject)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                ;
        if (vlcIntent != null) {
            nB.addAction(R.drawable.ic_menu_camera, "Vlc", vlcIntent);
        }
        Notification n = nB.build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(id, n);
    }

    private PendingIntent createIntentVlc(int id, String title) {
        ApplicationInfo vlcInfo = null;
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo info : list) {
            System.out.println(info.toString());
            if (info.packageName.equals("org.videolan.vlc")) {
                vlcInfo = info;
                break;
            } else if (info.packageName.startsWith("org.videolan.vlc")) {
                vlcInfo = info;
            }
        }
        if (vlcInfo != null) {
            System.err.println("Found : " + vlcInfo.toString());
            File expectedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Uri uri = Uri.fromFile(new File(expectedFilePath, title));
            Intent intent = packageManager.getLaunchIntentForPackage(vlcInfo.packageName);
            intent.setComponent(new ComponentName(vlcInfo.packageName, vlcInfo.packageName+".gui.video.VideoPlayerActivity"));
            // https://wiki.videolan.org/Android_Player_Intents/
            intent.setDataAndTypeAndNormalize(uri, "audio/*");
            intent.putExtra("title", title);
            intent.putExtra("from_start", true);

            return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return null;
    }

    private EasyYouTMp3Service newEasyYouTMp3Service(Context context) {
        return EasyYouTMp3Service.newInstance(context);
    }
}
