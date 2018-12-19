package com.justtennis.plugin.fft.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.CompetitionDto;
import com.justtennis.plugin.fft.dto.PlayerDto;

public class CompetitionViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mTypeView;
    public final TextView mLeagueView;
    public final TextView mClubView;
    public final TextView mNameView;
    public final TextView mDateStart;
    public final TextView mDateEnd;
    public CompetitionDto mItem;

    public CompetitionViewHolder(View view) {
        super(view);
        mView = view;
        mTypeView = view.findViewById(R.id.type);
        mLeagueView = view.findViewById(R.id.league);
        mClubView = view.findViewById(R.id.club);
        mNameView = view.findViewById(R.id.name);
        mDateStart = view.findViewById(R.id.dateStart);
        mDateEnd = view.findViewById(R.id.dateEnd);
    }

    @Override
    public String toString() {
        return "CompetitionViewHolder{" +
                "mTypeView=" + mTypeView.getText() +
                ", mLeagueView=" + mLeagueView.getText() +
                ", mClubView=" + mClubView.getText() +
                ", mNameView=" + mNameView.getText() +
                ", mDateStart=" + mDateStart.getText() +
                ", mDateEnd=" + mDateEnd.getText() +
                ", mItem=" + mItem +
                '}';
    }
}
