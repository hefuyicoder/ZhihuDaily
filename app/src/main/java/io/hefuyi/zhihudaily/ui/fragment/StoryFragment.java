package io.hefuyi.zhihudaily.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.injector.component.DaggerStoryComponent;
import io.hefuyi.zhihudaily.injector.component.StoryComponent;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.StoryModule;
import io.hefuyi.zhihudaily.mvp.contract.StoryContract;
import io.hefuyi.zhihudaily.mvp.model.Editor;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.util.IntentUtils;
import io.hefuyi.zhihudaily.util.ScrollPullDownHelper;
import io.hefuyi.zhihudaily.util.SharedPrefUtils;
import io.hefuyi.zhihudaily.util.WebUtils;
import io.hefuyi.zhihudaily.widget.AvatarsView;
import io.hefuyi.zhihudaily.widget.StoryHeaderView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment implements StoryContract.View{
    public static final String TAG = StoryFragment.class.getSimpleName();


    @Inject
    StoryContract.Presenter storyPresenter;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind((R.id.rl_container_header))
    RelativeLayout rlStoryHeader;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.webViewContainer)
    LinearLayout llWebViewContainer;
    @Bind(R.id.avatarView)
    AvatarsView avatarsView;
    @Bind(R.id.spaceView)
    View spaceView;

    private Toolbar mActionBarToolbar;
    private StoryHeaderView mStoryHeaderView;
    private SoftReference<WebView> mWebViewSoftReference;
    private String mStoryId;
    private ScrollPullDownHelper mScrollPullDownHelper;
    private Story mStory;

    /*初始化相关*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
        storyPresenter.attachView(this);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mStoryId = getArguments().getString(IntentUtils.EXTRA_STORY_ID);
        }

        mScrollPullDownHelper = new ScrollPullDownHelper();

    }

    private void injectDependences() {
        ApplicationComponent applicationComponent = ((DailyApplication) getActivity().getApplication()).getApplicationComponent();
        StoryComponent storyComponent = DaggerStoryComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .storyModule(new StoryModule())
                .build();
        storyComponent.inject(this);
    }
    /*初始化相关*/

    /*view接口*/
    @Override
    public void showStory(final Story story) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (story == null) {
                    return;
                } else {
                    mStory = story;
                    bindData(story);
                }
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showError() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "refresh error", Toast.LENGTH_SHORT).show();
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showProgressBar() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void hideProgressBar() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        };
        getActivity().runOnUiThread(runnable);
    }
    /*view接口*/

    /*普通逻辑*/
    public static StoryFragment newInstance(String storyId) {
        StoryFragment fragment = new StoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentUtils.EXTRA_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean isWebViewOK() {
        return mWebViewSoftReference != null && mWebViewSoftReference.get() != null;
    }

    private void bindData(Story story) {
        boolean hasImage = !TextUtils.isEmpty(story.getImage());
        bindHeaderView(hasImage);
        bindAvatarsView();
        bindWebView(hasImage);
    }

    private void bindHeaderView(boolean hasImage) {
        if (hasImage) {
            if (mActionBarToolbar != null) {
                mActionBarToolbar.getBackground().mutate().setAlpha(0);
            }
            spaceView.setVisibility(View.VISIBLE);
            rlStoryHeader.addView(mStoryHeaderView);
            mStoryHeaderView.bindData(mStory.getTitle(), mStory.getImageSource(), mStory.getImage());
        } else {
            spaceView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    mActionBarToolbar.getHeight()));
        }
    }

    private void bindAvatarsView() {
        List<Editor> editors = mStory.getRecommenders();
        if (editors != null && editors.size() > 0) {
            avatarsView.setVisibility(View.VISIBLE);
            List<String> avatars = new ArrayList<>(editors.size());
            for (Editor editor : editors) {
                avatars.add(editor.getAvatar());
            }
            avatarsView.bindData(getString(R.string.avatar_title_referee), avatars);
        } else {
            avatarsView.setVisibility(View.GONE);
        }
    }

    private void bindWebView(boolean hasImage) {
        if (TextUtils.isEmpty(mStory.getBody())) {
            WebSettings settings = mWebViewSoftReference.get().getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            mWebViewSoftReference.get().setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            mWebViewSoftReference.get().loadUrl(mStory.getShareUrl());
        } else {
            boolean isNightMode = SharedPrefUtils.isNightMode(getContext());
            String data = WebUtils.buildHtmlWithCss(mStory.getBody(), mStory.getCssList(), isNightMode);
            mWebViewSoftReference.get().loadDataWithBaseURL(WebUtils.BASE_URL, data, WebUtils.MIME_TYPE, WebUtils.ENCODING, WebUtils.FAIL_URL);
            if (hasImage) {
                addScrollListener();
            }
        }
    }

    private void addScrollListener() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!isAdded()) {
                    return;
                }
                changeHeaderPosition();
                changeToolbarAlpha();
            }
        });
    }

    private void changeHeaderPosition() {
        int scrollY = scrollView.getScrollY();
        int headerScrollY = (scrollY > 0) ? (scrollY / 2) : 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mStoryHeaderView.setScrollY(headerScrollY);
            mStoryHeaderView.requestLayout();
        }
    }

    /**
     * 1.如果滚动量小于图片和toolbar的差值,设置toolbar透明度,坐标为0,直接返回
     * 2.如果滚动两大于差值,则开始修改toolbar的坐标,y坐标为滚动量的1中差值的差值
     */
    private void changeToolbarAlpha() {
        int scrollY = scrollView.getScrollY();
        int storyHeaderViewHeight = getStoryHeaderViewHeight();
        float toolbarHeight = mActionBarToolbar.getHeight();
        float contentHeight = storyHeaderViewHeight - toolbarHeight;

        float ratio = Math.min(scrollY / contentHeight, 1.0f);
        mActionBarToolbar.getBackground().mutate().setAlpha((int) (ratio * 0xFF));

        if (scrollY <= contentHeight) {
            mActionBarToolbar.setY(0f);
            return;
        }

        boolean isPullingDown = mScrollPullDownHelper.onScrollChange(scrollY);
        float toolBarPositionY = isPullingDown ? 0 : (contentHeight - scrollY);
        mActionBarToolbar.setY(toolBarPositionY);

    }

    private int getStoryHeaderViewHeight() {
        return getResources().getDimensionPixelSize(R.dimen.view_header_story_height);
    }

    public void setToolBar(Toolbar toolbar) {
        this.mActionBarToolbar = toolbar;
    }
    /*普通逻辑*/

    /*生命周期回调*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStoryHeaderView = StoryHeaderView.newInstance(container);
        mWebViewSoftReference = new SoftReference<>(new WebView(getActivity()));

        if (isWebViewOK()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mWebViewSoftReference.get().setLayoutParams(layoutParams);
        }

        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //去除滑动到底部的蓝色阴影
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

        if (isWebViewOK()) {
            llWebViewContainer.addView(mWebViewSoftReference.get());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storyPresenter.refresh(mStoryId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isWebViewOK()) {
            mWebViewSoftReference.get().onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isWebViewOK()) {
            mWebViewSoftReference.get().onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isWebViewOK()) {
            mWebViewSoftReference.get().removeAllViews();
            mWebViewSoftReference.get().destroy();
            llWebViewContainer.removeView(mWebViewSoftReference.get());
            mWebViewSoftReference = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (mStory != null) {
                IntentUtils.share(getActivity(), mStory);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    /*生命周期回调*/
}
