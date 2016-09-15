package io.hefuyi.zhihudaily.mvp.presenter;

import io.hefuyi.zhihudaily.mvp.contract.SplashContract;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.domain.FetchStartImageUsecase;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hefuyi on 16/8/19.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private Subscription mSubscription;
    private SplashContract.View mView;
    private FetchStartImageUsecase mUsecase;

    public SplashPresenter(FetchStartImageUsecase usecase) {
        this.mUsecase = usecase;
    }

    @Override
    public void attachView(SplashContract.View view) {
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
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void refresh(int width, int height) {
        mUsecase.setScreenSize(width, height);
        mSubscription = mUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, StartImage>() {
                    @Override
                    public StartImage call(Throwable throwable) {
                        mView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<StartImage>() {
                    @Override
                    public void call(StartImage startImage) {
                        if (startImage != null) {
                            mView.showBackgroundImage(startImage);

                        }
                    }
                });
    }
}
