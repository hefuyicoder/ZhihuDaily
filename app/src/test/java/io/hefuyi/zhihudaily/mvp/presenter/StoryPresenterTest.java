package io.hefuyi.zhihudaily.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import io.hefuyi.zhihudaily.mvp.contract.StoryContract;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.domain.FetchStoryDetailUsecase;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hefuyi on 16/9/5.
 */
public class StoryPresenterTest {

    FetchStoryDetailUsecase mUsecase;
    StoryContract.View mView;
    StoryPresenter mPresenter;

    @Before
    public void setUp() {
        mUsecase = mock(FetchStoryDetailUsecase.class);
        mView = mock(StoryContract.View.class);
        mPresenter = new StoryPresenter(mUsecase);
        mPresenter.attachView(mView);
    }

    @Test
    public void testRefresh() {
        String storyId = "";
        Story fakeStory = new Story();
        when(mUsecase.execute()).thenReturn(Observable.just(fakeStory));
        mPresenter.refresh(storyId);
        verify(mUsecase).setStoryId(storyId);
        verify(mView).hideProgressBar();
        verify(mView).showStory(fakeStory);
    }

    @Test
    public void testRefreshError() {
        String storyId = "";
        when(mUsecase.execute()).thenReturn(Observable.<Story>error(new Throwable()));
        mPresenter.refresh(storyId);
        verify(mUsecase).setStoryId(storyId);
        verify(mView).hideProgressBar();
        verify(mView).showError();
    }
}