package com.justtennis.plugin.fft.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.fragment.MatchFragment.OnListFragmentInteractionListener;
import com.justtennis.plugin.fft.viewholder.MatchViewHolder;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MatchDto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {

    private final List<MatchDto> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MatchAdapter(List<MatchDto> items, OnListFragmentInteractionListener listener) {
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
        holder.mContentView.setText(holder.mItem.content);
        holder.mDetailView.setText(holder.mItem.details);

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
