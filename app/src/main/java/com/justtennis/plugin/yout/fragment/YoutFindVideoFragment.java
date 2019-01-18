package com.justtennis.plugin.yout.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.justtennis.plugin.common.tool.FragmentTool;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentFbPublicationListBinding;
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

    private FragmentFbPublicationListBinding binding;
    private AutoCompleteTextView textView;
    private YoutFindVideoListAdapter publicationListAdapter;
    private List<VideoDto> listDto = new ArrayList<>();
    private int maxLine;
    private AsyncTask<Void, String, YoutFindVideoResponse> youtFindVideoTask;

    public static Fragment newInstance() {
        return new YoutFindVideoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fb_publication_list, container, false);
        textView = binding.publicationMessage;
        binding.setLoading(false);
        maxLine = binding.publicationMessage.getMaxLines();

        initializePublicationMessage();
        initializeButton();
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

        initializeFindVideo();
    }

    private void clear() {
        AutoCompleteTextView textView = binding.publicationMessage;
        textView.clearFocus();
        textView.setText("");
    }

    private void initializePublicationMessage() {
        AutoCompleteTextView textView = binding.publicationMessage;
        Bundle bundle = getArguments();
        textView.setOnFocusChangeListener((v, hasFocus) -> updPublicationMessageDesign(textView, hasFocus));
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
        YoutFindVideoFragment.this.updPublicationMessageDesign(textView, true);
    }

    private void updPublicationMessageDesign(AutoCompleteTextView textView, boolean hasFocus) {
        if (hasFocus) {
            textView.setLines(maxLine);
        } else {
            textView.setLines(1);
            InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
    }

    private void updButtonStat() {
        AutoCompleteTextView textView = binding.publicationMessage;
        boolean check = youtFindVideoTask == null && textView.getText().length() > 0;
        binding.publicationButton.setEnabled(check);
    }

    private void initializeButton() {
        binding.publicationButton.setOnClickListener(this::onClickFab);
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), null);
    }

    private void initializeFindVideo(){
        final Context context = getContext();
        youtFindVideoTask = new YoutFindVideoTask(context, textView.getText().toString()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoading(true);
            }

            @Override
            protected void onPostExecute(YoutFindVideoResponse response) {
                super.onPostExecute(response);
                if (response != null && !response.videoList.isEmpty()) {
                    listDto.addAll(VideoContent.toDto(response.videoList));
                }
                publicationListAdapter.notifyDataSetChanged();
                binding.setLoading(false);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                NotificationManager.onTaskProcessUpdate(getActivity(), values);
            }
        }.execute();
    }

    private void onClickFab(View view) {
//        String message = binding.publicationMessage.getText().toString();
//        final Context context = getContext();
//
//        VideoDto dto = createDto(message);
//        listDto.add(0, dto);
//        this.publishFormResponse.publishId = id;
//        this.publishFormResponse.publishTitle = subject;
//
//        new FBPublishTask(context, this.publishFormResponse) {
//            @Override
//            protected void onPostExecute(Boolean success) {
//                super.onPostExecute(success);
//                String text = "Publish " + (success ? "Successfull" : "Failed");
//                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            protected void onProgressUpdate(Serializable... values) {
//                super.onProgressUpdate(values);
//                if (values instanceof String[]) {
//                    NotificationManager.onTaskProcessUpdate(getActivity(), (String[]) values);
//                } else if (publicationListAdapter != null){
//                    publicationListAdapter.notifyDataSetChanged();
//                }
//            }
//        }.execute(dto);
//        clear();
    }
}
