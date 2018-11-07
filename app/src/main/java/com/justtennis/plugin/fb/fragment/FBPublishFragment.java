package com.justtennis.plugin.fb.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
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

import com.justtennis.plugin.fb.adapter.PublicationListAdapter;
import com.justtennis.plugin.fb.dto.PublicationDto;
import com.justtennis.plugin.fb.task.FBPublishTask;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentFbPublicationListBinding;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.shared.fragment.AppFragment;
import com.justtennis.plugin.shared.manager.NotificationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FBPublishFragment extends AppFragment {

    private static final String TAG = FBPublishFragment.class.getName();

    private FragmentFbPublicationListBinding binding;
    private PublicationListAdapter publicationListAdapter;
    private List<PublicationDto> listPublication = new ArrayList<>();

    public static Fragment newInstance() {
        return new FBPublishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fb_publication_list, container, false);
        binding.setLoading(false);

        initializePublicationMessage();
        initializePublicationButton();
        initializeFabValidate();
        initializePublicationList();

        return binding.getRoot();
    }

    private void initializePublicationList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            publicationListAdapter = new PublicationListAdapter(null);
            publicationListAdapter.setList(listPublication);
            binding.publicationList.setAdapter(publicationListAdapter);
        }
    }

    private void clear() {
        AutoCompleteTextView textView = binding.publicationMessage;
        textView.clearFocus();
        textView.setText("");
    }

    private void initializePublicationMessage() {
        AutoCompleteTextView textView = binding.publicationMessage;
        textView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                textView.setLines(textView.getMaxLines());
            } else {
                textView.setLines(1);
                InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean check = textView.getText().length() > 0;
                int visibility = check ? View.VISIBLE : View.GONE;
                binding.publicationButton.setVisibility(visibility);
//                FragmentTool.onClickFab(getActivity(), (check ? FBPublishFragment.this::onClickFabPublish : null));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing  here
            }
        });
    }

    private void initializePublicationButton() {
        binding.publicationButton.setOnClickListener(this::onClickFabPublish);
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), null);
    }

    private void onClickFabPublish(View view) {
        String message = binding.publicationMessage.getText().toString();
        final Context context = getContext();

        PublicationDto dto = createDto(message);
        listPublication.add(dto);

        new FBPublishTask(context) {
            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                String text = "Publish " + (success ? "Successfull" : "Failed");
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onProgressUpdate(Serializable... values) {
                super.onProgressUpdate(values);
                if (values != null && values.length > 0) {
                    if (values instanceof String[]) {
                        NotificationManager.onTaskProcessUpdate(context, (String[]) values);
                    } else {
                        publicationListAdapter.notifyDataSetChanged();
                    }
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
