package com.justtennis.plugin.fb.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
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
import android.widget.Toast;

import com.justtennis.plugin.common.tool.FragmentTool;
import com.justtennis.plugin.fb.adapter.PublicationListAdapter;
import com.justtennis.plugin.fb.dto.PublicationContent;
import com.justtennis.plugin.fb.dto.PublicationDto;
import com.justtennis.plugin.fb.manager.SharingYoutubeManager;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;
import com.justtennis.plugin.fb.query.response.FBPublishFormResponse;
import com.justtennis.plugin.fb.task.FBProfilPublicationTask;
import com.justtennis.plugin.fb.task.FBPublishFormTask;
import com.justtennis.plugin.fb.task.FBPublishTask;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentFbPublicationListBinding;
import com.justtennis.plugin.shared.fragment.AppFragment;
import com.justtennis.plugin.shared.manager.NotificationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FBPublishFragment extends AppFragment {

    private static final String TAG = FBPublishFragment.class.getName();

    private FragmentFbPublicationListBinding binding;
    private PublicationListAdapter publicationListAdapter;
    private List<PublicationDto> listPublication = new ArrayList<>();
    private FBPublishFormTask publishFormTask;
    private FBPublishFormResponse publishFormResponse;
    private int maxLine;
    private static String id;
    private String subject;

    public static Fragment newInstance() {
        return new FBPublishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fb_publication_list, container, false);
        binding.setLoading(false);
        maxLine = binding.publicationMessage.getMaxLines();

        initializePublicationMessage();
        initializePublicationButton();
        initializeFabValidate();
        initializePublicationList();
        initializePublicationForm();

        return binding.getRoot();
    }

    private void initializePublicationList() {
        final AutoCompleteTextView textView = binding.publicationMessage;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            publicationListAdapter = new PublicationListAdapter(publicationDto -> {
                textView.requestFocus();
                textView.setText(publicationDto.message);
                updPublicationMessageDesign(textView, true);
            });
            publicationListAdapter.setList(listPublication);
            binding.publicationList.setAdapter(publicationListAdapter);

            initializeProfilPublication();
        } else {
            binding.setLoading(false);
        }
    }

    private void clear() {
        AutoCompleteTextView textView = binding.publicationMessage;
        textView.clearFocus();
        textView.setText("");
    }

    private void initializePublicationMessage() {
        AutoCompleteTextView textView = binding.publicationMessage;
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(Intent.EXTRA_TEXT)) {
            String text = bundle.getString(Intent.EXTRA_TEXT);
            if (text != null && !text.isEmpty()) {
                textView.setText(text);
                id = SharingYoutubeManager.getInstance().getIdFromUrl(text);
                subject = bundle.getString(Intent.EXTRA_SUBJECT);
            }
        }
        textView.setOnFocusChangeListener((v, hasFocus) -> updPublicationMessageDesign(textView, hasFocus));
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updPublicationButtonStat();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing  here
            }
        });
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

    private void updPublicationButtonStat() {
        AutoCompleteTextView textView = binding.publicationMessage;
        boolean check = publishFormTask == null && textView.getText().length() > 0;
        binding.publicationButton.setEnabled(check);
    }

    private void initializePublicationButton() {
        binding.publicationButton.setOnClickListener(this::onClickFabPublish);
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), null);
    }

    private void initializePublicationForm(){
        binding.progressBarBtn.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.fb_create_form_progress), PorterDuff.Mode.SRC_IN);
        binding.publicationButton.setText(getString(R.string.text_fb_button_create, getString(R.string.fb_text_unknown)));
        final Context context = getContext();
        publishFormTask = new FBPublishFormTask(context) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoadingAudience(true);
            }

            @Override
            protected void onPostExecute(FBPublishFormResponse publishFormResponse) {
                FBPublishFragment.this.publishFormResponse = publishFormResponse;
                super.onPostExecute(publishFormResponse);
                if (publishFormResponse != null && publishFormResponse.audience.value != null && !publishFormResponse.audience.value.isEmpty()) {
                    binding.publicationButton.setText(getString(R.string.text_fb_button_create, publishFormResponse.audience.value));
                }
                publishFormTask = null;
                updPublicationButtonStat();
                binding.setLoadingAudience(false);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                NotificationManager.onTaskProcessUpdate(getActivity(), values);
            }
        };
        publishFormTask.execute();
    }

    private void initializeProfilPublication(){
        final Context context = getContext();
        new FBProfilPublicationTask(context) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                binding.setLoading(true);
            }

            @Override
            protected void onPostExecute(FBProfilPublicationResponse profilPublicationResponse) {
                super.onPostExecute(profilPublicationResponse);
                if (profilPublicationResponse != null && !profilPublicationResponse.timeLineList.isEmpty()) {
                    listPublication.addAll(PublicationContent.toDto(profilPublicationResponse.timeLineList));
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

    private void onClickFabPublish(View view) {
        String message = binding.publicationMessage.getText().toString();
        final Context context = getContext();

        PublicationDto dto = createDto(message);
        listPublication.add(0, dto);
        this.publishFormResponse.publishId = id;
        this.publishFormResponse.publishTitle = subject;

        new FBPublishTask(context, this.publishFormResponse) {
            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                String text = "Publish " + (success ? "Successfull" : "Failed");
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onProgressUpdate(Serializable... values) {
                super.onProgressUpdate(values);
                if (values instanceof String[]) {
                    NotificationManager.onTaskProcessUpdate(getActivity(), (String[]) values);
                } else if (publicationListAdapter != null){
                    publicationListAdapter.notifyDataSetChanged();
                }
            }
        }.execute(dto);
        clear();
    }

    private PublicationDto createDto(String message) {
        Date date = new Date();
        long id = date.getTime();
        return new PublicationDto(id, date, message);
    }
}
