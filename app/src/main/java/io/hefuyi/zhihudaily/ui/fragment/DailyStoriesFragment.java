package io.hefuyi.zhihudaily.ui.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import io.hefuyi.zhihudaily.injector.component.DaggerDailyStoryComponent;
import io.hefuyi.zhihudaily.injector.component.DailyStoryComponent;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.DailyStoryModule;
import io.hefuyi.zhihudaily.mvp.contract.DailyStoryContract;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.ui.activity.NavigationDrawerActivity;
import io.hefuyi.zhihudaily.ui.adapter.DailyStoriesAdapter;
import io.hefuyi.zhihudaily.ui.adapter.holder.DateViewHolder;
import io.hefuyi.zhihudaily.util.L;
import io.hefuyi.zhihudaily.widget.LoadMoreRecyclerView;
import io.hefuyi.zhihudaily.widget.MyViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyStoriesFragment extends BaseFragment implements DailyStoryContract.View{
    public static final String TAG = DailyStoriesFragment.class.getSimpleName();

    @Inject
    DailyStoryContract.Presenter dailyStoryPresenter;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;

    private LinearLayoutManager mLinearLayoutManager;
    private DailyStoriesAdapter mAdapter;
    private String mDate;
    private boolean isDataLoaded;

    /*初始化相关*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreate  " + this);
        super.onCreate(savedInstanceState);
        injectDependences();
        dailyStoryPresenter.attachView(this);
        mAdapter = new DailyStoriesAdapter();
    }

    private void injectDependences() {
        ApplicationComponent applicationComponent = ((NavigationDrawerActivity) getActivity()).getApplicationComponent();
        DailyStoryComponent dailyStoryComponent = DaggerDailyStoryComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .dailyStoryModule(new DailyStoryModule())
                .build();
        dailyStoryComponent.inject(this);
    }
    /*初始化相关*/

    /*view接口方法*/
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
    public void showStory(final DailyStories dailyStories) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dailyStories == null) {
                    isDataLoaded = false;
                } else {
                    isDataLoaded = true;
                    mDate = dailyStories.getDate();
                    mAdapter.setList(dailyStories);
                }
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void appendStory(final DailyStories dailyStories) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dailyStories != null) {
                    mDate = dailyStories.getDate();
                    mAdapter.appendList(dailyStories);
                }
            }
        };
        getActivity().runOnUiThread(runnable);
    }
    /*view接口方法*/

    /*普通逻辑*/
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

    @Override
    public void refreshUI() {
        TypedValue itemDateTextColor = new TypedValue();
        TypedValue itemStoryTextColor = new TypedValue();
        TypedValue itemStoryBackground = new TypedValue();
        TypedValue windowBackground = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.item_date_text_color, itemDateTextColor, true);
        theme.resolveAttribute(R.attr.item_story_text_color, itemStoryTextColor, true);
        theme.resolveAttribute(R.attr.item_story_background_color, itemStoryBackground, true);
        theme.resolveAttribute(R.attr.windowBackground, windowBackground, true);

        Resources resources = getResources();
        View window = ((ViewGroup) getActivity().getWindow().getDecorView());
        window.setBackgroundColor(resources.getColor(windowBackground.resourceId));
        int childCount = recyclerView.getChildCount();
        int firstVisible = mLinearLayoutManager.findFirstVisibleItemPosition();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            int viewType = mAdapter.getItemViewType(childIndex+firstVisible);
            switch (viewType) {
                case DailyStoriesAdapter.Type.TYPE_HEADER:
                    break;
                case DailyStoriesAdapter.Type.TYPE_DATE:
                    TextView textView = (TextView) recyclerView.getChildAt(childIndex);
                    textView.setTextColor(resources.getColor(itemDateTextColor.resourceId));
                    break;
                case DailyStoriesAdapter.Type.TYPE_STORY:
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

    private String mTitle;
    private int lastTitLePos = -1;

    private void changeActionBarTitle(int dy) {
        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (lastTitLePos == position) {
            return;
        }
        DailyStoriesAdapter.Item item = mAdapter.getItem(position);
        int type = item.getType();
        if (type == DailyStoriesAdapter.Type.TYPE_HEADER) {
            mTitle = getString(R.string.title_activity_main);
        } else if (dy > 0 && type == DailyStoriesAdapter.Type.TYPE_DATE) {
            mTitle = DateViewHolder.getDate(item.getDate(), getActivity());
        } else if (dy < 0) {
            mTitle = DateViewHolder.getDate(mAdapter.getTitleBeforePosition(position), getActivity());
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTitle);
        lastTitLePos = position;
    }
    /*普通逻辑*/

    /*生命周期回调*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_daily_strories, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dailyStoryPresenter.refresh();
            }
        });

        recyclerView.setOnLoadMoreListener(new LoadMoreRecyclerView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                dailyStoryPresenter.loadMore(mDate);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                changeActionBarTitle(dy);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!isDataLoaded) {
                    dailyStoryPresenter.refresh();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null) {
            L.i(TAG, "recyclerView != null");
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                L.i(TAG, "MyViewPager startAutoScroll");
                ((MyViewPager) view).startAutoScroll();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recyclerView != null) {
            L.i(TAG, "revyvlerView !=null");
            View view = recyclerView.findViewById(R.id.viewPager);
            if (view != null) {
                L.i(TAG, "MyViewPager stopAutoScroll");
                ((MyViewPager) view).stopAutoScroll();
            }
        }
    }
    /*生命周期回调*/
}
