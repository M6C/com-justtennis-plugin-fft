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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.MatchAdapter;
import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.model.User;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.resolver.UserResolver;
import com.justtennis.plugin.fft.task.CreateInviteTask;
import com.justtennis.plugin.fft.task.MillesimeMatchTask;
import com.justtennis.plugin.fft.task.MillesimeTask;

import org.cameleon.android.common.manager.IMainManager;
import org.cameleon.android.common.manager.MainManager;
import org.cameleon.android.common.tool.FragmentTool;
import org.cameleon.android.common.tool.ProgressTool;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentInteractionListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MillesimeMatchFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String TAG = MillesimeMatchFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_MATCH = "ARG_MATCH";
    private static final String ARG_MILLESIME = "ARG_MILLESIME";
    private static final String DAT_MILLESIME_POS = "DAT_MILLESIME_POS";

    private int mMillesimePosition = 0;
    private int mColumnCount = 1;
    private MatchDto argMatch;
    private String argMillesime;
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
    private View llMessage;
    private View llContent;
    private FragmentActivity activity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MillesimeMatchFragment() {
        // Nothing to do
    }

    public static MillesimeMatchFragment newInstance() {
        return newInstance(null, null);
    }

    public static MillesimeMatchFragment newInstance(MatchDto match, String millesime) {
        MillesimeMatchFragment fragment = new MillesimeMatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        if (match != null) {
            args.putSerializable(ARG_MATCH, match);
        }
        if (millesime != null && !millesime.isEmpty()) {
            args.putString(ARG_MILLESIME, millesime);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
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

        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT, mColumnCount);
            argMatch = arguments.containsKey(ARG_MATCH) ? (MatchDto) arguments.getSerializable(ARG_MATCH) : argMatch;
            argMillesime = arguments.getString(ARG_MILLESIME, argMillesime);
            mMillesimePosition = arguments.getInt(DAT_MILLESIME_POS, mMillesimePosition);
        }

        initializeMatch();
        initializeMillesime();
        hideFab();

        return view;
    }

    @Override
    public void onDestroyView() {
        if (mMillesimeTask != null) {
            mMillesimeTask.cancel(true);
        }
        if (mMillesimeMatchTask != null) {
            mMillesimeMatchTask.cancel(true);
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);
        if (argMatch != null) {
            outState.putSerializable(ARG_MATCH, argMatch);
        }
        if (argMillesime != null) {
            outState.putString(ARG_MILLESIME, argMillesime);
        }
        outState.putInt(DAT_MILLESIME_POS, mMillesimePosition);
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        MatchDto match = (MatchDto)item;
        if (match.linkPalmares != null && !match.linkPalmares.isEmpty()) {
            FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance(match, getMillesime()));
        }
    }

    private void initializeMatch() {
        Context context = getContext();
        assert context != null;
        listMatchDto.clear();
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
            adpMatch.setCanValidate(canValidate());
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
            String millesime = getMillesime();
            adpMatch.setMillesime(millesime);
            mMillesimeMatchTask = new MyMillesimeMatchTask(context, (argMatch == null ? null : argMatch.linkPalmares), millesime);
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
        if (canValidate()) {
            FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
            FragmentTool.onClickFab(getActivity(), listMatch.isEmpty() ? null : this::onClickFabCreate);
        } else {
            initializeFabInfo();
        }
    }

    private void initializeFabInfo() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.INFORMATION);
        FragmentTool.onClickFab(getActivity(), listMatch.isEmpty() ? null : this::onClickFabInformation);
    }

    private void initializeFabHideMessage(FragmentActivity activity) {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.BACK);
        FragmentTool.onClickFab(activity, v -> {
            hideMessage();
            initializeFabValidate();
        });
    }

    private void onClickFabCreate(View view) {
        closeKeyboard();

        List<User> users = UserResolver.getInstance().queryAll(getContext());
        if (!users.isEmpty()) {
            MyCreateInviteTask myCreateInviteTask = new MyCreateInviteTask(getContext(), listMatch, listMatchDto, getMillesime());
            myCreateInviteTask.execute((Void) null);
        } else {
            FragmentActivity activity = Objects.requireNonNull(getActivity());
            showMessage(R.string.msg_no_user_found_in_main_application);
            initializeFabHideMessage(activity);
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(llContent.getWindowToken(), 0);
    }

    private void onClickFabInformation(View view) {
        showInformation();
    }

    private boolean canValidate() {
        return argMatch == null;
    }

    private String getMillesime() {
        return listMillesime.get(mMillesimePosition);
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeTask  extends MillesimeTask {
        private Context context;

        MyMillesimeTask(Context context) {
            super(context);
            this.context = context;
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
                    if (argMillesime != null && !argMillesime.isEmpty()) {
                        mMillesimePosition = listMillesime.indexOf(argMillesime);
                    } else {
                        mMillesimePosition = (listMillesime.size() > 1) ? 1 : 0;
                    }
                    if (mMillesimePosition > 0) {
                        spMillesime.setSelection(mMillesimePosition);
                    }
                } else {
                    initializeFabRefresh();
                    showMessage(R.string.msg_fft_must_connect_to_site);
                }
                showProgressMillesime(false);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            getMainManager().getNotificationManager().onTaskProcessUpdate(getActivity(), values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            logMe("MyMillesimeTask cancelled.");
            showProgressMillesime(false);
            mMillesimeTask = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyMillesimeMatchTask extends MillesimeMatchTask {

        private Context context;

        MyMillesimeMatchTask(Context context, String palmaresAction, String millesime) {
            super(context, palmaresAction, millesime);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressMatch(true);
            hideMessage();
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
            MatchContent.sortDefault(listMatchDto);
            adpMatch.notifyDataSetChanged();
            initializeFabValidate();
            if (listMatchDto.isEmpty()) {
                showMessage(R.string.msg_no_match_found);
            }

            if (argMatch != null) {
                showInformation();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            getMainManager().getNotificationManager().onTaskProcessUpdate(getActivity(), values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            logMe("MyMillesimeMatchTask cancelled.");
            showProgressMatch(false);
            mMillesimeMatchTask = null;
        }
    }

    private IMainManager getMainManager() {
        return MainManager.getInstance();
    }

    private void showInformation() {
        String s = MessageFormat.format("Selected Player Match {0} millesime {1}", argMatch.name, getMillesime());
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
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
            logMe("MyCreateInviteTask cancelled.");
            initializeFabValidate();
        }
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
