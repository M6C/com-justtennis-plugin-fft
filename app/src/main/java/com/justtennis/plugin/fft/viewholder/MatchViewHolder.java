package com.justtennis.plugin.fft.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.MatchDto;

public class MatchViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final CheckBox mCheckView;
    public final TextView mDateView;
    public final TextView mVicDefView;
    public final TextView mNameView;
    public final TextView mRankingView;
    public final TextView mScoreView;
    public final TextView mPointsView;
    public MatchDto mItem;

    public MatchViewHolder(View view) {
        super(view);
        mView = view;
        mCheckView = view.findViewById(R.id.check);
        mDateView = view.findViewById(R.id.date);
        mVicDefView = view.findViewById(R.id.viDef);
        mNameView = view.findViewById(R.id.name);
        mRankingView = view.findViewById(R.id.ranking);
        mScoreView = view.findViewById(R.id.score);
        mPointsView = view.findViewById(R.id.points);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mDateView.getText() + "'";
    }
}
