package io.hefuyi.zhihudaily.injector.module;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.mvp.contract.NavigationContract;
import io.hefuyi.zhihudaily.mvp.presenter.NavigationPresenter;
import io.hefuyi.zhihudaily.domain.FetchThemesUsecase;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/22.
 */
@Module
public class NavigationModule {

    @Provides
    public FetchThemesUsecase provideGetThemesUsecase(Repository repository) {
        return new FetchThemesUsecase(repository);
    }

    @Provides
    public NavigationContract.Presenter provideNavigationPresenter(FetchThemesUsecase usecase) {
        return new NavigationPresenter(usecase);
    }
}
