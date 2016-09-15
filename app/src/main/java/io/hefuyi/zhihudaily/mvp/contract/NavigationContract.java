package io.hefuyi.zhihudaily.mvp.contract;


import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.mvp.presenter.BasePresenter;
import io.hefuyi.zhihudaily.mvp.view.BaseView;

/**
 * Created by hefuyi on 16/8/22.
 */
public interface NavigationContract {

    interface View extends BaseView {
        void showThemes(Themes themes);

        void showError();
    }

    interface Presenter extends BasePresenter<View> {
        void refresh();
    }

}
