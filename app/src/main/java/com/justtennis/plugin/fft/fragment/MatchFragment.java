package com.justtennis.plugin.fft.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.MatchAdapter;
import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.model.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.model.RankingMatchResponse;
import com.justtennis.plugin.fft.task.MillesimeTask;
import com.justtennis.plugin.fft.task.RankingMatchTask;
import com.justtennis.plugin.fft.tool.ProgressTool;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MatchFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private List<MatchDto> mList = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private View llMatch;
    private Spinner spMillesime;
    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adpMillesime;
    private MatchAdapter adpMatch;
    private MillesimeTask mMillesimeTask;
    private RankingMatchTask mRankingMatchTask;
    private ProgressBar pgMatch;
    private ProgressBar pgMillesime;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchFragment() {
    }

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
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

        initializeMatch();
        initializeMillesime();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MatchDto item);
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
            adpMatch = new MatchAdapter(mList, mListener);
            recyclerView.setAdapter(adpMatch);
        }

        if (mRankingMatchTask != null) {
            mRankingMatchTask.cancel(true);
        }
        showProgressMatch(true);
        mRankingMatchTask = new MyRankingMatchTask(context);
        mRankingMatchTask.execute((Void)null);
    }

    private void initializeMillesime() {
        Context context = getContext();
        assert context != null;
        adpMillesime = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        spMillesime.setAdapter(adpMillesime);

        if (mMillesimeTask != null) {
            mMillesimeTask.cancel(true);
        }
        showProgressMillesime(true);
        mMillesimeTask = new MyMillesimeTask(context);
        mMillesimeTask.execute((Void) null);
    }

    private void showProgressMillesime(final boolean show) {
        ProgressTool.showProgress(getContext(), spMillesime, pgMillesime, show);
    }

    private void showProgressMatch(final boolean show) {
        ProgressTool.showProgress(getContext(), llMatch, pgMatch, show);
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeTask  extends MillesimeTask {
        MyMillesimeTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }

        @Override
        protected void onPostExecute(List<PalmaresMillesimeResponse.Millesime> millesimes) {
            super.onPostExecute(millesimes);
            showProgressMillesime(false);
            mMillesimeTask = null;
            for(PalmaresMillesimeResponse.Millesime millesime : millesimes) {
                list.add(millesime.value);
            }
            adpMillesime.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMillesime(false);
            mMillesimeTask = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyRankingMatchTask extends RankingMatchTask {
        MyRankingMatchTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mList.clear();
        }

        @Override
        protected void onPostExecute(List<RankingMatchResponse.RankingItem> matchs) {
            super.onPostExecute(matchs);
            showProgressMatch(false);
            mList.addAll(MatchContent.toDto(matchs));
            adpMatch.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMatch(false);
            mRankingMatchTask = null;
        }
    }
}
