package io.hefuyi.zhihudaily.mvp.presenter;

import io.hefuyi.zhihudaily.mvp.contract.DailyStoryContract;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.domain.FetchBeforeDailyStoriesUsecase;
import io.hefuyi.zhihudaily.domain.FetchLatestDailyStoriesUsecase;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hefuyi on 16/8/22.
 */
public class DailyStoryPresenter implements DailyStoryContract.Presenter{

    private FetchBeforeDailyStoriesUsecase mBeforeDailyStoriesUsecase;
    private FetchLatestDailyStoriesUsecase mLatestDailyStoriesUsecase;
    private DailyStoryContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public DailyStoryPresenter(FetchLatestDailyStoriesUsecase latestDailyStoriesUsecase, FetchBeforeDailyStoriesUsecase beforeDailyStoriesUsecase) {
        this.mBeforeDailyStoriesUsecase = beforeDailyStoriesUsecase;
        this.mLatestDailyStoriesUsecase = latestDailyStoriesUsecase;
    }

    @Override
    public void attachView(DailyStoryContract.View view) {
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
    public void refresh() {
        mView.showRefreshing();
        Subscription subscription = mLatestDailyStoriesUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, DailyStories>() {
                    @Override
                    public DailyStories call(Throwable throwable) {
                        mView.showError("refresh error");
                        mView.hideRefreshing();
                        return null;
                    }
                })
                .subscribe(new Action1<DailyStories>() {
                    @Override
                    public void call(DailyStories dailyStories) {
                        if (dailyStories != null) {
                            mView.hideRefreshing();
                            mView.showStory(dailyStories);
                        }

                    }
                });
        mCompositeSubscription.add(subscription);
    }


    @Override
    public void loadMore(String date) {
        mBeforeDailyStoriesUsecase.setDate(date);
        Subscription subscription = mBeforeDailyStoriesUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, DailyStories>() {
                    @Override
                    public DailyStories call(Throwable throwable) {
                        mView.showError("load more error");
                        return null;
                    }
                })
                .subscribe(new Action1<DailyStories>() {
                    @Override
                    public void call(DailyStories dailyStories) {
                        if (dailyStories != null) {
                            mView.appendStory(dailyStories);

                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
