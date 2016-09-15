package io.hefuyi.zhihudaily.util;

import android.util.Log;

import io.hefuyi.zhihudaily.BuildConfig;

/**
 * Created by hefuyi on 16/7/30.
 */
public class L {

    public static int i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            return Log.i("==="+tag+"===", msg);
        }
        return -1;
    }

    public static int e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            return Log.e("==="+tag+"===", msg);
        }
        return -1;
    }
}
