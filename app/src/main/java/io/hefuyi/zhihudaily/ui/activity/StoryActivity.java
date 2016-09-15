package io.hefuyi.zhihudaily.ui.activity;

import android.os.Bundle;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.ui.fragment.StoryFragment;
import io.hefuyi.zhihudaily.util.IntentUtils;

public class StoryActivity extends BaseAppcompatActivity {
    private static final String TAG = StoryActivity.class.getSimpleName();


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_story;
    }

    @Override
    protected void refreshUI() {
    }

    @Override
    protected void smoothToTop() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }
        if (savedInstanceState == null) {
            String storyId = getIntent().getStringExtra(IntentUtils.EXTRA_STORY_ID);
            StoryFragment storyFragment = StoryFragment.newInstance(storyId);

            storyFragment.setToolBar(mToolbar);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, storyFragment, StoryFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
