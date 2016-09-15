package io.hefuyi.zhihudaily.injector.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.hefuyi.zhihudaily.injector.scope.PerActivity;

/**
 * Created by hefuyi on 16/8/19.
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Context context() {
        return mActivity;
    }
}
