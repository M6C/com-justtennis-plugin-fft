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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.justtennis.plugin.fft.model.enums.EnumPlayer;
import com.justtennis.plugin.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.common.tool.FragmentTool;
import com.justtennis.plugin.common.tool.ProgressTool;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.FindPlayerAdapter;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.dto.PlayerContent;
import com.justtennis.plugin.fft.dto.PlayerDto;
import com.justtennis.plugin.fft.query.response.FindPlayerResponse;
import com.justtennis.plugin.fft.task.FindPlayerTask;
import com.justtennis.plugin.shared.manager.NotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindPlayerFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String TAG = FindPlayerFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private List<FindPlayerResponse.PlayerItem> listPlayer = new ArrayList<>();
    private List<PlayerDto> listPlayerDto = new ArrayList<>();
    private View llMatch;
    private Spinner spGenre;
    private FindPlayerAdapter adpPlayer;
    private MyFindPlayerTask mFindPlayerTask;
    private ProgressBar pgPlayer;
    private TextView tvMessage;
    private View llMessage;
    private View llContent;
    private FragmentActivity activity;
    private EditText etFirstname;
    private EditText etLastname;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindPlayerFragment() {
        // Nothing to do
    }

    public static FindPlayerFragment newInstance() {
        FindPlayerFragment fragment = new FindPlayerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
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
        View view = inflater.inflate(R.layout.fragment_find_player, container, false);

        llMatch = view.findViewById(R.id.list);
        spGenre = view.findViewById(R.id.sp_genre);
        etFirstname = view.findViewById(R.id.et_firstname);
        etLastname = view.findViewById(R.id.et_lastname);
        pgPlayer = view.findViewById(R.id.progress_match);
        tvMessage = view.findViewById(R.id.tv_message);
        llMessage = view.findViewById(R.id.llMessage);
        llContent = view.findViewById(R.id.llContent);

        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT, mColumnCount);
        }

        initializeGenre();
        initializeMatch();
        initializeFabValidate();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        PlayerDto player = (PlayerDto) item;
        if (player.linkPalmares != null && !player.linkPalmares.isEmpty()) {
            String firstname = (player.firstname.length() > 1) ? player.firstname.substring(0, 1).toUpperCase() + player.firstname.substring(1).toLowerCase() : player.firstname.toLowerCase();
            String name = player.lastname.toUpperCase() + " " + firstname;
            MatchDto matchDto = new MatchDto(null,null, name,null,player.ranking,null,null,null,player.linkPalmares);
            FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance(matchDto, null));
        }
    }

    @Override
    public void onDestroyView() {
        if (mFindPlayerTask != null) {
            mFindPlayerTask.cancel(true);
        }
        super.onDestroyView();
    }

    private void initializeGenre() {
        Context context = getContext();
        assert context != null;

        List<String> listValue = new ArrayList<>();
        for(EnumPlayer.GENRE playerGenre : EnumPlayer.GENRE.values()) {
            listValue.add(playerGenre.label);
        }

        ArrayAdapter<String> adpGenre = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listValue);
        spGenre.setAdapter(adpGenre);
    }

    private void initializeMatch() {
        Context context = getContext();
        assert context != null;
        listPlayerDto.clear();
        // Set the adapter
        if (llMatch instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) llMatch;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adpPlayer = new FindPlayerAdapter(context, listPlayerDto, this);
            recyclerView.setAdapter(adpPlayer);
        }

        if (mFindPlayerTask != null) {
            mFindPlayerTask.cancel(true);
        }
    }

    private void findPlayer() {
        closeKeyboard();

        EnumPlayer.GENRE genre = EnumPlayer.GENRE.findByLabel(spGenre.getSelectedItem().toString());
        mFindPlayerTask = new MyFindPlayerTask(getContext(), genre, etFirstname.getText().toString(), etLastname.getText().toString());
        mFindPlayerTask.execute((Void) null);
    }

    private void closeKeyboard() {
        InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(llContent.getWindowToken(), 0);
    }

    private void showProgressMatch(final boolean show) {
        ProgressTool.showProgress(getContext(), llMatch, pgPlayer, show);
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

    private void initializeFabValidate() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(getActivity()), FragmentTool.INIT_FAB_IMAGE.VALIDATE);
        FragmentTool.onClickFab(getActivity(), this::onClickFindPlayer);
    }

    private void initializeFabHideMessage() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(activity), FragmentTool.INIT_FAB_IMAGE.BACK);
        FragmentTool.onClickFab(activity, v -> {
            hideMessage();
            initializeFabValidate();
        });
    }

    private void onClickFindPlayer(View view) {
        findPlayer();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyFindPlayerTask extends FindPlayerTask {

        private Context context;

        MyFindPlayerTask(Context context, EnumPlayer.GENRE genre, String firstname, String lastname) {
            super(context, genre, firstname, lastname);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideMessage();
            showProgressMatch(true);
            listPlayer.clear();
            listPlayerDto.clear();
            adpPlayer.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<FindPlayerResponse.PlayerItem> players) {
            super.onPostExecute(players);
            showProgressMatch(false);
            listPlayer.addAll(players);
            listPlayerDto.addAll(PlayerContent.toDto(players));
            PlayerContent.sortDefault(listPlayerDto);
            adpPlayer.notifyDataSetChanged();
            if (listPlayerDto.isEmpty()) {
//                initializeFabHideMessage();
                showMessage(R.string.msg_no_player_found);
            } else {
//                initializeFabValidate();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            NotificationManager.onTaskProcessUpdate(getActivity(), values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            logMe("MyFindPlayerTask cancelled.");
            showProgressMatch(false);
            initializeFabValidate();
            mFindPlayerTask = null;
        }
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
