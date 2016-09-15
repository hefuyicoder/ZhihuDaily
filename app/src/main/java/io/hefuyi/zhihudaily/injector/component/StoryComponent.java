package io.hefuyi.zhihudaily.injector.component;

import dagger.Component;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.StoryModule;
import io.hefuyi.zhihudaily.injector.scope.PerActivity;
import io.hefuyi.zhihudaily.ui.fragment.StoryFragment;

/**
 * Created by hefuyi on 16/8/23.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, StoryModule.class})
public interface StoryComponent {

    void inject(StoryFragment storyFragment);
}
