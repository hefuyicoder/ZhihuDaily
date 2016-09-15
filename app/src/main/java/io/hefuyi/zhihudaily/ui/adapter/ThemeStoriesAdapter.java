package io.hefuyi.zhihudaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.mvp.model.Editor;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.ui.adapter.holder.StoryViewHolder;
import io.hefuyi.zhihudaily.util.UIUtils;
import io.hefuyi.zhihudaily.widget.AvatarsView;
import io.hefuyi.zhihudaily.widget.StoryHeaderView;

/**
 * Created by hefuyi on 16/8/6.
 */
public class ThemeStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Theme mTheme;

    public ThemeStoriesAdapter() {

    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;
        notifyDataSetChanged();
    }

    public void appendStories(List<Story> stories) {
        mTheme.getStories().addAll(stories);
        notifyDataSetChanged();
    }

    public static final class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_AVATARS = 1;
        public static final int TYPE_ITEM = 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case Type.TYPE_HEADER:
                viewHolder = new HeaderViewHolder(StoryHeaderView.newInstance(parent));
                break;
            case Type.TYPE_AVATARS:
//                AvatarsView avatarsView = new AvatarsView(parent.getContext());
//                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//                );
//                layoutParams.setMargins(0, DensityUtil.dip2px(parent.getContext(), 8), 0, 0);
//                avatarsView.setLayoutParams(layoutParams);
                AvatarsView avatarsView =(AvatarsView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_avatars, parent,false);
                viewHolder = new AvatarViewHolder(avatarsView);
                break;
            case Type.TYPE_ITEM:
                viewHolder = new StoryViewHolder(UIUtils.inflate(R.layout.recycler_item_story, parent));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Type.TYPE_HEADER:
                ((StoryHeaderView)holder.itemView).bindData(mTheme.getDescription(),null,mTheme.getBackground());
                break;
            case Type.TYPE_AVATARS:
                List<String> images = new ArrayList<>();
                for (Editor editor : mTheme.getEditors()) {
                    images.add(editor.getAvatar());
                }
                ((AvatarsView) holder.itemView).bindData(
                        holder.itemView.getResources().getString(R.string.avatar_title_editor), images);
                break;
            case Type.TYPE_ITEM:
                StoryViewHolder stotyViewHolder = (StoryViewHolder) holder;
                final int storyPosition = hasEditors() ? position - 2 : position - 1;
                stotyViewHolder.bindStoryView(mTheme.getStories().get(storyPosition));
                break;
            default:
                throw new IllegalArgumentException("error view type");
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mTheme != null) {
            if (!TextUtils.isEmpty(mTheme.getBackground())) {
                count += 1;
            }
            if (mTheme.getEditors() != null && mTheme.getEditors().size() > 0) {
                count += 1;
            }
            if (mTheme.getStories() != null) {
                count += mTheme.getStories().size();
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(mTheme.getBackground()) && position == 0) {
            return Type.TYPE_HEADER;
        } else if (hasEditors() && position == 1) {
            return Type.TYPE_AVATARS;
        } else {
            return Type.TYPE_ITEM;
        }
    }

    private boolean hasEditors() {
        return mTheme.getEditors() != null && mTheme.getEditors().size() > 0;
    }

    public static final class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static final class AvatarViewHolder extends RecyclerView.ViewHolder {
        public AvatarViewHolder(View itemView) {
            super(itemView);

        }
    }
}
