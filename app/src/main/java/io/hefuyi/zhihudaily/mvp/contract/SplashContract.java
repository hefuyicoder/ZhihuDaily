package io.hefuyi.zhihudaily.mvp.contract;


import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.mvp.presenter.BasePresenter;
import io.hefuyi.zhihudaily.mvp.view.BaseView;

/**
 * Created by hefuyi on 16/8/19.
 */
public interface SplashContract {

    interface View extends BaseView {

        void showBackgroundImage(StartImage startImage);

        void showError();
    }

    interface Presenter extends BasePresenter<View> {

        void refresh(int width, int height);

    }

}
