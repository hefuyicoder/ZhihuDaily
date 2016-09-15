package io.hefuyi.zhihudaily.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.util.SharedPrefUtils;
import io.hefuyi.zhihudaily.util.UIUtils;

public abstract class BaseAppcompatActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    protected abstract int getContentViewLayoutId();

    protected abstract void refreshUI();

    protected abstract void smoothToTop();

    /*初始化相关方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(getContentViewLayoutId());
        mToolbar = (Toolbar) findViewById(R.id.actionbarToolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothToTop();
            }
        });

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = UIUtils.getStatusBarHeight(this);
            mToolbar.setPadding(0, statusBarHeight, 0, 0);
        }
        setupActionBar();
    }

    private void initTheme() {
        boolean isNightMode = SharedPrefUtils.isNightMode(this);
        if (isNightMode) {
            setTheme(R.style.NightTheme);
        }
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !(this instanceof NavigationDrawerActivity)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    /*初始化相关方法*/

    /*事件响应方法*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_mode) {
            showAnimation();
            toggleThemeSetting();
            refreshUI();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAnimation() {
        final View decorview = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorview);
        if (decorview instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackground(new BitmapDrawable((getResources()), cacheBitmap));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorview).addView(view, layoutParams);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorview).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isNightMode = SharedPrefUtils.isNightMode(this);
        MenuItem modeItem = menu.findItem(R.id.action_mode);
        if (modeItem != null) {
            if (isNightMode) {
                modeItem.setTitle("日间模式");
            } else {
                modeItem.setTitle("夜间模式");
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
    /*事件响应方法*/

    /*普通逻辑方法*/
    private void toggleThemeSetting() {
        Boolean isNightMode = SharedPrefUtils.isNightMode(this);
        if (isNightMode) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        SharedPrefUtils.markIsNightMode(this,!isNightMode);
    }

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnable = true;
        view.setDrawingCacheEnabled(drawingCacheEnable);
        view.buildDrawingCache(drawingCacheEnable);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
    /*普通逻辑方法*/
}
