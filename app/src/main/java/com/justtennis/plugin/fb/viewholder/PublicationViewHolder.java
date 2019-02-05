package com.justtennis.plugin.fb.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justtennis.plugin.fb.dto.PublicationDto;
import com.justtennis.plugin.fb.enums.STATUS_PUBLICATION;
import com.justtennis.plugin.fft.R;
import org.cameleon.android.shared.adapter.RecyclerViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PublicationViewHolder extends RecyclerViewHolder<PublicationDto> {

    private static final DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();

    private final TextView mMessage;
    private final TextView mDate;
    private final ProgressBar mProgress;
    private View mView;

    public PublicationViewHolder(View view) {
        super(view);
        mMessage= view.findViewById(R.id.publication_message);
        mDate = view.findViewById(R.id.publication_date);
        mProgress = view.findViewById(R.id.publication_progress);
        this.mView = view;
    }

    @Override
    public void set(PublicationDto pub) {
        super.set(pub); // Mandatory
        mView.setTag(pub);

        mMessage.setText(pub.message);
        mDate.setText(pub.postDate != null ? dateTimeInstance.format(pub.postDate) : "");
        mProgress.setVisibility(pub.statusPublication == STATUS_PUBLICATION.PENDING ? View.VISIBLE : View.GONE);
    }
}
