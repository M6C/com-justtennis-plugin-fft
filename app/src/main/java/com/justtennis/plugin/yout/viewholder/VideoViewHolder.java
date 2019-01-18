package com.justtennis.plugin.yout.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;
import com.justtennis.plugin.yout.adapter.YoutFindVideoListAdapter;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.squareup.picasso.Picasso;

public class VideoViewHolder extends RecyclerViewHolder<VideoDto> {

    private final TextView mMessage;
    private final TextView mDate;
    private final ProgressBar mProgress;
    private final ImageView mImageView;
    private final CheckBox mCheck;
    private View mView;
    private YoutFindVideoListAdapter adapter;

    public VideoViewHolder(View view, YoutFindVideoListAdapter adapter) {
        super(view);
        mMessage= view.findViewById(R.id.publication_message);
        mDate = view.findViewById(R.id.publication_date);
        mProgress = view.findViewById(R.id.publication_progress);
        mImageView = view.findViewById(R.id.imageView);
        mCheck = view.findViewById(R.id.check);
        this.mView = view;
        this.adapter = adapter;
    }

    @Override
    public void set(VideoDto dto) {
        super.set(dto); // Mandatory
        mView.setTag(dto);
        dto.viewHolder = this;

        mMessage.setText(dto.title);
        mDate.setText(dto.publishedTime);
        mProgress.setVisibility(View.GONE);
        mCheck.setVisibility(adapter.isShowCheck() ? View.VISIBLE : View.GONE);

        if (dto.type == MEDIA_TYPE.PLAYLIST) {
            mCheck.setEnabled(false);
            Picasso.get()
                    .load(R.drawable.ic_playlist)
                    .fit()
                    .into(mImageView);
        } else if (dto.type == MEDIA_TYPE.CHANNEL) {
            mCheck.setEnabled(false);
            Picasso.get()
                    .load(R.drawable.ic_channel)
                    .fit()
                    .into(mImageView);
        } else if (dto.thumbnails.size() > 0) {
            mCheck.setEnabled(true);
            Picasso.get()
                    .load(dto.thumbnails.get(0))
                    .fit()
                    .centerCrop()
                    .into(mImageView);
        }
    }

    public void check() {
        VideoDto dto = (VideoDto) mView.getTag();
        if (dto.type == MEDIA_TYPE.VIDEO) {
            mCheck.setChecked(!mCheck.isChecked());
        } else {
            mCheck.setChecked(false);
        }
    }
}
