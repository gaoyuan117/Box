package com.xzwzz.orange.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseFragment;
import com.xzwzz.orange.bean.HotBean;
import com.xzwzz.orange.ui.NovelActivity;
import com.xzwzz.orange.ui.VideoActivity;
import com.xzwzz.orange.ui.adapter.VideoAdapter;
import com.xzwzz.orange.ui.login.LoginActivity;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.MemberUtil;
import com.xzwzz.orange.utils.MyImageLoader;
import com.xzwzz.orange.utils.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyuan on 2018/6/7.
 */

public class YunBoFragment extends BaseFragment implements OnBannerClickListener, View.OnClickListener {
    private android.support.v7.widget.Toolbar mToolbar;
    private Banner mBanner;
    private List<HotBean.BannerBean> hotBeans = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_yun;
    }

    @Override
    public void initView(View view) {

        mToolbar = view.findViewById(R.id.toolbar);
        mBanner = view.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
//        StatusBarUtil.getInstance().setPaddingSmart(getActivity(), mToolbar);
        view.findViewById(R.id.img_novel).setOnClickListener(this);
        view.findViewById(R.id.img_video).setOnClickListener(this);

    }

    @Override
    public void initData() {
        RetrofitClient.getInstance().createApi().getBanner().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<HotBean>() {
                    @Override
                    protected void onHandleSuccess(List<HotBean> list) {
                        if (list != null && list.size() > 0) {

                            List<HotBean.BannerBean> slide = list.get(0).slide;
                            hotBeans.clear();
                            hotBeans.addAll(slide);
                            bannerList.clear();
                            for (HotBean.BannerBean bean : slide) {
                                bannerList.add(bean.slide_pic);
                            }
                            setBanner();
                        }
                    }
                });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (view.getId()) {

            case R.id.img_novel:

                toActivity(NovelActivity.class);
                break;
            case R.id.img_video:

                toActivity(VideoActivity.class);
                break;
        }
    }


    private void toActivity(Class<?> activity) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                startActivity(new Intent(getActivity(), activity));
            }

            @Override
            public void noMember() {
                LoginUtils.vipDialog(getActivity());
            }
        }));
    }


    @Override
    public void OnBannerClick(int position) {
        if (TextUtils.isEmpty(hotBeans.get(position - 1).slide_url)) return;

        Uri uri = Uri.parse(hotBeans.get(position - 1).slide_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /**
     * 设置轮播图
     */
    private void setBanner() {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new MyImageLoader());
        mBanner.setImages(bannerList);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setViewPagerIsScroll(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }


}
