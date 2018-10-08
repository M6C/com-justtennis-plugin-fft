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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.MatchAdapter;
import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.fft.model.User;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.fft.resolver.UserResolver;
import com.justtennis.plugin.fft.task.RankingMatchTask;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.fft.tool.ProgressTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RankingMatchFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String TAG = RankingMatchFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private List<MillesimeMatchResponse.MatchItem> listMatch = new ArrayList<>();
    private List<MatchDto> listMatchDto = new ArrayList<>();
    private View llMatch;
    private MatchAdapter adpMatch;
    private MyRankingMatchTask mRankingMatchTask;
    private ProgressBar pgMatch;
    private TextView tvMessage;
    private LinearLayout llMessage;
    private LinearLayout llContent;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RankingMatchFragment() {
        // Nothing to do
    }

    public static RankingMatchFragment newInstance() {
        RankingMatchFragment fragment = new RankingMatchFragment();
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
        View view = inflater.inflate(R.layout.fragment_ranking_match_list, container, false);

        llMatch = view.findViewById(R.id.list);
        pgMatch = view.findViewById(R.id.progress_match);
        tvMessage = view.findViewById(R.id.tv_message);
        llMessage = view.findViewById(R.id.llMessage);
        llContent = view.findViewById(R.id.llContent);

        initializeMatch();
        hideFab();
        loadMatch();

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

        if (mRankingMatchTask!= null) {
            mRankingMatchTask.cancel(true);
        }
    }

    private void loadMatch() {
        Context context = getContext();
        mRankingMatchTask = new MyRankingMatchTask(context);
        mRankingMatchTask.execute((Void)null);
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
        FragmentTool.onClickFab(getActivity(), view -> loadMatch());
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
    private class MyRankingMatchTask extends RankingMatchTask {

        MyRankingMatchTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressMatch(true);
            listMatch.clear();
            listMatchDto.clear();
        }

        @Override
        protected void onPostExecute(List<RankingMatchResponse.RankingItem> matchs) {
            super.onPostExecute(matchs);
            try {
                mRankingMatchTask = null;
                listMatchDto.addAll(MatchContent.toDto(matchs));
                adpMatch.notifyDataSetChanged();
            } finally {
                if (!listMatchDto.isEmpty()) {
                    initializeFabValidate();
                    hideMessage();
                } else {
                    initializeFabRefresh();
                    showMessage(R.string.msg_fft_must_connect_to_site);
                }
                showProgressMatch(false);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMatch(false);
            mRankingMatchTask = null;
        }
    }
}
