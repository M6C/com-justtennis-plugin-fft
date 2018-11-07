package com.justtennis.plugin.fb.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fb.dto.PublicationDto;
import com.justtennis.plugin.fb.viewholder.PublicationViewHolder;
import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewAdapter;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;

import java.util.function.Consumer;

public class PublicationListAdapter extends RecyclerViewAdapter<PublicationDto, RecyclerViewHolder<PublicationDto>> {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PublicationListAdapter(@Nullable Consumer<PublicationDto> listener) {
        super(listener);
    }

    @Override
    protected Comparator<PublicationDto> getComparator() {
        return new Comparator<PublicationDto>() {
            @Override
            public boolean areItemsTheSame(PublicationDto oldItem, PublicationDto newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(PublicationDto oldItem, PublicationDto newItem) {
                return oldItem.message.equals(newItem.message);
            }
        };
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fb_publication_list_content, parent, false);
        return new PublicationViewHolder(view);
    }

}