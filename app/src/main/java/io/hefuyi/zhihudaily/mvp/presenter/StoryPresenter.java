package io.hefuyi.zhihudaily.mvp.presenter;

import io.hefuyi.zhihudaily.mvp.contract.StoryContract;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.domain.FetchStoryDetailUsecase;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hefuyi on 16/8/23.
 */
public class StoryPresenter implements StoryContract.Presenter {

    private FetchStoryDetailUsecase mUsecase;
    private Subscription mSubscription;
    private StoryContract.View mView;

    public StoryPresenter(FetchStoryDetailUsecase fetchStoryDetailUsecase) {
        this.mUsecase = fetchStoryDetailUsecase;
    }

    @Override
    public void attachView(StoryContract.View view) {
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
    public void refresh(String storyId) {
        mUsecase.setStoryId(storyId);
        mSubscription = mUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, Story>() {
                    @Override
                    public Story call(Throwable throwable) {
                        mView.hideProgressBar();
                        mView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Story>() {
                    @Override
                    public void call(Story story) {
                        if (story != null) {
                            mView.hideProgressBar();
                            mView.showStory(story);
                        }

                    }
                });
    }
}
