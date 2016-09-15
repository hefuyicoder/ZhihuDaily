package io.hefuyi.zhihudaily.ui.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.injector.component.DaggerThemeStoryComponent;
import io.hefuyi.zhihudaily.injector.component.ThemeStoryComponent;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.ThemeStoryModule;
import io.hefuyi.zhihudaily.mvp.contract.ThemeStoryContract;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.ui.activity.NavigationDrawerActivity;
import io.hefuyi.zhihudaily.ui.adapter.ThemeStoriesAdapter;
import io.hefuyi.zhihudaily.util.L;
import io.hefuyi.zhihudaily.widget.AvatarsView;
import io.hefuyi.zhihudaily.widget.LoadMoreRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThemeStoriesFragment extends BaseFragment implements ThemeStoryContract.View{
    public static final String TAG = ThemeStoriesFragment.class.getSimpleName();

    @Inject
    ThemeStoryContract.Presenter themeStoryPresenter;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;

    private ThemeStoriesAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mThemeId;
    private String mLastStoryId;
    private boolean isDataLoaded;

    /*初始化相关*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreate  " + this);
        super.onCreate(savedInstanceState);
        injectDependences();
        themeStoryPresenter.attachView(this);
        mAdapter = new ThemeStoriesAdapter();
    }

    private void injectDependences() {
        ApplicationComponent applicationComponent = ((NavigationDrawerActivity) getActivity()).getApplicationComponent();
        ThemeStoryComponent themeStoryComponent = DaggerThemeStoryComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .themeStoryModule(new ThemeStoryModule())
                .build();
        themeStoryComponent.inject(this);
    }
    /*初始化相关*/

    /*view接口*/
    @Override
    public void showError(final String errorString) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showRefreshing() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void hideRefreshing() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showStory(final Theme theme) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (theme == null) {
                    isDataLoaded = false;
                } else {
                    isDataLoaded = true;
                    if (theme.getStories().size() > 0) {
                        mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                    }
                    L.i(TAG, "last story id: " + mLastStoryId);
                    mAdapter.setTheme(theme);
                }
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void appendStory(final Theme theme) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (theme.getStories().size() > 0) {
                    mLastStoryId = theme.getStories().get(theme.getStories().size() - 1).getId();
                    mAdapter.appendStories(theme.getStories());
                }
            }
        };
        getActivity().runOnUiThread(runnable);
    }
    /*view接口*/

    /*普通逻辑*/
    @Override
    public void refreshUI() {
        TypedValue itemStoryTextColor = new TypedValue();
        TypedValue itemStoryBackground = new TypedValue();
        TypedValue windowBackground = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.item_story_text_color, itemStoryTextColor, true);
        theme.resolveAttribute(R.attr.item_story_background_color, itemStoryBackground, true);
        theme.resolveAttribute(R.attr.windowBackground, windowBackground, true);

        Resources resources = getResources();
        View window=((ViewGroup) getActivity().getWindow().getDecorView());
        window.setBackgroundColor(resources.getColor(windowBackground.resourceId));
        int childCount = recyclerView.getChildCount();
        int firstVisible = mLinearLayoutManager.findFirstVisibleItemPosition();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            int viewType = mAdapter.getItemViewType(childIndex + firstVisible);
            switch (viewType) {
                case ThemeStoriesAdapter.Type.TYPE_HEADER:
                    break;
                case ThemeStoriesAdapter.Type.TYPE_AVATARS:
                    AvatarsView avatarsView = (AvatarsView) recyclerView.getChildAt(childIndex);
                    avatarsView.setBackgroundColor(resources.getColor(windowBackground.resourceId));
                    break;
                case ThemeStoriesAdapter.Type.TYPE_ITEM:
                    CardView cardView = (CardView) recyclerView.getChildAt(childIndex);
                    cardView.setCardBackgroundColor(resources.getColor(itemStoryBackground.resourceId));
                    TextView title = (TextView) cardView.findViewById(R.id.title);
                    title.setTextColor(resources.getColor(itemStoryTextColor.resourceId));
                    break;
            }
        }
        invalidateCacheItem();
    }

    /**
     * 使RecyclerView缓存中在pool中的Item失效
     */
    private void invalidateCacheItem() {
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void smoothToTop() {
        int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition < 11) {
            recyclerView.smoothScrollToPosition(0);
        } else {
            recyclerView.smoothScrollToPosition(10);
            recyclerView.smoothScrollToPosition(0);
        }
        recyclerView.clearFocus();
    }
    /*普通逻辑*/

    /*生命周期回调*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.i(TAG, "onCreateView  " + this);
        L.i(TAG, getThemeNumber() + " : " + getThemeId());
        View viewRoot= inflater.inflate(R.layout.fragment_theme_stories, container, false);
        ButterKnife.bind(this, viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        L.i(TAG, "onViewCreated  " + this);
        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light, android.R.color.holo_green_dark,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isDataLoaded = false;
                themeStoryPresenter.refresh(mThemeId);
            }
        });

        recyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.setLoadingMore(true);
                themeStoryPresenter.loadMore(mThemeId,mLastStoryId);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        L.i(TAG, "onActivityCreated  " + this);
        super.onActivityCreated(savedInstanceState);
        mThemeId = getThemeId();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!isDataLoaded) {
                    themeStoryPresenter.refresh(mThemeId);
                }
            }
        });
    }
    /*生命周期回调*/
}
