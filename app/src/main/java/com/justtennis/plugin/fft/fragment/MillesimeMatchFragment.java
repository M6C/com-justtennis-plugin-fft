package com.justtennis.plugin.fft.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.MatchAdapter;
import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.fft.model.User;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.resolver.UserResolver;
import com.justtennis.plugin.fft.task.CreateInviteTask;
import com.justtennis.plugin.fft.task.MillesimeMatchTask;
import com.justtennis.plugin.fft.task.MillesimeTask;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.fft.tool.ProgressTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MillesimeMatchFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String TAG = MillesimeMatchFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mMillesimePosition = 0;
    private int mColumnCount = 1;
    private List<MillesimeMatchResponse.MatchItem> listMatch = new ArrayList<>();
    private List<MatchDto> listMatchDto = new ArrayList<>();
    private List<String> listMillesime = new ArrayList<>();
    private View llMatch;
    private Spinner spMillesime;
    private ArrayAdapter<String> adpMillesime;
    private MatchAdapter adpMatch;
    private MillesimeTask mMillesimeTask;
    private MillesimeMatchTask mMillesimeMatchTask;
    private ProgressBar pgMatch;
    private ProgressBar pgMillesime;
    private TextView tvMessage;
    private LinearLayout llMessage;
    private LinearLayout llContent;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MillesimeMatchFragment() {
        // Nothing to do
    }

    public static MillesimeMatchFragment newInstance() {
        MillesimeMatchFragment fragment = new MillesimeMatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);

        llMatch = view.findViewById(R.id.list);
        spMillesime = view.findViewById(R.id.sp_millesime);
        pgMatch = view.findViewById(R.id.progress_match);
        pgMillesime = view.findViewById(R.id.progress_millesime);
        tvMessage = view.findViewById(R.id.tv_message);
        llMessage = view.findViewById(R.id.llMessage);
        llContent = view.findViewById(R.id.llContent);

        initializeMatch();
        initializeMillesime();
        hideFab();

        return view;
    }

    private void initializeMatch() {
        Context context = getContext();
        assert context != null;
        // Set the adapter
        if (llMatch instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) llMatch;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adpMatch = new MatchAdapter(context, listMatchDto, this);
            recyclerView.setAdapter(adpMatch);
        }

        if (mMillesimeMatchTask!= null) {
            mMillesimeMatchTask.cancel(true);
        }
    }

    private void initializeMillesime() {
        Context context = getContext();
        assert context != null;
        adpMillesime = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listMillesime);
        spMillesime.setAdapter(adpMillesime);

        loadMillesime();

        spMillesime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMillesimePosition = position;
                loadMatch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do
            }
        });
    }

    private void loadMillesime() {
        Context context = getContext();
        if (mMillesimeTask != null) {
            mMillesimeTask.cancel(true);
        }
        mMillesimeTask = new MyMillesimeTask(context);
        mMillesimeTask.execute((Void) null);
    }

    private void loadMatch() {
        Context context = getContext();
        if (mMillesimePosition > 0) {
            String millesime = listMillesime.get(mMillesimePosition);
            adpMatch.setMillesime(millesime);
            mMillesimeMatchTask = new MyMillesimeMatchTask(context, millesime);
            mMillesimeMatchTask.execute((Void) null);
        }
    }

    private void refresh() {
        if (mMillesimePosition > 0) {
            loadMatch();
        } else {
            loadMillesime();
        }
    }

    private void showProgressMillesime(final boolean show) {
        ProgressTool.showProgress(getContext(), spMillesime, pgMillesime, show);
    }

    private void showProgressMatch(final boolean show) {
        ProgressTool.showProgress(getContext(), llMatch, pgMatch, show);
    }

    private void showMessage(@StringRes int id) {
        llContent.setVisibility(View.GONE);
        llMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(Html.fromHtml(getString(id)));
    }

    private void hideMessage() {
        llMessage.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
        tvMessage.setText("");
    }

    private void hideFab() {
        FragmentTool.onClickFab(Objects.requireNonNull(getActivity()), null);
    }

    private void initializeFabRefresh() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.REFRESH);
        FragmentTool.onClickFab(getActivity(), view -> refresh());
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), listMatch.isEmpty() ? null : this::onClickFabCreate);
    }

    private void initializeFabHideMessage(FragmentActivity activity) {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.BACK);
        FragmentTool.onClickFab(activity, v -> {
            hideMessage();
            initializeFabValidate();
        });
    }

    private void onClickFabCreate(View view) {
        List<User> users = UserResolver.getInstance().queryAll(getContext());
        if (!users.isEmpty()) {
            String millesime = listMillesime.get(mMillesimePosition);
            MyCreateInviteTask myCreateInviteTask = new MyCreateInviteTask(getContext(), listMatch, listMatchDto, millesime);
            myCreateInviteTask.execute((Void) null);
        } else {
            FragmentActivity activity = Objects.requireNonNull(getActivity());
            showMessage(R.string.msg_no_user_found_in_main_application);
            initializeFabHideMessage(activity);
        }
    }

    @Override
    public void onListFragmentInteraction(MatchDto item) {
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeTask  extends MillesimeTask {
        MyMillesimeTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressMillesime(true);
            listMillesime.clear();
            adpMillesime.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<PalmaresMillesimeResponse.Millesime> millesimes) {
            super.onPostExecute(millesimes);
            try {
                mMillesimeTask = null;
                if (millesimes != null && !millesimes.isEmpty()) {
                    listMillesime.add("");
                    for (PalmaresMillesimeResponse.Millesime millesime : millesimes) {
                        listMillesime.add(millesime.value);
                    }
                    adpMillesime.notifyDataSetChanged();
                }
            } finally {
                if (!listMillesime.isEmpty()) {
                    hideMessage();
                } else {
                    initializeFabRefresh();
                    showMessage(R.string.msg_fft_must_connect_to_site);
                }
                showProgressMillesime(false);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMillesime(false);
            mMillesimeTask = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeMatchTask extends MillesimeMatchTask {

        MyMillesimeMatchTask(Context context, String millesime) {
            super(context, millesime);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressMatch(true);
            listMatch.clear();
            listMatchDto.clear();
            adpMatch.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<MillesimeMatchResponse.MatchItem> matchs) {
            super.onPostExecute(matchs);
            showProgressMatch(false);
            listMatch.addAll(matchs);
            listMatchDto.addAll(MatchContent.toDto2(matchs));
            adpMatch.notifyDataSetChanged();
            initializeFabValidate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMatch(false);
            mMillesimeMatchTask = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyCreateInviteTask extends CreateInviteTask {

        MyCreateInviteTask(Context context, List<MillesimeMatchResponse.MatchItem> listMatch, List<MatchDto> listMatchDto, String millesime) {
            super(context, listMatch, listMatchDto, millesime);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideFab();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adpMatch.notifyDataSetChanged();
            initializeFabValidate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            initializeFabValidate();
        }
    }
}
