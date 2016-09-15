package io.hefuyi.zhihudaily;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.injector.component.DaggerApplicationComponent;
import io.hefuyi.zhihudaily.injector.module.ApplicationModule;
import io.hefuyi.zhihudaily.injector.module.NetworkModule;


/**
 * Created by hefuyi on 16/7/27.
 */
public class DailyApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private static Context sContext;
    private static RefWatcher sRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        setStrictMode();
        setCrashHandler();
        initStetho();
        initLeakCanary();
        setupInjector();
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(this)).build();
    }

    private void initLeakCanary() {
        sRefWatcher=LeakCanary.install(this);
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void setCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    private void setStrictMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
