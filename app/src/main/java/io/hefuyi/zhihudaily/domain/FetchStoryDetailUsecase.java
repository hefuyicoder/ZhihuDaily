package io.hefuyi.zhihudaily.domain;


import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/23.
 */
public class FetchStoryDetailUsecase implements Usecase<Story> {

    private String mStoryId;
    private Repository mRepository;

    public FetchStoryDetailUsecase(Repository repository) {
        this.mRepository = repository;
    }

    public void setStoryId(String storyId) {
        this.mStoryId = storyId;
    }

    @Override
    public Observable<Story> execute() {
        return mRepository.getStoryDetail(mStoryId);
    }

}
