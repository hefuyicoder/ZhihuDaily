package io.hefuyi.zhihudaily.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.R;

/**
 * Created by hefuyi on 16/8/4.
 */
public class StoryHeaderView extends RelativeLayout {
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.tvAuthor)
    TextView author;

    public StoryHeaderView(Context context) {
        this(context, null);
    }

    public StoryHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.view_header_story_height)));
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_header_story, this, true);
        ButterKnife.bind(this);
    }

    public void bindData(String title, String author, String url) {
        this.title.setText(title);
        if (TextUtils.isEmpty(author)) {
            this.author.setVisibility(View.GONE);
        } else {
            this.author.setVisibility(View.VISIBLE);
            this.author.setText(author);
        }
        Glide.with(getContext()).load(url).centerCrop().into(image);
    }

    public static StoryHeaderView newInstance(ViewGroup container) {
        return new StoryHeaderView(container.getContext());
    }
}
