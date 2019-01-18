package com.justtennis.plugin.yout.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import android.widget.AutoCompleteTextView;

import com.justtennis.plugin.common.tool.FragmentTool;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentYoutVideoListBinding;
import com.justtennis.plugin.shared.fragment.AppFragment;
import com.justtennis.plugin.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.shared.manager.NotificationManager;
import com.justtennis.plugin.yout.adapter.YoutFindVideoListAdapter;
import com.justtennis.plugin.yout.dto.VideoContent;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.query.response.YoutFindVideoResponse;
import com.justtennis.plugin.yout.task.YoutFindVideoTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YoutFindVideoFragment extends AppFragment {

    private static final String TAG = YoutFindVideoFragment.class.getName();

    private FragmentYoutVideoListBinding binding;
    private AutoCompleteTextView textView;
    private YoutFindVideoListAdapter publicationListAdapter;
    private List<VideoDto> listDto = new ArrayList<>();
    private AsyncTask<Void, String, YoutFindVideoResponse> youtFindVideoTask;
    private FragmentActivity activity;

    public static Fragment newInstance() {
        return new YoutFindVideoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yout_video_list, container, false);
        textView = binding.publicationMessage;
        binding.setLoading(false);

        initializePublicationMessage();
        initializeFabValidate();
        initializePublicationList();

        return binding.getRoot();
    }

    private void initializePublicationList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            publicationListAdapter = new YoutFindVideoListAdapter(this::updPublicationMessage);
        } else {
            publicationListAdapter = new YoutFindVideoListAdapter((OnListFragmentInteractionListener) item -> updPublicationMessage((VideoDto)item));
        }
        publicationListAdapter.setList(listDto);
        binding.publicationList.setAdapter(publicationListAdapter);
        binding.publicationList.setNestedScrollingEnabled(false);
    }

    private void clear() {
        AutoCompleteTextView textView = binding.publicationMessage;
        textView.clearFocus();
        textView.setText("");
    }

    private void initializePublicationMessage() {
        AutoCompleteTextView textView = binding.publicationMessage;
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

    private void updPublicationMessage(VideoDto dto) {
        textView.requestFocus();
        textView.setText(dto.url);
    }

    private void updButtonStat() {
        boolean check = youtFindVideoTask == null && textView.getText().length() > 0;
        FragmentTool.enableFab(activity, check);
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(activity), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(activity, this::onClickFab);
        updButtonStat();
    }

    private void onClickFab(View view) {
        final Context context = getContext();
        youtFindVideoTask = new YoutFindVideoTask(context, textView.getText().toString()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoading(true);
                listDto.clear();
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
