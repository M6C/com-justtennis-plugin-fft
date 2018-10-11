package com.justtennis.plugin.fft.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.PlayerDto;
import com.justtennis.plugin.fft.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.fft.viewholder.PlayerViewHolder;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlayerDto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FindPlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    private Context context;
    private final List<PlayerDto> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FindPlayerAdapter(Context context, List<PlayerDto> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_find_player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mCivilityView.setText(holder.mItem.civility);
        holder.mFirstnameView.setText(holder.mItem.firstname);
        holder.mLastnameView.setText(holder.mItem.lastname);
        holder.mYearView.setText(holder.mItem.year);
        holder.mRankingView.setText(holder.mItem.ranking);
        holder.mBestRankingView.setText(holder.mItem.bestRanking);
        holder.mLicenceView.setText(holder.mItem.licence);
        holder.mClubView.setText(holder.mItem.club);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
