package io.hefuyi.zhihudaily.ui.adapter.holder;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.util.IntentUtils;
import io.hefuyi.zhihudaily.util.ListUtils;
import io.hefuyi.zhihudaily.widget.CirclePageIndicator;
import io.hefuyi.zhihudaily.widget.MyViewPager;
import io.hefuyi.zhihudaily.widget.StoryHeaderView;

/**
 * Created by hefuyi on 16/8/4.
 */
public class HeaderViewPagerHolder extends RecyclerView.ViewHolder {

    private final MyViewPager mViewPager;
    private final CirclePageIndicator mPageIndicator;
    private PagerAdapter mPagerAdapter;

    public HeaderViewPagerHolder(View itemView, List<Story> stories) {
        super(itemView);

        mViewPager = (MyViewPager) itemView.findViewById(R.id.viewPager);
        mPageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
        if (ListUtils.isEmpty(stories)) {
            return;
        } else if (stories.size() < 2) {
            mPageIndicator.setVisibility(View.GONE);
        }
        mPagerAdapter = new HeaderPagerAdapter(stories);
    }

    public void bindHeaderView(){
        if (mViewPager.getAdapter() == null) {
            mViewPager.setAdapter(mPagerAdapter);
            mPageIndicator.setViewPager(mViewPager);
        } else {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    public boolean isAutoScrolling() {
        if (mViewPager != null) {
            return mViewPager.isAutoScrolling();
        }
        return false;
    }

    public void stopAutoScroll() {
        if (mViewPager != null) {
            mViewPager.stopAutoScroll();
        }
    }

    public void startAutoScroll() {
        if (mViewPager != null) {
            mViewPager.startAutoScroll();
        }
    }

    private final static class HeaderPagerAdapter extends PagerAdapter {
        private final List<Story> mStories;

        private int mChildCount;

        public HeaderPagerAdapter(List<Story> stories) {
            mStories = stories;
        }

        @Override
        public int getCount() {
            return mStories == null ? 0 : mStories.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            StoryHeaderView storyHeaderView = StoryHeaderView.newInstance(container);
            final Story story = mStories.get(position);
            storyHeaderView.bindData(story.getTitle(), story.getImageSource(), story.getImage());
            storyHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.intentToStoryActivity((Activity) v.getContext(), story);
                }
            });
            container.addView(storyHeaderView);
            return storyHeaderView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((StoryHeaderView) object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            mChildCount = getCount();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

    }

}
