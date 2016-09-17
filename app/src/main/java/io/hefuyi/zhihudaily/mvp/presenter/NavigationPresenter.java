package io.hefuyi.zhihudaily.mvp.presenter;

import io.hefuyi.zhihudaily.domain.FetchThemesUsecase;
import io.hefuyi.zhihudaily.mvp.contract.NavigationContract;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hefuyi on 16/8/22.
 */
public class NavigationPresenter implements NavigationContract.Presenter{

    private FetchThemesUsecase mUsecase;
    private NavigationContract.View mView;
    private Subscription mSubscription;

    public NavigationPresenter(FetchThemesUsecase usecase) {
        this.mUsecase = usecase;
    }

    @Override
    public void attachView(NavigationContract.View view) {
        this.mView = view;
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
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void refresh() {
        mSubscription = mUsecase.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Themes>() {
                    @Override
                    public Themes call(Throwable throwable) {
                        mView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Themes>() {
                    @Override
                    public void call(Themes themes) {
                        if (themes != null) {
                            mView.showThemes(themes);
                        }
                    }
                });
    }
}
