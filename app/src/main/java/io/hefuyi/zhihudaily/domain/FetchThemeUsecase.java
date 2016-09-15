package io.hefuyi.zhihudaily.domain;


import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/22.
 */
public class FetchThemeUsecase implements Usecase<Theme> {

    private Repository mRepository;

    private String mThemeId;

    public FetchThemeUsecase(Repository repository) {
        this.mRepository = repository;
    }

    public void setThemeId(String themeId) {
        this.mThemeId = themeId;
    }


    @Override
    public Observable<Theme> execute() {
        return mRepository.getTheme(mThemeId);
    }
}
