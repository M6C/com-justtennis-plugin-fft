package com.justtennis.plugin.yout.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justtennis.plugin.fft.R;
import org.cameleon.android.shared.adapter.RecyclerViewAdapter;
import org.cameleon.android.shared.adapter.RecyclerViewHolder;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentCheckListener;
import org.cameleon.android.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.viewholder.VideoViewHolder;

import java.util.function.Consumer;

public class YoutFindVideoListAdapter extends RecyclerViewAdapter<VideoDto, RecyclerViewHolder<VideoDto>> {

    private final OnListFragmentInteractionListener longClickListener;
    private final OnListFragmentCheckListener checkListener;
    private final OnListFragmentInteractionListener checkLongClickListener;
    private OnListFragmentInteractionListener mListener;

    private boolean showCheck;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public YoutFindVideoListAdapter(@Nullable Consumer<VideoDto> listener, OnListFragmentInteractionListener longClickListener, OnListFragmentCheckListener checkListener, OnListFragmentInteractionListener checkLongClickListener) {
        super(listener);
        this.longClickListener = longClickListener;
        this.checkListener = checkListener;
        this.checkLongClickListener = checkLongClickListener;
    }

    public YoutFindVideoListAdapter(OnListFragmentInteractionListener listener, OnListFragmentInteractionListener longClickListener, OnListFragmentCheckListener checkListener, OnListFragmentInteractionListener checkLongClickListener) {
        super(null);
        mListener = listener;
        this.longClickListener = longClickListener;
        this.checkListener = checkListener;
        this.checkLongClickListener = checkLongClickListener;
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
            if (null != mListener) {
                view.setOnClickListener(v -> {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(view.getTag());
                });
            }
        }

        view.setLongClickable(true);
        if (longClickListener  != null) {
            view.setOnLongClickListener(v -> {
                longClickListener.onListFragmentInteraction(view.getTag());
                return true;
            });
        }

        return new VideoViewHolder(view, this, checkListener, checkLongClickListener);
    }

    public boolean isShowCheck() {
        return showCheck;
    }

    public void setShowCheck(boolean showCheck) {
        this.showCheck = showCheck;
    }
}