package io.hefuyi.zhihudaily.mvp.contract;


import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.presenter.BasePresenter;
import io.hefuyi.zhihudaily.mvp.view.BaseView;

/**
 * Created by hefuyi on 16/8/22.
 */
public interface ThemeStoryContract {

    interface View extends BaseView {

        void showError(String errorString);

        void showRefreshing();

        void hideRefreshing();

        void showStory(Theme theme);

        void appendStory(Theme theme);
    }

    interface Presenter extends BasePresenter<View> {

        void refresh(String themeId);

        void loadMore(String themeId,String lastStoryId);
    }

}
