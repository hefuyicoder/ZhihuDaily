package io.hefuyi.zhihudaily.mvp.domain;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.hefuyi.zhihudaily.BuildConfig;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.domain.FetchBeforeDailyStoriesUsecase;
import io.hefuyi.zhihudaily.domain.FetchLatestDailyStoriesUsecase;
import io.hefuyi.zhihudaily.domain.FetchStartImageUsecase;
import io.hefuyi.zhihudaily.domain.FetchStoryDetailUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemeBeforeStoryUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemeUsecase;
import io.hefuyi.zhihudaily.domain.FetchThemesUsecase;
import io.hefuyi.zhihudaily.mvp.TestUtils;
import io.hefuyi.zhihudaily.mvp.model.DailyStories;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.ui.activity.GuiderActivity;
import io.hefuyi.zhihudaily.util.DensityUtil;
import rx.observers.TestSubscriber;

/**
 * Created by hefuyi on 16/9/5.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,application = DailyApplication.class)
public class UsecaseTest {

    @Test
    public void testFetchBeforeDailyStoriesUseCase() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(new Date(System.currentTimeMillis()-24*60*60*1000));
        FetchBeforeDailyStoriesUsecase fetchBeforeDailyStoriesUsecase= new FetchBeforeDailyStoriesUsecase(TestUtils.getRepository());
        fetchBeforeDailyStoriesUsecase.setDate(date);
        TestSubscriber<DailyStories> testSubscriber = new TestSubscriber<>();
        fetchBeforeDailyStoriesUsecase.execute().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchLatestDailyStoriesUsecase() {
        FetchLatestDailyStoriesUsecase fetchLatestDailyStoriesUsecase = new FetchLatestDailyStoriesUsecase(TestUtils.getRepository());
        TestSubscriber<DailyStories> testSubscriber = new TestSubscriber<>();
        fetchLatestDailyStoriesUsecase.execute().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchStartImageUsecase() {
        int width = DensityUtil.getScreenWidth(Robolectric.setupActivity(GuiderActivity.class));
        int height = DensityUtil.getScreenHeightWithDecorations(Robolectric.setupActivity(GuiderActivity.class));
        FetchStartImageUsecase fetchStartImageUsecase = new FetchStartImageUsecase(TestUtils.getRepository());
        fetchStartImageUsecase.setScreenSize(width, height);
        TestSubscriber<StartImage> testSubscriber = new TestSubscriber<>();
        fetchStartImageUsecase.execute().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchStoryDetailUsecase() {
        FetchLatestDailyStoriesUsecase fetchLatestDailyStoriesUsecase = new FetchLatestDailyStoriesUsecase(TestUtils.getRepository());
        TestSubscriber<DailyStories> dailyStoriesTestSubscriber = new TestSubscriber<>();
        fetchLatestDailyStoriesUsecase.execute().subscribe(dailyStoriesTestSubscriber);
        String storyId = dailyStoriesTestSubscriber.getOnNextEvents().get(0).getStories().get(0).getId();
        FetchStoryDetailUsecase fetchStoryDetailUsecase = new FetchStoryDetailUsecase(TestUtils.getRepository());
        fetchStoryDetailUsecase.setStoryId(storyId);
        TestSubscriber<Story> storyTestSubscriber = new TestSubscriber<>();
        fetchStoryDetailUsecase.execute().subscribe(storyTestSubscriber);
        storyTestSubscriber.assertNoErrors();
        storyTestSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchThemesUsecase() {
        FetchThemesUsecase fetchThemesUsecases = new FetchThemesUsecase(TestUtils.getRepository());
        TestSubscriber<Themes> testSubscriber = new TestSubscriber<>();
        fetchThemesUsecases.execute().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchThemeUsecase() {
        FetchThemesUsecase fetchThemesUsecases = new FetchThemesUsecase(TestUtils.getRepository());
        TestSubscriber<Themes> testSubscriber = new TestSubscriber<>();
        fetchThemesUsecases.execute().subscribe(testSubscriber);
        String themeId = testSubscriber.getOnNextEvents().get(0).getOthers().get(0).getId();

        FetchThemeUsecase fetchThemeUsecase = new FetchThemeUsecase(TestUtils.getRepository());
        fetchThemeUsecase.setThemeId(themeId);
        TestSubscriber<Theme> themeTestSubscriber = new TestSubscriber<>();
        fetchThemeUsecase.execute().subscribe(themeTestSubscriber);
        themeTestSubscriber.assertNoErrors();
        themeTestSubscriber.assertValueCount(1);
    }

    @Test
    public void testFetchThemeBeforeStoryUsecase() {
        FetchThemesUsecase fetchThemesUsecases = new FetchThemesUsecase(TestUtils.getRepository());
        TestSubscriber<Themes> testSubscriber = new TestSubscriber<>();
        fetchThemesUsecases.execute().subscribe(testSubscriber);
        String themeId = testSubscriber.getOnNextEvents().get(0).getOthers().get(0).getId();

        FetchThemeUsecase fetchThemeUsecase = new FetchThemeUsecase(TestUtils.getRepository());
        fetchThemeUsecase.setThemeId(themeId);
        TestSubscriber<Theme> themeTestSubscriber = new TestSubscriber<>();
        fetchThemeUsecase.execute().subscribe(themeTestSubscriber);
        int storyCount = themeTestSubscriber.getOnNextEvents().get(0).getStories().size();
        String storyId = themeTestSubscriber.getOnNextEvents().get(0).getStories().get(storyCount - 1).getId();

        FetchThemeBeforeStoryUsecase fetchThemeBeforeStoryUsecase = new FetchThemeBeforeStoryUsecase(TestUtils.getRepository());
        fetchThemeBeforeStoryUsecase.setStoryId(storyId);
        fetchThemeBeforeStoryUsecase.setThemeId(themeId);
        fetchThemeBeforeStoryUsecase.execute().subscribe(themeTestSubscriber);
        themeTestSubscriber.assertNoErrors();
        themeTestSubscriber.assertValueCount(1);
    }

    @Test
    public void testActivity() {
        GuiderActivity testActivity = Robolectric.setupActivity(GuiderActivity.class);
    }
}