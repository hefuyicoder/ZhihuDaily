package io.hefuyi.zhihudaily.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import io.hefuyi.zhihudaily.mvp.contract.DailyStoryContract;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.domain.FetchBeforeDailyStoriesUsecase;
import io.hefuyi.zhihudaily.domain.FetchLatestDailyStoriesUsecase;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hefuyi on 16/9/4.
 */
public class DailyStoryPresenterTest {

    FetchBeforeDailyStoriesUsecase mBeforeDailyStoriesUsecase;
    FetchLatestDailyStoriesUsecase mLatestDailyStoriesUsecase;
    DailyStoryContract.View mView;
    DailyStoryPresenter mPresenter;

    @Before
    public void setUp() {
        mBeforeDailyStoriesUsecase = mock(FetchBeforeDailyStoriesUsecase.class);
        mLatestDailyStoriesUsecase = mock(FetchLatestDailyStoriesUsecase.class);
        mView = mock(DailyStoryContract.View.class);
        mPresenter = new DailyStoryPresenter(mLatestDailyStoriesUsecase, mBeforeDailyStoriesUsecase);
        mPresenter.attachView(mView);
    }

    @Test
    public void testRefresh() {
        final DailyStories fakeDailyStory = new DailyStories();
        when(mLatestDailyStoriesUsecase.execute()).thenReturn(Observable.just(fakeDailyStory));
        mPresenter.refresh();
        verify(mView).showRefreshing();
        verify(mView).hideRefreshing();
        verify(mView).showStory(fakeDailyStory);
    }

    @Test
    public void testRefreshError() {
        when(mLatestDailyStoriesUsecase.execute()).thenReturn(Observable.<DailyStories>error(new Throwable()));
        mPresenter.refresh();
        verify(mView).showRefreshing();
        verify(mView).showError("refresh error");
        verify(mView).hideRefreshing();
    }

    @Test
    public void testLoadMore() {
        String date = "";
        DailyStories fakeDailyStory = new DailyStories();
        when(mBeforeDailyStoriesUsecase.execute()).thenReturn(Observable.just(fakeDailyStory));
        mPresenter.loadMore(date);
        verify(mBeforeDailyStoriesUsecase).setDate(date);
        verify(mView).appendStory(fakeDailyStory);
    }

    @Test
    public void testLoadMoreError() {
        String date = "";
        DailyStories fakeDailyStory = new DailyStories();
        when(mBeforeDailyStoriesUsecase.execute()).thenReturn(Observable.<DailyStories>error(new Throwable()));
        mPresenter.loadMore(date);
        verify(mBeforeDailyStoriesUsecase).setDate(date);
        verify(mView).showError("load more error");
    }
}