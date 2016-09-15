package io.hefuyi.zhihudaily.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    private static final String ARG_THEME_NUMBER = "theme_number";
    private static final String ARG_THEME_ID = "theme_id";

    private int mArgThemeNumber;
    private String mArgThemeId;

    public abstract void refreshUI();

    public abstract void smoothToTop();

    //get fragment by position
    public static BaseFragment newInstance(int position, String sectionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_THEME_NUMBER, position);
        bundle.putString(ARG_THEME_ID, sectionId);
        BaseFragment fragment;
        if (position == 0) {
            fragment = new DailyStoriesFragment();
        } else {
            fragment = new ThemeStoriesFragment();
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgThemeNumber = getArguments().getInt(ARG_THEME_NUMBER);
        mArgThemeId = getArguments().getString(ARG_THEME_ID);
    }

    public int getThemeNumber() {
        return mArgThemeNumber;
    }

    public String getThemeId() {
        return mArgThemeId;
    }
}
