package io.hefuyi.zhihudaily.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hefuyi on 16/7/28.
 */
public class SharedPrefUtils {

    public static final String TAG = SharedPrefUtils.class.getSimpleName();

    //标识是否是第一次使用
    private static final String SHARED_PREF_IS_FIRST_LAUNCH = "shared_pref_is_first_launch";

    //startImage
    private static final String SHARED_PREF_SPLASH_JSON = "shared_pref_splash_json";

    private static final String SHARED_PREF_IS_NIGHE_MODE = "shared_pref_is_nighe_mode";


    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SHARED_PREF_IS_FIRST_LAUNCH, true);
    }

    public static void markFirstLaunch(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SHARED_PREF_IS_FIRST_LAUNCH, false).apply();
    }

    public static boolean isNightMode(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SHARED_PREF_IS_NIGHE_MODE, false);
    }

    public static void markIsNightMode(Context context,boolean isNightMode) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SHARED_PREF_IS_NIGHE_MODE, isNightMode).apply();
    }

    public static String getSplashJson(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SHARED_PREF_SPLASH_JSON, null);
    }

    public static void setSplashJson(Context context, String jsonString) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(SHARED_PREF_SPLASH_JSON, jsonString).apply();
    }
}
