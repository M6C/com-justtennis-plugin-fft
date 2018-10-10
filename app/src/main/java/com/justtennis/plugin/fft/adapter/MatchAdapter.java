package com.justtennis.plugin.fft.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.cameleon.common.android.factory.FactoryDialog;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.MatchContent;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.fft.model.Invite;
import com.justtennis.plugin.fft.model.Player;
import com.justtennis.plugin.fft.model.Saison;
import com.justtennis.plugin.fft.resolver.InviteResolver;
import com.justtennis.plugin.fft.resolver.PlayerResolver;
import com.justtennis.plugin.fft.resolver.SaisonResolver;
import com.justtennis.plugin.fft.service.FFTService;
import com.justtennis.plugin.fft.viewholder.MatchViewHolder;

import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MatchDto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {

    private Context context;
    private final List<MatchDto> mValues;
    private final OnListFragmentInteractionListener mListener;
    private String millesime;
    private boolean canValidate;

    public MatchAdapter(Context context, List<MatchDto> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_match_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mItem.selected = !checkMatchExist(holder.mItem);

        holder.mCheckView.setVisibility(canValidate ? View.VISIBLE : View.GONE);
        holder.mCheckView.setChecked(holder.mItem.selected);
        holder.mDateView.setText(holder.mItem.date);
        holder.mVicDefView.setText(holder.mItem.vicDef);
        holder.mNameView.setText(holder.mItem.name);
        holder.mRankingView.setText(holder.mItem.ranking);
        holder.mScoreView.setText("Oui".equalsIgnoreCase(holder.mItem.wo) ? "WO" : holder.mItem.score);
        holder.mPointsView.setText(holder.mItem.points);

        holder.mScoreView.setVisibility(holder.mItem.score != null && !holder.mItem.score.isEmpty() ? View.VISIBLE : View.GONE);
        holder.mPointsView.setVisibility(holder.mItem.points != null && !holder.mItem.points.isEmpty() ? View.VISIBLE : View.GONE);

        holder.mView.setOnClickListener(v -> {
            holder.mCheckView.setChecked(!holder.mCheckView.isChecked());
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });

        holder.mCheckView.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked() && checkMatchExist(holder.mItem)) {
                FactoryDialog.getInstance()
                        .buildOkCancelDialog(context,
                                null,
                                (dialog, which) -> checkBox.setChecked(false),
                                R.string.dialog_confirm_check_match_already_exist_title,
                                R.string.dialog_confirm_check_match_already_exist_message)
                        .show();
            }
        });

        holder.mCheckView.setOnCheckedChangeListener((v, checked) -> holder.mItem.selected = checked);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private boolean checkMatchExist(MatchDto dto) {
        Long idSaison = null;
        List<Saison> listSaison = SaisonResolver.getInstance().queryNameEndWith(context, millesime);
        if (listSaison != null && !listSaison.isEmpty()) {
            idSaison = listSaison.get(0).getId();
        }
        List<Invite> listIvite = null;
        if (idSaison != null) {
            Long idPlayer = -1L;
            String[] firstlastname = MatchContent.getFirstLastName(dto.name);
            if (firstlastname != null) {
                List<Player> players = PlayerResolver.getInstance().queryByName(context, firstlastname[0], firstlastname[1]);
                if (players != null && !players.isEmpty()) {
                    idPlayer = players.get(0).getId();
                }
            }

            if (idPlayer >=0) {
                Date date = FFTService.getDateFromFFT(dto.date);
                String scoreResult = FFTService.getScoreResultFromFFT(dto.vicDef).toString();
                listIvite = InviteResolver.getInstance().queryInvite(context, idSaison, idPlayer, date, scoreResult);
            }
        }
        return (listIvite != null && !listIvite.isEmpty());
    }

    public void setMillesime(String millesime) {
        this.millesime = millesime;
    }

    public void setCanValidate(boolean canValidate) {
        this.canValidate = canValidate;
    }
}
