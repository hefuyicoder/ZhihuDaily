package io.hefuyi.zhihudaily.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.hefuyi.zhihudaily.DailyApplication;
import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.animation.AnimationEndListener;
import io.hefuyi.zhihudaily.injector.component.ApplicationComponent;
import io.hefuyi.zhihudaily.injector.component.DaggerSplashComponent;
import io.hefuyi.zhihudaily.injector.component.SplashComponent;
import io.hefuyi.zhihudaily.injector.module.ActivityModule;
import io.hefuyi.zhihudaily.injector.module.SplashModule;
import io.hefuyi.zhihudaily.mvp.contract.SplashContract;
import io.hefuyi.zhihudaily.mvp.model.StartImage;
import io.hefuyi.zhihudaily.ui.activity.GuiderActivity;
import io.hefuyi.zhihudaily.util.DensityUtil;
import io.hefuyi.zhihudaily.util.L;
import io.hefuyi.zhihudaily.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment implements SplashContract.View{

    public static final String TAG = SplashFragment.class.getSimpleName();

    @Inject
    SplashContract.Presenter splashPresenter;
    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.splash)
    ImageView ivSplash;

    private Animation mIvSplashAnim;
    private int mWidth;
    private int mHeight;

    /*初始化相关*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        splashPresenter.attachView(this);
        mIvSplashAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash);
        //计算屏幕宽度和高度
        //if api>=19 use themes in values-v19 has a full screen splash
        mWidth = DensityUtil.getScreenWidth(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHeight = DensityUtil.getScreenHeightWithDecorations(getActivity());
        } else {
            mHeight = DensityUtil.getScreenHeight(getActivity());
        }
        L.i(TAG, "screen height = " + mHeight);
    }

    private void injectDependencies() {
        ApplicationComponent applicationComponent = ((DailyApplication) getActivity().getApplication()).getApplicationComponent();
        SplashComponent splashComponent = DaggerSplashComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .splashModule(new SplashModule())
                .build();
        splashComponent.inject(this);
    }
    /*初始化相关*/

    /*view接口*/
    @Override
    public void showBackgroundImage(final StartImage startImage) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvAuthor.setText(startImage.getText());
                Glide.with(getActivity())
                        .load(startImage.getImg())
                        .error(R.drawable.bg_splash)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                ivSplash.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivSplash.startAnimation(mIvSplashAnim);
                                    }
                                });
                                return false;
                            }
                        })
                        .centerCrop()
                        .into(ivSplash);

            }
        };
        getActivity().runOnUiThread(runnable);
    }

    @Override
    public void showError() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "refresh error", Toast.LENGTH_SHORT).show();

            }
        };
        getActivity().runOnUiThread(runnable);
    }
    /*view接口*/

    /*普通逻辑*/
    /*普通逻辑*/

    /*生命周期回调*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //if api>=19 use themes in values-v19 has a full screen splash.
        //so we need relayout the tvAuthor and ivLogo by set bigger marginButtom
        /*根据透明导航栏做调整*/
        int navigationBarHeight = UIUtils.getStatusBarHeight(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int tvMarginBottom = navigationBarHeight + DensityUtil.dip2px(getActivity(), 8);
            int logoMarginBottom = navigationBarHeight + DensityUtil.dip2px(getActivity(), 48);
            ((RelativeLayout.LayoutParams) tvAuthor.getLayoutParams()).setMargins(0, 0, 0, tvMarginBottom);
            ((RelativeLayout.LayoutParams) ivLogo.getLayoutParams()).setMargins(0, 0, 0, logoMarginBottom);

            tvAuthor.requestLayout();
            ivLogo.requestLayout();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置动画监听器,加载启动图片
        mIvSplashAnim.setAnimationListener(animationListener);
        splashPresenter.refresh(mWidth,mHeight);
    }
    /*生命周期回调*/

    private final Animation.AnimationListener animationListener=new AnimationEndListener(){
        @Override
        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            //启动图片消失
            ivSplash.setVisibility(View.GONE);
            ((GuiderActivity)getActivity()).intentToMainActivity();
        }
    };

}
