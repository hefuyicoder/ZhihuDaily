package io.hefuyi.zhihudaily.injector.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.injector.scope.PerApplication;

/**
 * Created by hefuyi on 16/8/16.
 */
@Module
public class ApplicationModule {
    private final DailyApplication mApplication;

    public ApplicationModule(DailyApplication application) {
        this.mApplication = application;
    }

    @Provides
    @PerApplication
    public DailyApplication provideDailyApplication() {
        return mApplication;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return mApplication;
    }
}
