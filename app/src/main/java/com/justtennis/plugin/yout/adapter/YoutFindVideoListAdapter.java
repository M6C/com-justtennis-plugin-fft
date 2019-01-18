package com.justtennis.plugin.yout.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewAdapter;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;
import com.justtennis.plugin.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.viewholder.VideoViewHolder;

import java.util.function.Consumer;

public class YoutFindVideoListAdapter extends RecyclerViewAdapter<VideoDto, RecyclerViewHolder<VideoDto>> {

    private OnListFragmentInteractionListener mListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public YoutFindVideoListAdapter(@Nullable Consumer<VideoDto> listener) {
        super(listener);
    }

    public YoutFindVideoListAdapter(OnListFragmentInteractionListener listener) {
        super(null);
        mListener = listener;
    }

    @Override
    protected Comparator<VideoDto> getComparator() {
        return new Comparator<VideoDto>() {
            @Override
            public boolean areItemsTheSame(VideoDto oldItem, VideoDto newItem) {
                return oldItem.id.equals(newItem.id);
            }

            @Override
            public boolean areContentsTheSame(VideoDto oldItem, VideoDto newItem) {
                return oldItem.id.equals(newItem.id);
            }
        };
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yout_video_list_content, parent, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            view.setOnClickListener(v -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(view.getTag());
                }
            });
        }
        return new VideoViewHolder(view);
    }

}