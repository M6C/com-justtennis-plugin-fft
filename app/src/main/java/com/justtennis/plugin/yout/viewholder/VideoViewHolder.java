package com.justtennis.plugin.yout.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.shared.adapter.RecyclerViewHolder;
import com.justtennis.plugin.shared.interfaces.interfaces.OnListFragmentInteractionListener;
import com.justtennis.plugin.yout.adapter.YoutFindVideoListAdapter;
import com.justtennis.plugin.yout.dto.VideoDto;
import com.justtennis.plugin.yout.enums.MEDIA_TYPE;
import com.squareup.picasso.Picasso;

public class VideoViewHolder extends RecyclerViewHolder<VideoDto> {

    private final TextView mMessage;
    private final TextView mDate;
    private final TextView mLength;
    private final View mProgress;
    private final ImageView mImageView;
    private final CheckBox mCheck;
    private final TextView mProgressTv;
    private final View mView;
    private final YoutFindVideoListAdapter adapter;
    private OnListFragmentInteractionListener checkListener;
    private OnListFragmentInteractionListener checkLongClickListener;

    public VideoViewHolder(View view, YoutFindVideoListAdapter adapter, OnListFragmentInteractionListener checkListener, OnListFragmentInteractionListener checkLongClickListener) {
        super(view);
        mMessage= view.findViewById(R.id.publication_message);
        mDate = view.findViewById(R.id.publication_date);
        mLength = view.findViewById(R.id.publication_length);
        mProgress = view.findViewById(R.id.download_progress);
        mProgressTv = view.findViewById(R.id.progressTv);
        mImageView = view.findViewById(R.id.imageView);
        mCheck = view.findViewById(R.id.check);
        this.mView = view;
        this.adapter = adapter;
        this.checkListener = checkListener;
        this.checkLongClickListener = checkLongClickListener;
    }

    @Override
    public void set(VideoDto dto) {
        super.set(dto); // Mandatory
        mView.setTag(dto);
        dto.viewHolder = this;

        mMessage.setText(dto.title);
        mDate.setText(dto.publishedTime);
        mDate.setVisibility(dto.type == MEDIA_TYPE.VIDEO ? View.VISIBLE : View.INVISIBLE);
        mLength.setText(dto.length);
        mProgress.setVisibility(View.GONE);

        mCheck.setVisibility(adapter.isShowCheck() ? View.VISIBLE : View.GONE);
        check(dto.checked);

        if (dto.type == MEDIA_TYPE.PLAYLIST) {
            enableCheck(false);
            Picasso.get()
                    .load(R.drawable.ic_playlist)
                    .fit()
                    .into(mImageView);
            mCheck.setOnCheckedChangeListener(null);
        } else if (dto.type == MEDIA_TYPE.CHANNEL) {
            enableCheck(false);
            Picasso.get()
                    .load(R.drawable.ic_channel)
                    .fit()
                    .into(mImageView);
            mCheck.setOnCheckedChangeListener(null);
        } else if (!dto.thumbnails.isEmpty()) {
            enableCheck(true);
            Picasso.get()
                    .load(dto.thumbnails.get(0))
                    .fit()
                    .centerCrop()
                    .into(mImageView);

            mCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                dto.checked = isChecked;
                checkListener.onListFragmentInteraction(dto);
            });
            mCheck.setOnLongClickListener(v -> {
                checkLongClickListener.onListFragmentInteraction(dto);
                return true;
            });
        }
    }

    public void check() {
        VideoDto dto = (VideoDto) mView.getTag();
        if (dto.type == MEDIA_TYPE.VIDEO) {
            check(!mCheck.isChecked());
        } else {
            check(false);
        }
    }

    public void updateDownloadStatus(VideoDto.STATUS_DOWNLOAD status) {
        switch (status) {
            case PENDING:
            case DOWNLOADING:
                enableCheck(false);
                mProgress.setVisibility(View.VISIBLE);
                mProgressTv.setText(status == VideoDto.STATUS_DOWNLOAD.PENDING ? R.string.yout_waiting_video : R.string.yout_downloading_video);
                break;
            case DOWNLOADED:
            case DOWNLOAD_ERROR:
                enableCheck(true);
                check(true);
                mProgress.setVisibility(View.GONE);
                break;
            case NO:
            default:
                enableCheck(true);
                mProgress.setVisibility(View.GONE);
                break;
        }
    }

    private void check(boolean b) {
        mCheck.setChecked(b);
    }

    private void enableCheck(boolean b) {
        mCheck.setEnabled(b);
    }
}
