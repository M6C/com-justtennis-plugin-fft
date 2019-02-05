package com.justtennis.plugin.fft.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.CompetitionDto;
import com.justtennis.plugin.fft.viewholder.CompetitionViewHolder;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CompetitionDto} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FindCompetitionAdapter extends RecyclerView.Adapter<CompetitionViewHolder> {

    private Context context;
    private final List<CompetitionDto> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FindCompetitionAdapter(Context context, List<CompetitionDto> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_find_competition_item, parent, false);
        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompetitionViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mTypeView.setText(holder.mItem.type);
        holder.mLeagueView.setText(holder.mItem.league);
        holder.mClubView.setText(holder.mItem.club);
        holder.mNameView.setText(holder.mItem.name);
        holder.mDateStart.setText(holder.mItem.dateStart);
        holder.mDateEnd.setText(holder.mItem.dateEnd);
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
