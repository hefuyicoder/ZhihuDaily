package io.hefuyi.zhihudaily.ui.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.injector.component.DaggerNavigationComponent;
import io.hefuyi.zhihudaily.injector.component.NavigationComponent;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.NavigationModule;
import io.hefuyi.zhihudaily.interfaces.NavigationDrawerCallbacks;
import io.hefuyi.zhihudaily.mvp.contract.NavigationContract;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.mvp.model.Themes;
import io.hefuyi.zhihudaily.ui.activity.NavigationDrawerActivity;
import io.hefuyi.zhihudaily.ui.adapter.NavigationDrawerAdapter;
import io.hefuyi.zhihudaily.util.L;
import io.hefuyi.zhihudaily.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment implements NavigationDrawerCallbacks,NavigationContract.View{

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    public static final String TAG = NavigationFragment.class.getSimpleName();

    @Inject
    NavigationContract.Presenter navigationPresenter;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private NavigationDrawerCallbacks mCallbacks;
    private int mCurrentSelectedPosition = -1;
    private NavigationDrawerAdapter mAdapter;
    private List<Theme> mThemes;

    /*初始化相关*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreate  " + this);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
        //init adapter
        boolean isKitKatWithNavigation = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                && UIUtils.hasNavigationBar(getActivity());
        mAdapter = new NavigationDrawerAdapter(isKitKatWithNavigation);
        //set callback for adapter
        mAdapter.setNavigationDrawerCallbacks(this);
    }

    public void injectDependences() {
        ApplicationComponent applicationComponent = ((DailyApplication) getActivity().getApplication())
                .getApplicationComponent();
        NavigationComponent navigationComponent = DaggerNavigationComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .navigationModule(new NavigationModule())
                .build();
        navigationComponent.inject(this);
    }
    /*初始化相关*/

    /*view接口方法*/
    @Override
    public void showThemes(final Themes themes) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mThemes = themes.getOthers();
                mAdapter.setThemes(mThemes);
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showError() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "get themes error", Toast.LENGTH_SHORT).show();
            }
        };
        getActivity().runOnUiThread(runnable);
    }
    /*view接口方法*/

    /*普通逻辑*/
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mCurrentSelectedPosition == position) {
            ((NavigationDrawerActivity)getActivity()).closeDrawer();
            return;
        }
        ((NavigationDrawerActivity)getActivity()).closeDrawer();
        mCurrentSelectedPosition = position;
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void refreshUI() {
        TypedValue headerBackground = new TypedValue();
        TypedValue navdrawerBackground = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, headerBackground, true);
        theme.resolveAttribute(R.attr.navdrawer_background, navdrawerBackground,true);

        recyclerView.setBackgroundResource(navdrawerBackground.resourceId);
        int childCount = recyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            int viewType = mAdapter.getItemViewType(childIndex);
            switch (viewType) {
                case NavigationDrawerAdapter.Type.TYPE_HEADER:
                    ViewGroup header = (ViewGroup) recyclerView.getChildAt(childIndex);
                    header.setBackgroundResource(headerBackground.resourceId);
                    break;
                case NavigationDrawerAdapter.Type.TYPE_ITEM:
                    break;
                case NavigationDrawerAdapter.Type.TYPE_BOTTOM_SPACE:
                    View childView = recyclerView.getChildAt(childIndex);
                    childView.setBackgroundResource(navdrawerBackground.resourceId);
                    break;
            }
        }
    }

    public static int getDefaultNavDrawerItem() {
        return 0;
    }

    public String getSectionId(int position){
        return position == 0 ? null : mThemes.get(position - 1).getId();
    }

    public String getTitle(int position){
        return position == 0 ? getString(R.string.title_activity_main) : mThemes.get(position - 1).getName();
    }

    public void selectItem(int position) {
        if (position == mCurrentSelectedPosition) {
            ((NavigationDrawerActivity)getActivity()).closeDrawer();
            return;
        }
        mAdapter.selectPosition(position);
    }
    /*普通逻辑*/

    /*生命周期回调*/
    @Override
    public void onAttach(Context context) {
        L.i(TAG, "onAttach  " + this);
        super.onAttach(context);
        injectDependences();
        navigationPresenter.attachView(this);
        try {
            mCallbacks = (NavigationDrawerCallbacks) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationPresenter.refresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    /*生命周期回调*/















}
