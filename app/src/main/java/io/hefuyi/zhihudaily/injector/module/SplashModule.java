package io.hefuyi.zhihudaily.injector.module;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.mvp.contract.SplashContract;
import io.hefuyi.zhihudaily.mvp.presenter.SplashPresenter;
import io.hefuyi.zhihudaily.domain.FetchStartImageUsecase;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/19.
 */
@Module
public class SplashModule {
    @Provides
    public FetchStartImageUsecase provideGetStartImageUsecase(Repository repository) {
        return new FetchStartImageUsecase(repository);
    }

    @Provides
    public SplashContract.Presenter provideSplashPresenter(FetchStartImageUsecase usecase) {
        return new SplashPresenter(usecase);
    }
}
