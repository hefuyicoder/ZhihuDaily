package io.hefuyi.zhihudaily.injector.module;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.mvp.contract.StoryContract;
import io.hefuyi.zhihudaily.mvp.presenter.StoryPresenter;
import io.hefuyi.zhihudaily.domain.FetchStoryDetailUsecase;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/23.
 */
@Module
public class StoryModule {

    @Provides
    public FetchStoryDetailUsecase getStoryDetailUsecase(Repository repository) {
        return new FetchStoryDetailUsecase(repository);
    }

    @Provides
    public StoryContract.Presenter getStoryPresenter(FetchStoryDetailUsecase fetchStoryDetailUsecase) {
        return new StoryPresenter(fetchStoryDetailUsecase);
    }
}
