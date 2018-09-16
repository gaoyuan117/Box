package com.xzwzz.orange.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseFragment;
import com.xzwzz.orange.bean.AdListBean;
import com.xzwzz.orange.bean.HotBean;
import com.xzwzz.orange.ui.adapter.ViewPagerAdapter;
import com.xzwzz.orange.utils.MyImageLoader;
import com.xzwzz.orange.utils.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class AvFragment extends BaseFragment implements OnBannerClickListener {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar mToolbar;
    private Banner mBanner;

    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;
    private List<String> titles;
    private List<String> bannerList = new ArrayList<>();
    private List<AdListBean> adListBeans;
    private List<HotBean.BannerBean> hotBeans = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_av;
    }

    @Override
    public void initView(View view) {
        mBanner = view.findViewById(R.id.banner);
        mToolbar = view.findViewById(R.id.toolbar);
        mTabLayout = view.findViewById(R.id.tb_my);
        mViewPager = view.findViewById(R.id.vp_my);
        appBarLayout = view.findViewById(R.id.appbar);

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        Log.e("gy","标题："+ AppContext.novelTermList.size());
        for (int i = 0; i < AppContext.novelTermList.size(); i++) {
            AvItemFragment f = new AvItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", AppContext.novelTermList.get(i).getTerm_id());
            f.setArguments(bundle);
            fragments.add(f);
            titles.add(AppContext.novelTermList.get(i).getName());
        }

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), fragments, titles);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

    }

    @Override
    public void initData() {
        getBanner();
        mBanner.setOnBannerClickListener(this);
    }

    private void getBanner() {
        RetrofitClient.getInstance().createApi().getBanner().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<HotBean>() {
                    @Override
                    protected void onHandleSuccess(List<HotBean> list) {
                        if (list.size() > 0) {

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

    @Override
    public void OnBannerClick(int position) {
        if (TextUtils.isEmpty(adListBeans.get(position - 1).getUrl()) || !adListBeans.get(position - 1).getUrl().startsWith("http"))
            return;
        Uri uri = Uri.parse(hotBeans.get(position - 1).slide_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
