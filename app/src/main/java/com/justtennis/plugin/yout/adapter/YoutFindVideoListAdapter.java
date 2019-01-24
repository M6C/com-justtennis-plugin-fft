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

    private final OnListFragmentInteractionListener longClickListener;
    private OnListFragmentInteractionListener checkListener;
    private OnListFragmentInteractionListener mListener;

    private boolean showCheck;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public YoutFindVideoListAdapter(@Nullable Consumer<VideoDto> listener, OnListFragmentInteractionListener longClickListener, OnListFragmentInteractionListener checkListener) {
        super(listener);
        this.longClickListener = longClickListener;
        this.checkListener = checkListener;
    }

    public YoutFindVideoListAdapter(OnListFragmentInteractionListener listener, OnListFragmentInteractionListener longClickListener, OnListFragmentInteractionListener checkListener) {
        super(null);
        mListener = listener;
        this.longClickListener = longClickListener;
        this.checkListener = checkListener;
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

        view.setLongClickable(true);
        view.setOnLongClickListener(v -> {
            longClickListener.onListFragmentInteraction(view.getTag());
            return true;
        });

        return new VideoViewHolder(view, this, checkListener);
    }

    public boolean isShowCheck() {
        return showCheck;
    }

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }
}