package io.hefuyi.zhihudaily.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import io.hefuyi.zhihudaily.mvp.contract.NavigationContract;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.domain.FetchThemesUsecase;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hefuyi on 16/9/5.
 */
public class NavigationPresenterTest {

    FetchThemesUsecase mUsecase;
    NavigationContract.View mView;
    NavigationPresenter mPresenter;

    @Before
    public void setUp() {
        mUsecase = mock(FetchThemesUsecase.class);
        mView = mock(NavigationContract.View.class);
        mPresenter = new NavigationPresenter(mUsecase);
        mPresenter.attachView(mView);
    }

    @Test
    public void testRefresh() {
        Themes fakeTheme = new Themes();
        when(mUsecase.execute()).thenReturn(Observable.<Themes>just(fakeTheme));
        mPresenter.refresh();
        verify(mUsecase).execute();
        verify(mView).showThemes(fakeTheme);
    }

    @Test
    public void testRefreshError() {
        Themes fakeTheme = new Themes();
        when(mUsecase.execute()).thenReturn(Observable.<Themes>error(new Throwable()));
        mPresenter.refresh();
        verify(mUsecase).execute();
        verify(mView).showError();
    }
}