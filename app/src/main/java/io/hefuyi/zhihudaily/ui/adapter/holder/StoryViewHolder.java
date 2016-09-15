package io.hefuyi.zhihudaily.ui.adapter.holder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.util.IntentUtils;

/**
 * Created by hefuyi on 16/8/4.
 */
public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final CardView mCardView;
    private final TextView tvTitle;
    private final ImageView ivStoryImage;
    private final ImageView ivMultiPic;
    private Story mStory;
    private Context mContext;

    public StoryViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
        ivStoryImage = (ImageView) itemView.findViewById(R.id.image);
        ivMultiPic = (ImageView) itemView.findViewById(R.id.multiPic);
        mCardView.setOnClickListener(this);
        mContext = itemView.getContext();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.card) {
            IntentUtils.intentToStoryActivity((Activity) v.getContext(), mStory);
        }
    }

    public void bindStoryView(Story story) {
        mStory = story;
        tvTitle.setText(mStory.getTitle());
        String imageUrl = mStory.getImages() == null ? "" : mStory.getImages().get(0);
        if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(mStory.getMultiPic())) {
            ivMultiPic.setVisibility(View.GONE);
        } else if (Boolean.valueOf(mStory.getMultiPic())) {
            ivMultiPic.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(imageUrl)) {
            ivStoryImage.setVisibility(View.GONE);
        } else {
            ivStoryImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(imageUrl).centerCrop().into(ivStoryImage);
        }
    }
}
