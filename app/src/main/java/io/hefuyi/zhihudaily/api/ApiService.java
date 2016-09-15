package io.hefuyi.zhihudaily.api;

import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hefuyi on 16/8/16.
 */
public interface ApiService {

    @GET("start-image/{width}*{height}")
    Observable<StartImage> getStartImage(@Path("width") int width, @Path("height") int height);

    @GET("themes")
    Observable<Themes> getThemes();

    @GET("news/latest")
    Observable<DailyStories> getLatestDailyStories();

    @GET("news/before/{date}")
    Observable<DailyStories> getBeforeDailyStories(@Path("date") String date);

    @GET("theme/{themeId}")
    Observable<Theme> getTheme(@Path("themeId") String themeId);

    @GET("theme/{themeId}/before/{storyId}")
    Observable<Theme> getThemeBeforeStory(@Path("themeId") String themeId, @Path("storyId") String storyId);

    @GET("news/{storyId}")
    Observable<Story> getStoryDetail(@Path("storyId") String storyId);

}
