package io.hefuyi.zhihudaily.respository.interfaces;


import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import rx.Observable;

/**
 * Created by hefuyi on 16/7/30.
 */
public interface Repository {

    Observable<StartImage> getStartImage(int width, int height);

    Observable<Themes> getThemes();

    Observable<DailyStories> getLatestDailyStories();

    Observable<DailyStories> getBeforeDailyStories(String date);

    Observable<Theme> getTheme(String themeId);

    Observable<Theme> getThemeBeforeStory(String themeId, String storyId);

    Observable<Story> getStoryDetail(String storyId);

}
