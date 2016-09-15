package io.hefuyi.zhihudaily.domain;

import rx.Observable;

/**
 * Created by hefuyi on 16/8/19.
 */
public interface Usecase<T> {
    Observable<T> execute();
}
