package io.hefuyi.zhihudaily.mvp.presenter;


import io.hefuyi.zhihudaily.mvp.view.BaseView;

/**
 * Created by hefuyi on 16/8/19.
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void onCreate();

    void onStart();

    void onPause();

    void onStop();

}
