package io.hefuyi.zhihudaily.domain;


import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/22.
 */
public class FetchBeforeDailyStoriesUsecase implements Usecase<DailyStories> {

    private Repository mRepository;

    private String mDate;

    public FetchBeforeDailyStoriesUsecase(Repository repository) {
        this.mRepository = repository;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    @Override
    public Observable<DailyStories> execute() {
        return mRepository.getBeforeDailyStories(mDate);
    }
}
