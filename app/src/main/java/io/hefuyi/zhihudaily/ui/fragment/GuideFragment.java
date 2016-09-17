package io.hefuyi.zhihudaily.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.ui.activity.GuiderActivity;
import io.hefuyi.zhihudaily.util.SharedPrefUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {

    public static final String TAG = GuideFragment.class.getSimpleName();

    private final String hello = "<h1>Welcome<h1><p>This application is a simple demonstration of Zhihu Daily Android developed by me. It's a free app. All the information, content and api copyright is belong to Zhihu. Inc.</Br><p>-Hefuyi</p></p><h2>About</h2><p>Email: hefuyicoder@gmail.com</p><p>Github: github.com/hefuyicoder</p>";

    @Bind(R.id.tvHello)
    TextView tvHello;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, rootView);
        tvHello.setText(Html.fromHtml(hello));
        return rootView;
    }

    @OnClick(R.id.btnEnter)
    public void onEnterClick() {
        SharedPrefUtils.markFirstLaunch(getActivity());
        ((GuiderActivity)getActivity()).intentToMainActivity();
    }
}
