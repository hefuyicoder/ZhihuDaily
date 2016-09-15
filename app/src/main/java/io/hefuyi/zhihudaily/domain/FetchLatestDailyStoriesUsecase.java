package io.hefuyi.zhihudaily.domain;


import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/22.
 */
public class FetchLatestDailyStoriesUsecase implements Usecase<DailyStories> {

    private Repository mRepository;

    public FetchLatestDailyStoriesUsecase(Repository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<DailyStories> execute() {
        return mRepository.getLatestDailyStories();
    }
}
