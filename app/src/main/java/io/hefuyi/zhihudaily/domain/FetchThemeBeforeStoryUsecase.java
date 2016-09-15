package io.hefuyi.zhihudaily.domain;


import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/22.
 */
public class FetchThemeBeforeStoryUsecase implements Usecase<Theme> {

    private Repository mRepository;

    private String mThemeId;

    private String mStoryId;

    public FetchThemeBeforeStoryUsecase(Repository repository) {
        this.mRepository = repository;
    }

    public void setThemeId(String themeId) {
        this.mThemeId = themeId;
    }

    public void setStoryId(String storyId) {
        this.mStoryId = storyId;
    }

    @Override
    public Observable<Theme> execute() {
        return mRepository.getThemeBeforeStory(mThemeId, mStoryId);
    }
}
