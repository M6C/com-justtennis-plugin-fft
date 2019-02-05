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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cameleon.common.android.factory.FactoryDialog;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.adapter.FindCompetitionAdapter;
import com.justtennis.plugin.fft.common.FFTConfiguration;
import com.justtennis.plugin.fft.dto.CompetitionContent;
import com.justtennis.plugin.fft.dto.CompetitionDto;
import com.justtennis.plugin.fft.model.enums.EnumCompetition;
import com.justtennis.plugin.fft.query.response.FindCompetitionResponse;
import com.justtennis.plugin.fft.task.FindCompetitionTask;

import org.cameleon.android.common.ApplicationConfig;
import org.cameleon.android.common.manager.IMainManager;
import org.cameleon.android.common.manager.MainManager;
import org.cameleon.android.common.tool.FragmentTool;
import org.cameleon.android.common.tool.ProgressTool;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentInteractionListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class FindCompetitionFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String TAG = FindCompetitionFragment.class.getName();

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private List<FindCompetitionResponse.CompetitionItem> list = new ArrayList<>();
    private List<CompetitionDto> listDto = new ArrayList<>();
    private View llMatch;
    private Spinner spType;
    private FindCompetitionAdapter adp;
    private MyFindTask mFindTask;
    private ProgressBar progress;
    private TextView tvMessage;
    private View llMessage;
    private View llContent;
    private FragmentActivity activity;
    private EditText etCity;
    private EditText etName;
    private EditText etDateStart;
    private EditText etDateEnd;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindCompetitionFragment() {
        // Nothing to do
    }

    public static FindCompetitionFragment newInstance() {
        FindCompetitionFragment fragment = new FindCompetitionFragment();
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
        View view = inflater.inflate(R.layout.fragment_find_competition, container, false);

        llMatch = view.findViewById(R.id.list);
        spType = view.findViewById(R.id.sp_type);
        etCity = view.findViewById(R.id.et_city);
        etName = view.findViewById(R.id.et_name);
        etDateStart = view.findViewById(R.id.et_date_start);
        etDateEnd = view.findViewById(R.id.et_date_end);
        progress = view.findViewById(R.id.progress_match);
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

        etDateStart.setOnClickListener(this::onClickDate);
        etDateEnd.setOnClickListener(this::onClickDate);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        CompetitionDto dto = (CompetitionDto) item;
        if (dto.linkTournament != null && !dto.linkTournament.isEmpty()) {
//            String firstname = (dto.firstname.length() > 1) ? dto.firstname.substring(0, 1).toUpperCase() + dto.firstname.substring(1).toLowerCase() : dto.firstname.toLowerCase();
//            String name = dto.lastname.toUpperCase() + " " + firstname;
//            MatchDto matchDto = new MatchDto(null,null, name,null,dto.ranking,null,null,null,dto.linkPalmares);
//            FragmentTool.replaceFragment(activity, MillesimeMatchFragment.newInstance(matchDto, null));
        }
    }

    @Override
    public void onDestroyView() {
        if (mFindTask != null) {
            mFindTask.cancel(true);
        }
        super.onDestroyView();
    }

    private void initializeGenre() {
        Context context = getContext();
        assert context != null;

        List<String> listValue = new ArrayList<>();
        for(EnumCompetition.TYPE item : EnumCompetition.TYPE.values()) {
            listValue.add(item.label);
        }

        spType.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listValue));
    }

    private void initializeMatch() {
        Context context = getContext();
        assert context != null;
        listDto.clear();
        // Set the adapter
        if (llMatch instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) llMatch;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adp = new FindCompetitionAdapter(context, listDto, this);
            recyclerView.setAdapter(adp);
        }

        if (mFindTask != null) {
            mFindTask.cancel(true);
        }
    }
    private void onClickDate(View view) {
        FactoryDialog.getInstance().buildDatePickerDialog(activity, (dialog, view2, which) -> {
            DatePicker datePicker = (DatePicker)view2;

            Calendar calendar = GregorianCalendar.getInstance(ApplicationConfig.getLocal());
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            ((EditText)view).setText(FFTConfiguration.sdfFFT.format(calendar.getTime()));
        }, -1, new Date()).show();
    }

    private void find() {
        closeKeyboard();

        EnumCompetition.TYPE genre = EnumCompetition.TYPE.findByLabel(spType.getSelectedItem().toString());
        Date dateStart = getDate(etDateStart);
        Date dateEnd = getDate(etDateEnd);

        mFindTask = new MyFindTask(getContext(), genre, etCity.getText().toString(), etName.getText().toString(), dateStart, dateEnd);
        mFindTask.execute((Void) null);
    }

    private void closeKeyboard() {
        InputMethodManager imm =  (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(llContent.getWindowToken(), 0);
    }

    private Date getDate(EditText date) {
        String s = date.getText().toString();
        if (!s.isEmpty()) {
            try {
                return FFTConfiguration.sdfFFT.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void showProgressMatch(final boolean show) {
        ProgressTool.showProgress(getContext(), llMatch, progress, show);
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
        FragmentTool.onClickFab(getActivity(), this::onClickFab);
    }

    private void initializeFabHideMessage() {
        FragmentTool.initializeFabDrawable(Objects.requireNonNull(activity), FragmentTool.INIT_FAB_IMAGE.BACK);
        FragmentTool.onClickFab(activity, v -> {
            hideMessage();
            initializeFabValidate();
        });
    }

    private void onClickFab(View view) {
        find();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyFindTask extends FindCompetitionTask {

        private Context context;
        private String name;

        MyFindTask(Context context, EnumCompetition.TYPE type, String city, String name, Date dateStart, Date dateEnd) {
            super(context, type, city, dateStart, dateEnd);
            this.context = context;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideMessage();
            showProgressMatch(true);
            list.clear();
            listDto.clear();
            adp.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<FindCompetitionResponse.CompetitionItem> item) {
            super.onPostExecute(item);
            showProgressMatch(false);
            list.addAll(item);
            if (name.isEmpty()) {
                listDto.addAll(CompetitionContent.toDto(item));
            } else {
                // List filter
                String s = name.toLowerCase();
                for(FindCompetitionResponse.CompetitionItem i : item) {
                    if (i.toString().toLowerCase().contains(s)) {
                        listDto.add(CompetitionContent.toDto(i));
                    }
                }
            }
            CompetitionContent.sortDefault(listDto);
            adp.notifyDataSetChanged();
            if (listDto.isEmpty()) {
//                initializeFabHideMessage();
                showMessage(R.string.msg_no_player_found);
            } else {
//                initializeFabValidate();
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
            logMe("MyFindTask cancelled.");
            showProgressMatch(false);
            initializeFabValidate();
            mFindTask = null;
        }
    }

    private IMainManager getMainManager() {
        return MainManager.getInstance();
    }

    private static void logMe(String msg) {
        System.out.println(msg);
    }
}
