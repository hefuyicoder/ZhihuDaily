package io.hefuyi.zhihudaily.injector.module;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.mvp.contract.DailyStoryContract;
import io.hefuyi.zhihudaily.mvp.presenter.DailyStoryPresenter;
import io.hefuyi.zhihudaily.domain.FetchBeforeDailyStoriesUsecase;
import io.hefuyi.zhihudaily.domain.FetchLatestDailyStoriesUsecase;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/22.
 */
@Module
public class DailyStoryModule {

    @Provides
    public FetchLatestDailyStoriesUsecase getLatestDailyStoriesUsecase(Repository repository) {
        return new FetchLatestDailyStoriesUsecase(repository);
    }

    @Provides
    public FetchBeforeDailyStoriesUsecase getBeforeDailyStoriesUsecase(Repository repository) {
        return new FetchBeforeDailyStoriesUsecase(repository);
    }

    @Provides
    public DailyStoryContract.Presenter getDailyStoryPresenter(FetchLatestDailyStoriesUsecase latestDailyStoriesUsecase,
                                                               FetchBeforeDailyStoriesUsecase beforeDailyStoriesUsecase) {
        return new DailyStoryPresenter(latestDailyStoriesUsecase, beforeDailyStoriesUsecase);
    }
}
