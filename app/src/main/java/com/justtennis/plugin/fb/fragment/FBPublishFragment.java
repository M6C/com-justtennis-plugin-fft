package com.justtennis.plugin.fb.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
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

import com.justtennis.plugin.fb.task.FBPublishTask;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.databinding.FragmentFbPublicationListBinding;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.shared.fragment.AppFragment;
import com.justtennis.plugin.shared.manager.NotificationManager;

import java.util.Objects;

public class FBPublishFragment extends AppFragment {

    private static final String TAG = FBPublishFragment.class.getName();

    private FragmentFbPublicationListBinding binding;

    public static Fragment newInstance() {
        return new FBPublishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fb_publication_list, container, false);

        initializePublicationMessage();
        initializePublicationButton();
        initializeFabValidate();

        return binding.getRoot();
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
        new FBPublishTask(context) {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                Toast.makeText(context, "Publish " + (aBoolean ? "Successfull" : "failed"), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                NotificationManager.onTaskProcessUpdate(context, values);
            }
        }.execute(message);
        clear();
    }
}
