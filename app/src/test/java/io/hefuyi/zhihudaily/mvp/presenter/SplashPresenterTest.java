package io.hefuyi.zhihudaily.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import io.hefuyi.zhihudaily.mvp.contract.SplashContract;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.domain.FetchStartImageUsecase;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hefuyi on 16/9/5.
 */
public class SplashPresenterTest {

    SplashContract.View mSplashView;
    FetchStartImageUsecase mUsecase;
    SplashPresenter mPresenter;

    @Before
    public void setUp() {
        mSplashView = mock(SplashContract.View.class);
        mUsecase = mock(FetchStartImageUsecase.class);
        mPresenter = new SplashPresenter(mUsecase);
        mPresenter.attachView(mSplashView);
    }

    @Test
    public void testRefresh() {
        int width = 0;
        int height = 0;
        StartImage fakeStartImage = new StartImage();
        when(mUsecase.execute()).thenReturn(Observable.just(fakeStartImage));
        mPresenter.refresh(width, height);
        verify(mUsecase).setScreenSize(width, height);
        verify(mUsecase).execute();
        mSplashView.showBackgroundImage(fakeStartImage);
    }

    @Test
    public void testRefreshError() {
        int width = 0;
        int height = 0;
        when(mUsecase.execute()).thenReturn(Observable.<StartImage>error(new Throwable()));
        mPresenter.refresh(width, height);
        verify(mUsecase).setScreenSize(width, height);
        verify(mUsecase).execute();
        verify(mSplashView).showError();
    }
}