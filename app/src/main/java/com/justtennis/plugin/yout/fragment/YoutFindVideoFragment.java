package com.justtennis.plugin.yout.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cameleon.android.common.MainActivity;
import org.cameleon.android.common.tool.FragmentTool;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentYoutVideoListBinding;
import org.cameleon.android.shared.fragment.AppFragment;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentCheckListener;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import org.cameleon.android.common.manager.NotificationManager;
import com.justtennis.plugin.yout.adapter.YoutFindVideoListAdapter;
import com.justtennis.plugin.yout.component.service.DownloadComponentService;
import com.justtennis.plugin.yout.dto.VideoContent;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.justtennis.plugin.yout.preference.YouTubeSharedPref;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.rxjava.RxFindVideo;
import com.justtennis.plugin.yout.task.YoutFindVideoTask;
import com.justtennis.plugin.yout.task.YoutGotoUrlTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YoutFindVideoFragment extends AppFragment {

    private static final String TAG = YoutFindVideoFragment.class.getName();

    private static final String ARG_LIST_DTO = "ARG_LIST_DTO";

    private FragmentYoutVideoListBinding binding;
    private AutoCompleteTextView textView;
    private YoutFindVideoListAdapter publicationListAdapter;
    private List<VideoDto> listDto = new ArrayList<>();
    private List<VideoDto> listDtoArg;
    private boolean withArg;
    private AsyncTask<Void, String, YoutFindVideoResponse> youtFindVideoTask;
    private FragmentActivity activity;
    private LinearLayout llContent;
    private int countCheckedRow;
    private TextView checkCount;

    public static Fragment newInstance() {
        return new YoutFindVideoFragment();
    }

    public static Fragment newInstance(ArrayList<VideoDto> listDto) {
        YoutFindVideoFragment fragment = new YoutFindVideoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIST_DTO, listDto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yout_video_list, container, false);
        textView = binding.publicationMessage;
        llContent = binding.llContent;
        checkCount = binding.checkCount;
        binding.setLoading(false);

        textView.setOnEditorActionListener((v, actionId, event) -> {
            if  ((actionId == EditorInfo.IME_ACTION_DONE)) {
                onClickFab(null);
            }
            return false;
        });

        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();
        if (arguments != null) {
            listDtoArg = (ArrayList<VideoDto>) arguments.getSerializable(ARG_LIST_DTO);
            if (listDtoArg != null) {
                listDto.addAll(listDtoArg);
            }
        }
        withArg = listDtoArg != null && !listDtoArg.isEmpty();

        initializePublicationList();
        initializePublicationMessage();
        initializeFabValidate();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        RxFindVideo.subscribe(RxFindVideo.SUBJECT_UPDATE_DOWNLOAD_STATUS_VIDEO, this, o -> updateDownloadStatus((VideoDto) o));
    }

    @Override
    public void onPause() {
        RxFindVideo.unregister(this);
        super.onPause();
    }

    private void initializePublicationList() {
        OnListFragmentInteractionListener longClickListener = withArg ? null : item -> onLongClickRow((VideoDto) item);
        OnListFragmentCheckListener checkListener = (item, isCheck) -> onCheckRow((VideoDto) item, isCheck);
        OnListFragmentInteractionListener checkLongClickListener = item -> onCheckLongClick((VideoDto) item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            publicationListAdapter = new YoutFindVideoListAdapter(this::updPublicationMessage, longClickListener, checkListener, checkLongClickListener);
        } else {
            publicationListAdapter = new YoutFindVideoListAdapter((OnListFragmentInteractionListener) item -> updPublicationMessage((VideoDto)item), longClickListener, checkListener, checkLongClickListener);
        }
        publicationListAdapter.setList(listDto);
        publicationListAdapter.setShowCheck(withArg);
        binding.publicationList.setAdapter(publicationListAdapter);
        binding.publicationList.setNestedScrollingEnabled(false);
    }

    private void initializePublicationMessage() {
        checkCount.setVisibility(withArg ? View.VISIBLE : View.GONE);
        if (withArg) {
            textView.setText(listDtoArg.get(0).title);
            textView.setEnabled(false);
        } else {
            textView.setEnabled(true);
            textView.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Nothing here
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updButtonStat();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Nothing  here
                }
            });
        }
    }

    private void updPublicationMessage(VideoDto dto) {
        if (dto.type == MEDIA_TYPE.PLAYLIST) {
            executeGotoUrl(getContext(), dto);
        }
        else if (dto.type == MEDIA_TYPE.VIDEO) {
            updPulicationMessageVideo(dto);
        } else {
            Toast.makeText(activity, dto.url, Toast.LENGTH_SHORT).show();
        }
    }

    private void updPulicationMessageVideo(VideoDto dto) {
        if (!publicationListAdapter.isShowCheck()) {
            boolean playYoutube = true;
            if (dto.downloadPath == null || dto.downloadPath.isEmpty()) {
                dto.downloadPath = YouTubeSharedPref.getVideoPath(activity, dto.id);
            }
            if (dto.downloadPath != null && !dto.downloadPath.isEmpty()) {
                File file = new File(dto.downloadPath);
                if (file.exists()) {
                    playYoutube = false;
                    watchPlayerVideo(activity, file);
                }
            }
            if (playYoutube) {
                watchYoutubeVideo(activity, dto.id);
            }
        } else {
            dto.viewHolder.check();
        }
    }

    private void onLongClickRow(VideoDto dto) {
        publicationListAdapter.setShowCheck(!publicationListAdapter.isShowCheck());
        if (publicationListAdapter.isShowCheck()) {
            dto.checked = MEDIA_TYPE.VIDEO == dto.type;
            if (dto.checked) {
                setCountCheckedRow(1);
            }
        } else {
            for(VideoDto d : listDto) {
                d.checked = false;
            }
            setCountCheckedRow(0);
        }
        checkCount.setVisibility(publicationListAdapter.isShowCheck() ? View.VISIBLE : View.GONE);
        publicationListAdapter.notifyDataSetChanged();
    }

    private void onCheckRow(VideoDto dto, boolean isCheck) {
        if (dto.checked != isCheck) {
            dto.checked = isCheck;
            incCountCheckedRow(dto.checked ? 1 : -1);
        }
    }

    private void onCheckLongClick(VideoDto dto) {
        boolean checked = dto.checked;
        int cnt = 0;
        if (checked) {
            for (VideoDto d : listDto) {
                d.checked = (MEDIA_TYPE.VIDEO == d.type);
                cnt += (d.checked ? 1 : 0);
            }
        } else {
            for (VideoDto d : listDto) {
                d.checked = false;
            }
        }
        setCountCheckedRow(cnt);
        publicationListAdapter.notifyDataSetChanged();
    }

    public static void watchPlayerVideo(Context context, File file){
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setDataAndType(Uri.fromFile(file), "audio/*");
        context.startActivity(Intent.createChooser(viewIntent, null));
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void updButtonStat() {
        boolean check = false;
        if (publicationListAdapter.isShowCheck()) {
            check = getCountCheckedRow() > 0;
        } else {
            check = youtFindVideoTask == null && textView.getText().length() > 0;
        }
        FragmentTool.enableFab(activity, check);
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(activity), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(activity, this::onClickFab);
        updButtonStat();
    }

    private void closeKeyboard() {
        InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(llContent.getWindowToken(), 0);
    }

    private void onClickFab(View view) {
        closeKeyboard();
        final Context context = getContext();
        if (publicationListAdapter.isShowCheck()) {
            executeDownVideo(context);
        } else {
            executeFindVideo(context);
        }
    }

    public int getCountCheckedRow() {
        return countCheckedRow;
    }

    public void setCountCheckedRow(int countCheckedRow) {
        this.countCheckedRow = countCheckedRow;
        checkCount.setText(String.format("%d", countCheckedRow));
        updButtonStat();
    }

    public void incCountCheckedRow(int nb) {
        setCountCheckedRow(countCheckedRow+nb);
    }

    private void executeGotoUrl(Context context, VideoDto dto) {
        AsyncTask<Void, String, YoutFindVideoResponse> youtGotoUrlTask = new YoutGotoUrlTask(context, dto.url) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoading(true);
            }

            @Override
            protected void onPostExecute(YoutFindVideoResponse response) {
                super.onPostExecute(response);
                if (response != null && !response.videoList.isEmpty()) {
                    ArrayList<VideoDto> list = new ArrayList<>();
                    list.addAll(VideoContent.toDto(response.videoList));

                    FragmentTool.replaceFragment(activity, YoutFindVideoFragment.newInstance(list));
                }
                binding.setLoading(false);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                NotificationManager.onTaskProcessUpdate(activity, values);
            }
        }.execute();
    }

    private void updateDownloadStatus(VideoDto dto) {
        activity.runOnUiThread(() -> {
            for (VideoDto d : listDto) {
                if (d.equals(dto)) {
                    d.updateDownloadStatus(dto.downloadStatus);
                    break;
                }
            }
        });
    }

    private void executeDownVideo(Context context) {
        ((MainActivity)activity).showMessage("Starting Download to MP3.");

        DownloadComponentService.start(context, listDto);
    }

    private void executeFindVideo(Context context) {
        if (youtFindVideoTask != null) {
            return;
        }
        youtFindVideoTask = new YoutFindVideoTask(context, textView.getText().toString()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoading(true);
                listDto.clear();
                publicationListAdapter.setShowCheck(false);
            }

            @Override
            protected void onPostExecute(YoutFindVideoResponse response) {
                super.onPostExecute(response);
                if (response != null && !response.videoList.isEmpty()) {
                    listDto.addAll(VideoContent.toDto(response.videoList));
                }
                publicationListAdapter.notifyDataSetChanged();
                binding.setLoading(false);
                youtFindVideoTask = null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                NotificationManager.onTaskProcessUpdate(activity, values);
            }
        }.execute();
    }
}
