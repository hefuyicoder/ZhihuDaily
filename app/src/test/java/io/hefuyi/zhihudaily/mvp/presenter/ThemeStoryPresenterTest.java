package io.hefuyi.zhihudaily.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import io.hefuyi.zhihudaily.mvp.contract.ThemeStoryContract;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.domain.FetchThemeBeforeStoryUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemeUsecase;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hefuyi on 16/9/5.
 */
public class ThemeStoryPresenterTest {

    FetchThemeUsecase mThemeUsecase;
    FetchThemeBeforeStoryUsecase mBeforeStoryUsecase;
    ThemeStoryContract.View mView;
    ThemeStoryPresenter mPresenter;

    @Before
    public void setUp() {
        mThemeUsecase = mock(FetchThemeUsecase.class);
        mBeforeStoryUsecase = mock(FetchThemeBeforeStoryUsecase.class);
        mView = mock(ThemeStoryContract.View.class);
        mPresenter = new ThemeStoryPresenter(mThemeUsecase, mBeforeStoryUsecase);
        mPresenter.attachView(mView);
    }

    @Test
    public void testRefresh() {
        String themeId = "";
        Theme fakeTheme = new Theme();
        when(mThemeUsecase.execute()).thenReturn(Observable.just(fakeTheme));
        mPresenter.refresh(themeId);
        verify(mThemeUsecase).setThemeId(themeId);
        verify(mView).showRefreshing();
        verify(mView).hideRefreshing();
        verify(mView).showStory(fakeTheme);
    }

    @Test
    public void testRefreshError() {
        String themeId = "";
        when(mThemeUsecase.execute()).thenReturn(Observable.<Theme>error(new Throwable()));
        mPresenter.refresh(themeId);
        verify(mThemeUsecase).setThemeId(themeId);
        verify(mView).showRefreshing();
        verify(mView).hideRefreshing();
        verify(mView).showError("refresh error");
    }

    @Test
    public void testLoadMore() {
        String themeId = "";
        String lastStoryId = "";
        Theme fakeTheme = new Theme();
        when(mBeforeStoryUsecase.execute()).thenReturn(Observable.just(fakeTheme));
        mPresenter.loadMore(themeId, lastStoryId);
        verify(mBeforeStoryUsecase).setStoryId(lastStoryId);
        verify(mBeforeStoryUsecase).setThemeId(themeId);
        verify(mView).appendStory(fakeTheme);
    }

    @Test
    public void testLoadMoreError() {
        String themeId = "";
        String lastStoryId = "";
        Theme fakeTheme = new Theme();
        when(mBeforeStoryUsecase.execute()).thenReturn(Observable.<Theme>error(new Throwable()));
        mPresenter.loadMore(themeId, lastStoryId);
        verify(mBeforeStoryUsecase).setStoryId(lastStoryId);
        verify(mBeforeStoryUsecase).setThemeId(themeId);
        verify(mView).showError("load more error");
    }
}