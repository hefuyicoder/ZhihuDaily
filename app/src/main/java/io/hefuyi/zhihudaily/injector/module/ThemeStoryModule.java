package io.hefuyi.zhihudaily.injector.module;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.mvp.contract.ThemeStoryContract;
import io.hefuyi.zhihudaily.mvp.presenter.ThemeStoryPresenter;
import io.hefuyi.zhihudaily.domain.FetchThemeBeforeStoryUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemeUsecase;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/22.
 */
@Module
public class ThemeStoryModule {

    @Provides
    public FetchThemeUsecase getThemeUsecase(Repository repository) {
        return new FetchThemeUsecase(repository);
    }

    @Provides
    public FetchThemeBeforeStoryUsecase getThemeBeforeStoryUsecase(Repository repository) {
        return new FetchThemeBeforeStoryUsecase(repository);
    }

    @Provides
    public ThemeStoryContract.Presenter getThemeStoryPresenter(FetchThemeUsecase themeUsecase, FetchThemeBeforeStoryUsecase themeBeforeStoryUsecase) {
        return new ThemeStoryPresenter(themeUsecase, themeBeforeStoryUsecase);
    }

}
