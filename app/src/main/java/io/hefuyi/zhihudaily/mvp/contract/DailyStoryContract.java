package io.hefuyi.zhihudaily.mvp.contract;


import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.presenter.BasePresenter;
import io.hefuyi.zhihudaily.mvp.view.BaseView;

/**
 * Created by hefuyi on 16/8/22.
 */
public interface DailyStoryContract {

    interface View extends BaseView {

        void showError(String errorString);

        void hideRefreshing();

        void showRefreshing();

        void showStory(DailyStories dailyStories);

        void appendStory(DailyStories dailyStories);
    }

    interface Presenter extends BasePresenter<View> {

        void refresh();

        void loadMore(String date);
    }

}
