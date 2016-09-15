package io.hefuyi.zhihudaily.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import io.hefuyi.zhihudaily.util.L;

/**
 * Created by hefuyi on 16/8/4.
 */
public class LoadMoreRecyclerView extends RecyclerView {
    private static final String TAG = LoadMoreRecyclerView.class.getSimpleName();
    private boolean mIsLoadingMore = false;
    private onLoadMoreListener mListener;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        this.setOnScrollListener(new onLoadMoreScrollListener());
    }

    private static class onLoadMoreScrollListener extends OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LoadMoreRecyclerView view = (LoadMoreRecyclerView) recyclerView;
            onLoadMoreListener onLoadMoreListener = view.getOnLoadMoreListener();

            onLoadMoreListener.onScrolled(recyclerView, dx, dy);

            //if scroll to bottom
            LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int itemCount = layoutManager.getItemCount();
            if (lastVisibleItem >= itemCount - 1 && !view.getLoadingMore()) {
                onLoadMoreListener.onLoadMore();
                L.i(TAG, "load more: lastVisibleItem = " + lastVisibleItem + ", itemCount " + itemCount);
            } else {
                super.onScrolled(recyclerView, dx, dy);
            }
        }
    }

    public void setOnLoadMoreListener(onLoadMoreListener listener) {
        this.mListener = listener;
        init();
    }


    private onLoadMoreListener getOnLoadMoreListener() {
        return this.mListener;
    }

    public void setLoadingMore(boolean isLoading) {
        this.mIsLoadingMore = isLoading;
    }

    private boolean getLoadingMore() {
        return mIsLoadingMore;
    }

    public interface onLoadMoreListener {
        void onLoadMore();

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException("LoadMoreRecyclerView must have a LinearLayoutManager!");
        }
    }
}
