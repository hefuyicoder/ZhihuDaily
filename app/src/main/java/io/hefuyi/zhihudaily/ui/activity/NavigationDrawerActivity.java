package io.hefuyi.zhihudaily.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.interfaces.NavigationDrawerCallbacks;
import io.hefuyi.zhihudaily.ui.fragment.BaseFragment;
import io.hefuyi.zhihudaily.ui.fragment.DailyStoriesFragment;
import io.hefuyi.zhihudaily.ui.fragment.NavigationFragment;
import io.hefuyi.zhihudaily.ui.fragment.ThemeStoriesFragment;
import io.hefuyi.zhihudaily.util.L;

public class NavigationDrawerActivity extends BaseAppcompatActivity implements NavigationDrawerCallbacks {

    private static final String TAG = NavigationDrawerActivity.class.getSimpleName();
    private static final String STATE_SELECTED_POSITION = "state_selected_positioin";

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation_drawer)
    View navigationDrawer;

    private NavigationFragment mNavigationFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "";
    private int mLastPosition = 0;

    /*初始化相关方法*/
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_navigation_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.i(TAG, "onCreate  " + this);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        setUpDrawer();
        if (savedInstanceState == null) {
            mNavigationFragment.selectItem(NavigationFragment.getDefaultNavDrawerItem());
        } else {
            int position = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mLastPosition = position;
            mTitle = mNavigationFragment.getTitle(position);
            restortActionBar();
            mNavigationFragment.selectItem(position);
        }
    }

    private void setUpDrawer() {
        mNavigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        drawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void restortActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    /*初始化相关方法*/

    /*事件响应方法*/
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment lastFragment = fragmentManager.findFragmentByTag(getTag(mLastPosition));
        if (lastFragment != null) {
            fragmentTransaction.detach(lastFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(getTag(position));
        if (fragment == null) {
            fragment = getFragmentItem(position);
            fragmentTransaction.add(R.id.container, fragment, getTag(position));
        } else {
            fragmentTransaction.attach(fragment);
        }
        fragmentTransaction.commit();
        mLastPosition = position;

        mTitle = mNavigationFragment.getTitle(position);
        restortActionBar();
    }

    @Override
    protected void refreshUI() {
        mNavigationFragment.refreshUI();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment lastFragment = fragmentManager.findFragmentByTag(getTag(mLastPosition));
        if (lastFragment != null) {
            ((BaseFragment) lastFragment).refreshUI();
        }
        refreshToolBar();
    }

    private void refreshToolBar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        mToolbar.setBackgroundColor(getResources().getColor(typedValue.resourceId));
    }

    @Override
    protected void smoothToTop() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment lastFragment = fragmentManager.findFragmentByTag(getTag(mLastPosition));
        if (lastFragment != null) {
            ((BaseFragment) lastFragment).smoothToTop();
        }
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(navigationDrawer);
    }
    /*事件响应方法*/

    /*普通逻辑方法*/
    private String getTag(int position) {
        switch (position) {
            case 0:
                return DailyStoriesFragment.TAG;
            default:
                return ThemeStoriesFragment.TAG + position;
        }
    }

    private Fragment getFragmentItem(int position) {
        return BaseFragment.newInstance(position, mNavigationFragment.getSectionId(position));
    }

    public ApplicationComponent getApplicationComponent() {
        ApplicationComponent applicationComponent = ((DailyApplication) getApplication()).getApplicationComponent();
        return applicationComponent;
    }

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(navigationDrawer);
    }
    /*普通逻辑方法*/

    /*生命周期回调方法*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mLastPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_navigationdrawer, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    /*生命周期回调方法*/
}
