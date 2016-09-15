package io.hefuyi.zhihudaily.injector.component;

import dagger.Component;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.NavigationModule;
import io.hefuyi.zhihudaily.injector.scope.PerActivity;
import io.hefuyi.zhihudaily.ui.fragment.NavigationFragment;

/**
 * Created by hefuyi on 16/8/22.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, NavigationModule.class})
public interface NavigationComponent {

    void inject(NavigationFragment navigationFragment);
}
