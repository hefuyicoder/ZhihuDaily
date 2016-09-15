package io.hefuyi.zhihudaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.ui.adapter.holder.DateViewHolder;
import io.hefuyi.zhihudaily.ui.adapter.holder.HeaderViewPagerHolder;
import io.hefuyi.zhihudaily.ui.adapter.holder.StoryViewHolder;
import io.hefuyi.zhihudaily.util.UIUtils;

/**
 * Created by hefuyi on 16/8/4.
 */
public class DailyStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DailyStoriesAdapter.class.getSimpleName();
    private final List<Item> mItems;
    private final List<Item> mTmpItems;

    public static class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_DATE = 1;
        public static final int TYPE_STORY = 2;
    }

    public DailyStoriesAdapter() {
        mItems = new ArrayList<>();
        mTmpItems = new ArrayList<>();
    }

    public void setList(DailyStories dailyStories) {
        mItems.clear();
        appendList(dailyStories);
    }

    public void appendList(DailyStories dailyStories) {
        int positionStart = mItems.size();

        if (positionStart == 0) {
            Item header = new Item();
            header.setType(Type.TYPE_HEADER);
            header.setStories(dailyStories.getTopStories());
            mItems.add(header);
        }
        Item dateItem = new Item();
        dateItem.setType(Type.TYPE_DATE);
        dateItem.setDate(dailyStories.getDate());
        mItems.add(dateItem);

        List<Story> stories = dailyStories.getStories();
        for (Story story : stories) {
            Item storyItem = new Item();
            storyItem.setType(Type.TYPE_STORY);
            storyItem.setStory(story);
            mItems.add(storyItem);
        }

        int itemCount = mItems.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        }else{
            notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof HeaderViewPagerHolder) {
            HeaderViewPagerHolder pagerHolder = (HeaderViewPagerHolder) holder;
            if (pagerHolder.isAutoScrolling()) {
                pagerHolder.stopAutoScroll();
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof HeaderViewPagerHolder) {
            HeaderViewPagerHolder pagerHolder = (HeaderViewPagerHolder) holder;
            if (!pagerHolder.isAutoScrolling()) {
                pagerHolder.startAutoScroll();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Type.TYPE_HEADER:
                itemView = UIUtils.inflate(R.layout.recycler_header_viewpager, parent);
                return new HeaderViewPagerHolder(itemView, mItems.get(0).getStories());
            case Type.TYPE_DATE:
                itemView = UIUtils.inflate(R.layout.recycler_item_date, parent);
                return new DateViewHolder(itemView);
            case Type.TYPE_STORY:
                itemView = UIUtils.inflate(R.layout.recycler_item_story, parent);
                return new StoryViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Item item = mItems.get(position);
        switch (viewType) {
            case Type.TYPE_HEADER:
                ((HeaderViewPagerHolder) holder).bindHeaderView();
                break;
            case Type.TYPE_DATE:
                ((DateViewHolder) holder).bindDate(item.getDate());
                break;
            case Type.TYPE_STORY:
                ((StoryViewHolder) holder).bindStoryView(item.getStory());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    public String getTitleBeforePosition(int position) {
        mTmpItems.clear();
        mTmpItems.addAll(mItems.subList(0, position + 1));
        Collections.reverse(mTmpItems);
        for (Item item : mTmpItems) {
            if (item.getType() == Type.TYPE_DATE) {
                return item.getDate();
            }
        }
        return "";
    }

    public static class Item {
        private int type;
        private String date;
        private Story story;
        private List<Story> stories;

        public int getType() {
            return type;
        }

        public String getDate() {
            return date;
        }

        public Story getStory() {
            return story;
        }

        public List<Story> getStories() {
            return stories;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setStory(Story story) {
            this.story = story;
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
        }
    }

}
