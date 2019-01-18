package com.justtennis.plugin.yout.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.squareup.picasso.Picasso;

public class VideoViewHolder extends RecyclerViewHolder<VideoDto> {

    private final TextView mMessage;
    private final TextView mDate;
    private final ProgressBar mProgress;
    private final ImageView mImageView;
    private View mView;

    public VideoViewHolder(View view) {
        super(view);
        mMessage= view.findViewById(R.id.publication_message);
        mDate = view.findViewById(R.id.publication_date);
        mProgress = view.findViewById(R.id.publication_progress);
        mImageView = view.findViewById(R.id.imageView);
        this.mView = view;
    }

    @Override
    public void set(VideoDto dto) {
        super.set(dto); // Mandatory
        mView.setTag(dto);

        mMessage.setText(dto.title);
        mDate.setText(dto.publishedTime);
        mProgress.setVisibility(View.GONE);

        if (dto.type == MEDIA_TYPE.PLAYLIST) {
            Picasso.get()
                    .load(R.drawable.ic_playlist)
                    .fit()
                    .into(mImageView);
        } else if (dto.type == MEDIA_TYPE.CHANNEL) {
            Picasso.get()
                    .load(R.drawable.ic_channel)
                    .fit()
                    .into(mImageView);
        } else if (dto.thumbnails.size() > 0) {
            Picasso.get()
                    .load(dto.thumbnails.get(0))
                    .fit()
                    .centerCrop()
                    .into(mImageView);
        }
    }
}
