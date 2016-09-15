package io.hefuyi.zhihudaily.injector.component;

import dagger.Component;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.SplashModule;
import io.hefuyi.zhihudaily.injector.scope.PerActivity;
import io.hefuyi.zhihudaily.ui.fragment.SplashFragment;

/**
 * Created by hefuyi on 16/8/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SplashModule.class})
public interface SplashComponent {
    void inject(SplashFragment splashFragment);
}
