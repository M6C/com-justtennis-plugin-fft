package com.justtennis.plugin.fft.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.PlayerDto;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mCivilityView;
    public final TextView mFirstnameView;
    public final TextView mLastnameView;
    public final TextView mYearView;
    public final TextView mRankingView;
    public final TextView mBestRankingView;
    public final TextView mLicenceView;
    public final TextView mClubView;
    public PlayerDto mItem;

    public PlayerViewHolder(View view) {
        super(view);
        mView = view;
        mCivilityView = view.findViewById(R.id.civility);
        mFirstnameView = view.findViewById(R.id.firstname);
        mLastnameView = view.findViewById(R.id.lastname);
        mYearView = view.findViewById(R.id.year);
        mRankingView = view.findViewById(R.id.ranking);
        mBestRankingView = view.findViewById(R.id.bestRanking);
        mLicenceView = view.findViewById(R.id.licence);
        mClubView = view.findViewById(R.id.club);
    }

    @Override
    public String toString() {
        return super.toString() +
                " '" + mCivilityView.getText() + "'" +
                " '" + mFirstnameView.getText() + "'" +
                " '" + mLastnameView.getText() + "'" +
                " '" + mYearView.getText() + "'" +
                " '" + mRankingView.getText() + "'" +
                " '" + mBestRankingView.getText() + "'" +
                " '" + mLicenceView.getText() + "'" +
                " '" + mClubView.getText() + "'";
    }
}
