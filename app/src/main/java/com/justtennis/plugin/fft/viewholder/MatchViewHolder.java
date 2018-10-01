package com.justtennis.plugin.fft.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.MatchDto;

public class MatchViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mContentView;
    public final TextView mDetailView;
    public final CheckBox mCheckView;
    public MatchDto mItem;

    public MatchViewHolder(View view) {
        super(view);
        mView = view;
        mContentView = view.findViewById(R.id.content);
        mDetailView = view.findViewById(R.id.detail);
        mCheckView = view.findViewById(R.id.check);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}
