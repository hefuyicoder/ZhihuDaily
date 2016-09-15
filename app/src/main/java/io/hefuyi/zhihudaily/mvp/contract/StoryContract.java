package io.hefuyi.zhihudaily.mvp.contract;

import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.presenter.BasePresenter;
import io.hefuyi.zhihudaily.mvp.view.BaseView;


/**
 * Created by hefuyi on 16/8/23.
 */
public interface StoryContract {

    interface View extends BaseView {

        void showStory(Story story);

        void showError();

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View> {

        void refresh(String storyId);
    }

}
