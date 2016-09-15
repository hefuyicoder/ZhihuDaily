package io.hefuyi.zhihudaily.domain;

import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/22.
 */
public class FetchThemesUsecase implements Usecase<Themes> {
    private Repository mRepository;

    public FetchThemesUsecase(Repository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<Themes> execute() {
        return mRepository.getThemes();
    }

}
