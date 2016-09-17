package io.hefuyi.zhihudaily.mvp.presenter;

import io.hefuyi.zhihudaily.domain.FetchThemeBeforeStoryUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemeUsecase;
import io.hefuyi.zhihudaily.mvp.contract.ThemeStoryContract;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hefuyi on 16/8/22.
 */
public class ThemeStoryPresenter implements ThemeStoryContract.Presenter {

    private CompositeSubscription mCompositeSubscription;
    private FetchThemeUsecase mThemeUsecase;
    private FetchThemeBeforeStoryUsecase mBeforeStoryUsecase;
    private ThemeStoryContract.View mView;

    public ThemeStoryPresenter(FetchThemeUsecase themeUsecase, FetchThemeBeforeStoryUsecase themeBeforeStoryUsecase) {
        this.mThemeUsecase = themeUsecase;
        this.mBeforeStoryUsecase = themeBeforeStoryUsecase;
    }

    @Override
    public void attachView(ThemeStoryContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void refresh(String themeId) {
        mView.showRefreshing();
        mThemeUsecase.setThemeId(themeId);
        Subscription subscription = mThemeUsecase.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Theme>() {
                    @Override
                    public Theme call(Throwable throwable) {
                        mView.hideRefreshing();
                        mView.showError("refresh error");
                        return null;
                    }
                })
                .subscribe(new Action1<Theme>() {
                    @Override
                    public void call(Theme theme) {
                        if (theme != null) {
                            mView.hideRefreshing();
                            mView.showStory(theme);
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void loadMore(String themeId, String lastStoryId) {
        mBeforeStoryUsecase.setThemeId(themeId);
        mBeforeStoryUsecase.setStoryId(lastStoryId);
        Subscription subscription = mBeforeStoryUsecase.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Theme>() {
                    @Override
                    public Theme call(Throwable throwable) {
                        mView.showError("load more error");
                        return null;
                    }
                })
                .subscribe(new Action1<Theme>() {
                    @Override
                    public void call(Theme theme) {
                        if (theme != null) {
                            mView.appendStory(theme);
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
