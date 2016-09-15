package io.hefuyi.zhihudaily.domain;


import android.text.TextUtils;

import com.google.gson.Gson;

import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import io.hefuyi.zhihudaily.util.SharedPrefUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hefuyi on 16/8/19.
 */
public class FetchStartImageUsecase implements Usecase<StartImage> {
    private Repository mRepository;
    private int mWidth;
    private int mHeight;

    public FetchStartImageUsecase(Repository repository) {
        this.mRepository = repository;
    }

    public void setScreenSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    public Observable<StartImage> execute() {
        String oldJsonString = SharedPrefUtils.getSplashJson(DailyApplication.getContext());
        if (!TextUtils.isEmpty(oldJsonString)) { //use old StartImage first
            StartImage startImage = new Gson().fromJson(oldJsonString, StartImage.class);
            mRepository.getStartImage(mWidth, mHeight).observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<StartImage>() {
                        @Override
                        public void call(StartImage startImage) {
                            SharedPrefUtils.setSplashJson(DailyApplication.getContext(), new Gson().toJson(startImage));
                        }
                    });
            return Observable.just(startImage);
        } else { //get StartImage from network and save
            return mRepository.getStartImage(mWidth, mHeight).doOnNext(new Action1<StartImage>() {
                @Override
                public void call(StartImage startImage) {
                    SharedPrefUtils.setSplashJson(DailyApplication.getContext(), new Gson().toJson(startImage));
                }
            });
        }
    }
}
