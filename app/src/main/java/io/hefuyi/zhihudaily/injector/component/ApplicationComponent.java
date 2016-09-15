package io.hefuyi.zhihudaily.injector.component;

import android.app.Application;

import dagger.Component;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.injector.module.ApplicationModule;
import io.hefuyi.zhihudaily.injector.module.NetworkModule;
import io.hefuyi.zhihudaily.injector.scope.PerApplication;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;

/**
 * Created by hefuyi on 16/8/16.
 */
@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Application application();

    DailyApplication dailyApplication();

    Repository repository();

}
