package io.hefuyi.zhihudaily.util;

import android.app.Activity;
import android.content.Intent;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.mvp.model.Story;
import io.hefuyi.zhihudaily.ui.activity.NavigationDrawerActivity;
import io.hefuyi.zhihudaily.ui.activity.StoryActivity;

/**
 * Created by hefuyi on 16/7/29.
 */
public class IntentUtils {
    public static final String EXTRA_STORY_ID = "extra_story_id";

    public static final void intentToMainActivity(Activity activity) {
        Intent intent = new Intent(activity, NavigationDrawerActivity.class);
        activity.startActivity(intent);
        activity.finish();
        //过渡动画
        activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                android.support.v7.appcompat.R.anim.abc_fade_out);
    }

    public static final void intentToStoryActivity(Activity activity, Story story) {
        Intent intent = new Intent(activity, StoryActivity.class);
        intent.putExtra(EXTRA_STORY_ID, String.valueOf(story.getId()));
        activity.startActivity(intent);
    }

    public static final void share(Activity activity, Story story) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(story.getTitle()).append(" ")
                .append(activity.getString(R.string.share_link))
                .append(story.getShareUrl())
                .append(" ")
                .append(activity.getString(R.string.share_from));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, story.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        activity.startActivity(intent);
    }
}
