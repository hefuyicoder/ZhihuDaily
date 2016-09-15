package io.hefuyi.zhihudaily.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.ui.fragment.GuideFragment;
import io.hefuyi.zhihudaily.ui.fragment.SplashFragment;
import io.hefuyi.zhihudaily.util.IntentUtils;
import io.hefuyi.zhihudaily.util.SharedPrefUtils;

public class GuiderActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);

        FragmentManager fm = getSupportFragmentManager();

        if (SharedPrefUtils.isFirstLaunch(this)) {
            guide(fm);
        } else {
            splash(fm);
        }
    }

    private void splash(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentByTag(SplashFragment.TAG) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new SplashFragment(), SplashFragment.TAG)
                    .commit();
        }
    }

    private void guide(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentByTag(GuideFragment.TAG) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new GuideFragment(), GuideFragment.TAG)
                    .commit();
        }
    }

    public void intentToMainActivity() {
        IntentUtils.intentToMainActivity(this);
    }
}
