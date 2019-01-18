package com.justtennis.plugin.yout.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;
import com.justtennis.plugin.yout.dto.VideoDto;

public class VideoViewHolder extends RecyclerViewHolder<VideoDto> {

    private final TextView mMessage;
    private final TextView mDate;
    private final ProgressBar mProgress;
    private View mView;

    public VideoViewHolder(View view) {
        super(view);
        mMessage= view.findViewById(R.id.publication_message);
        mDate = view.findViewById(R.id.publication_date);
        mProgress = view.findViewById(R.id.publication_progress);
        this.mView = view;
    }

    @Override
    public void set(VideoDto dto) {
        super.set(dto); // Mandatory
        mView.setTag(dto);

        mMessage.setText(dto.title);
        mDate.setText(dto.publishedTime);
        mProgress.setVisibility(View.GONE);
    }
}
