package io.hefuyi.zhihudaily.respository;

import io.hefuyi.zhihudaily.api.ApiService;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.respository.interfaces.Repository;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by hefuyi on 16/7/30.
 */
public class RepositoryImpl implements Repository {

    private ApiService mApiService;

    public RepositoryImpl(Retrofit retrofit) {
        mApiService = retrofit.create(ApiService.class);
    }

    @Override
    public Observable<StartImage> getStartImage(int width, int height) {
        return mApiService.getStartImage(width, height);
    }

    @Override
    public Observable<Themes> getThemes() {
        return mApiService.getThemes();
    }

    @Override
    public Observable<DailyStories> getLatestDailyStories() {
        return mApiService.getLatestDailyStories();
    }

    @Override
    public Observable<DailyStories> getBeforeDailyStories(String date) {
        return mApiService.getBeforeDailyStories(date);
    }

    @Override
    public Observable<Theme> getTheme(String themeId) {
        return mApiService.getTheme(themeId);
    }

    @Override
    public Observable<Theme> getThemeBeforeStory(String themeId, String storyId) {
        return mApiService.getThemeBeforeStory(themeId, storyId);
    }

    @Override
    public Observable<Story> getStoryDetail(String storyId) {
        return mApiService.getStoryDetail(storyId);
    }
}
