package com.justtennis.plugin.fft.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import com.justtennis.plugin.fft.manager.InviteManager.SCORE_RESULT;
import com.justtennis.plugin.fft.model.Player;
import com.justtennis.plugin.fft.model.Ranking;
import com.justtennis.plugin.fft.model.Saison;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.query.response.RankingMatchResponse;
import com.justtennis.plugin.fft.resolver.InviteResolver;
import com.justtennis.plugin.fft.resolver.PlayerResolver;
import com.justtennis.plugin.fft.resolver.RankingResolver;
import com.justtennis.plugin.fft.resolver.SaisonResolver;
import com.justtennis.plugin.fft.resolver.ScoreSetResolver;
import com.justtennis.plugin.fft.task.MillesimeMatchTask;
import com.justtennis.plugin.fft.task.MillesimeTask;
import com.justtennis.plugin.fft.task.RankingMatchTask;
import com.justtennis.plugin.fft.tool.FragmentTool;
import com.justtennis.plugin.fft.tool.ProgressTool;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a listMillesime of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MatchFragment extends Fragment {

    private static final String TAG = MatchFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final long ID_UNKNOWN_PLAYER = -1l;
    private static final long ID_NC_RANKING = -1l;

    private int mMillesimePosition = 0;
    private int mColumnCount = 1;
    private List<MillesimeMatchResponse.MatchItem> listMatch = new ArrayList<>();
    private List<MatchDto> listMatchDto = new ArrayList<>();
    private List<String> listMillesime = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private View llMatch;
    private Spinner spMillesime;
    private ArrayAdapter<String> adpMillesime;
    private MatchAdapter adpMatch;
    private MillesimeTask mMillesimeTask;
    private MillesimeMatchTask mMillesimeMatchTask;
    private MyRankingMatchTask mRankingMatchTask;
    private ProgressBar pgMatch;
    private ProgressBar pgMillesime;
    private TextView tvMessage;
    private LinearLayout llMessage;
    private LinearLayout llContent;
    private SimpleDateFormat sdfFFT;
    private DateFormat sdfBirth;

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
        tvMessage = view.findViewById(R.id.tv_message);
        llMessage = view.findViewById(R.id.llMessage);
        llContent = view.findViewById(R.id.llContent);

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
        sdfFFT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        sdfBirth = new SimpleDateFormat(getString(R.string.msg_common_format_date), Locale.FRANCE);
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
            adpMatch = new MatchAdapter(listMatchDto, mListener);
            recyclerView.setAdapter(adpMatch);
        }

        if (mMillesimeMatchTask!= null) {
            mMillesimeMatchTask.cancel(true);
        }
        if (mRankingMatchTask!= null) {
            mRankingMatchTask.cancel(true);
        }
    }

    private void initializeMillesime() {
        Context context = getContext();
        assert context != null;
        adpMillesime = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listMillesime);
        spMillesime.setAdapter(adpMillesime);

        if (mMillesimeTask != null) {
            mMillesimeTask.cancel(true);
        }
        showProgressMillesime(true);
        mMillesimeTask = new MyMillesimeTask(context);
        mMillesimeTask.execute((Void) null);

        spMillesime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMillesimePosition = position;
                loadMatch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showProgressMatch(true);
            }
        });
    }

    private void loadMatch() {
        Context context = getContext();
        if (mMillesimePosition == 0) {
//            mRankingMatchTask = new MyRankingMatchTask(context);
//            mRankingMatchTask.execute((Void)null);
        } else {
            showProgressMatch(true);
            String millesime = listMillesime.get(mMillesimePosition);
            mMillesimeMatchTask = new MyMillesimeMatchTask(context, millesime);
            mMillesimeMatchTask.execute((Void) null);
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

    private void initializeFabRefresh() {
        FragmentTool.initializeFabDrawable(getActivity(), FragmentTool.INIT_FAB_IMAGE.REFRESH);
        FragmentTool.onClickFab(getActivity(), view -> loadMatch());
    }

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(getActivity(), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), listMatch.isEmpty() ? null : this::onClickFabCreate);
    }

    private void onClickFabCreate(View view) {
        String millesime = listMillesime.get(mMillesimePosition);
        Long idSaison = createOrGetSaison(millesime);

        if (idSaison != null) {
            for (int i=0; i<listMatch.size(); i++) {
                MillesimeMatchResponse.MatchItem match = listMatch.get(i);
                MatchDto dto = listMatchDto.get(i);
                if (dto.selected) {
                    createInvite(match, idSaison);
                }
            }
        }
    }

    private void createInvite(MillesimeMatchResponse.MatchItem match, Long idSaison) {
        Long idPlayer = createOrGetPlayer(match, idSaison);
        Long idRanking = getRanking(match);
        Date date = new Date();
        try {
            date = sdfFFT.parse(match.date);
        } catch (ParseException e) {
            Log.e(TAG, MessageFormat.format("Formatting match.date:{0}", match.date), e);
        }
        String scoreResult = match.vicDef.startsWith("D") ? SCORE_RESULT.DEFEAT.toString() :  SCORE_RESULT.VICTORY.toString();
        Long idClub = null;

        Context context = getContext();
        InviteResolver inviteResolver = InviteResolver.getInstance();
        Long idInvite = inviteResolver.createInvite(context, idSaison, idPlayer, idRanking, date, scoreResult, idClub);

        createScoreSet(match, idInvite);
    }

    @Nullable
    private Long createOrGetPlayer(MillesimeMatchResponse.MatchItem match, Long idSaison) {
        Long ret = null;
        String playerName = match.name;
        if (playerName != null) {
            String firstname = playerName;
            String lastname = "";
            int iSep = playerName.indexOf(' ');
            if (iSep > 0) {
                firstname = playerName.substring(0, iSep);
                lastname = playerName.substring(iSep+1);
            }

            String birthday = getBirthday(match);

            Context context = getContext();
            PlayerResolver playerResolver = PlayerResolver.getInstance();
            List<Player> listPlayer = playerResolver.queryByName(context, firstname, lastname);
            if (listPlayer == null || listPlayer.isEmpty()) {
                Long idRanking = getRanking(match);
                ret = playerResolver.createPlayer(context, firstname, lastname, birthday, idSaison, idRanking);
            } else {
                ret = listPlayer.get(0).getId();
            }

            if (ret == null) {
                // Set ret to UNKNOW
                ret = ID_UNKNOWN_PLAYER;
            }
        }
        return ret;
    }

    @Nullable
    private String getBirthday(MillesimeMatchResponse.MatchItem match) {
        String ret = null;
        if (match.year != null && match.year.length() == 4) {
            try {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(match.year));
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR, 1);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                ret = sdfBirth.format(calendar.getTime());
            } catch (RuntimeException e) {
                Log.e(TAG, MessageFormat.format("Birthday formatting match.year:{0}", match.year), e);
            }
        }
        return ret;
    }

    private long getRanking(MillesimeMatchResponse.MatchItem match) {
        long ret = ID_NC_RANKING;
        if (match.ranking != null && !match.ranking.isEmpty()) {
            RankingResolver rankingResolver = RankingResolver.getInstance();
            List<Ranking> listRanking = rankingResolver.queryByRanking(getContext(), match.ranking);
            if (listRanking != null && !listRanking.isEmpty()) {
                ret = listRanking.get(0).getId();
            }
        }
        if (ret == ID_NC_RANKING) {
            Log.w(TAG, MessageFormat.format("Ranking not found match.ranking:{0}", match.ranking));
        }
        return ret;
    }

    private void createScoreSet(MillesimeMatchResponse.MatchItem match, Long idInvite) {
        try {
            if (match.score != null && !match.score.isEmpty()) {
                Log.w(TAG, MessageFormat.format("ScoreSet creation for idInvite:{0} match.score:{1}", idInvite, match.score));
                ScoreSetResolver rankingResolver = ScoreSetResolver.getInstance();

                String[] listSet = match.score.split(" ");
                if (listSet.length > 0) {
                    int order = 0;
                    for (String set : listSet) {
                        String[] score = set.split("/");
                        if (score.length == 2) {
                            rankingResolver.createScoreSet(getContext(), idInvite, order++, Integer.parseInt(score[0]), Integer.parseInt(score[1]));
                        }
                    }
                }

            }
        } catch (RuntimeException e) {
            Log.e(TAG, "ScoreSet creation", e);
        }
    }

    private Long createOrGetSaison(String millesime) {
        Long idSaison = null;
        Context context = getContext();
        SaisonResolver saisonResolver = SaisonResolver.getInstance();
        List<Saison> listSaison = saisonResolver.queryAll(context);
        for(Saison s : listSaison) {
            if (s.getName().endsWith(millesime)) {
                idSaison = s.getId();
                break;
            }
        }

        if (idSaison == null) {
            idSaison = saisonResolver.createSaison(context, millesime);
        }
        return idSaison;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeTask  extends MillesimeTask {
        MyMillesimeTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listMillesime.clear();
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
            mRankingMatchTask = null;
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
            listMatch.clear();
            listMatchDto.clear();
        }

        @Override
        protected void onPostExecute(List<RankingMatchResponse.RankingItem> matchs) {
            super.onPostExecute(matchs);
            showProgressMatch(false);
            listMatchDto.addAll(MatchContent.toDto(matchs));
            adpMatch.notifyDataSetChanged();
            initializeFabValidate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showProgressMatch(false);
            mRankingMatchTask = null;
        }
    }
}
