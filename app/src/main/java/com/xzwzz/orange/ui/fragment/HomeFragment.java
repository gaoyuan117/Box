package com.xzwzz.orange.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseFragment;
import com.xzwzz.orange.bean.AdBean;
import com.xzwzz.orange.bean.HotBean;
import com.xzwzz.orange.bean.PlatformBean;
import com.xzwzz.orange.bean.PlatformBean1;
import com.xzwzz.orange.bean.PlatformBean2;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.module.live.LiveChannel2Activity;
import com.xzwzz.orange.module.live.LiveChannelActivity;
import com.xzwzz.orange.module.live.LiveModuleActivity;
import com.xzwzz.orange.module.live.adapter.LiveModule2Adapter;
import com.xzwzz.orange.module.live.adapter.LiveModuleAdapter;
import com.xzwzz.orange.ui.CollectActivity;
import com.xzwzz.orange.ui.LiveModule2Activity;
import com.xzwzz.orange.ui.login.LoginActivity;
import com.xzwzz.orange.utils.MyImageLoader;
import com.xzwzz.orange.utils.SharePrefUtil;
import com.xzwzz.orange.utils.StatusBarUtil;
import com.xzwzz.orange.widget.ViewStatusManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gaoyuan on 2018/6/5.
 */

public class HomeFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {
    private android.support.v7.widget.Toolbar mToolbar;
    private Banner mBanner;
    private android.widget.TextView mTvTitle;
    private android.support.v7.widget.RecyclerView mRecyclerMain;
    private List<HotBean.BannerBean> hotBeans = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();

    private LiveModuleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View headView;
    private TextView tvNum;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
        headView = View.inflate(getActivity(), R.layout.view_header_home, null);
        mBanner = headView.findViewById(R.id.banner);
        mTvTitle = headView.findViewById(R.id.tv_title);
        tvNum = headView.findViewById(R.id.tv_home_num);

        mSwipeRefreshLayout = view.findViewById(R.id.refresh);
        mToolbar = view.findViewById(R.id.toolbar);
        mRecyclerMain = view.findViewById(R.id.recycler_main);
        TextView tvCollect = view.findViewById(R.id.tv_collect);
        tvCollect.setVisibility(View.VISIBLE);
        tvCollect.setOnClickListener(v -> toCollectActivity());

        mAdapter = new LiveModuleAdapter();
//        StatusBarUtil.getInstance().setPaddingSmart(getActivity(), mToolbar);

        mRecyclerMain.setLayoutManager(new GridLayoutManager(mContext, 3));

        mRecyclerMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                if (position == 0) return;
                int offest = SizeUtils.dp2px(1f);
                if (position % 3 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 3 == 1) {
                    outRect.set(offest / 2, offest, offest / 2, 0);
                } else if (position % 3 == 2) {
                    outRect.set(offest / 2, offest, 0, 0);
                }
            }
        });
        mRecyclerMain.setAdapter(mAdapter);
        mAdapter.addHeaderView(headView);
    }

    private void toCollectActivity(){
        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }
        ActivityUtils.startActivity(CollectActivity.class);
    }

    @Override
    public void initData() {
        getAd();
        getBanner();
        String url = SharePrefUtil.getString("url", "");
        Log.e("gy","链接："+url);
        if (SharePrefUtil.getInt("type",0) == 1) {
            getList();
        } else {
            getList2();
        }
    }

    @Override
    protected void setListener() {
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mBanner.setOnBannerClickListener(this);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }

        if (AppContext.channel_type == 1) {
            PlatformBean1.DataBean item = (PlatformBean1.DataBean) adapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString("plamform", item.getTitle());
            bundle.putString("id", item.getLatform_type());
            bundle.putString("logo", item.getImg());
            ActivityUtils.startActivity(bundle, LiveChannelActivity.class);

        } else {
            PlatformBean1.DataBean item = (PlatformBean1.DataBean) adapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString("plamform", item.getTitle());
            bundle.putString("id", item.getLatform_type());
            bundle.putString("logo", item.getImg());
            ActivityUtils.startActivity(bundle, LiveChannel2Activity.class);
        }
    }

    @Override
    public void onRefresh() {
        if (AppContext.channel_type == 1) {
            getList();
        } else {
            getList2();
        }

        getBanner();

    }

    //获取广告
    private void getAd() {
        RetrofitClient.getInstance().createApi().getAd().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<AdBean>() {
                    @Override
                    protected void onHandleSuccess(List<AdBean> list) {
                        if (list.size() > 0) {
                            mTvTitle.setText(list.get(0).content);
                            mTvTitle.setSelected(true);
                        }
                    }
                });
    }

    //获取轮播图
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

    //获取直播列表
    private void getList() {
        String url = SharePrefUtil.getString("url", "");
        Log.e("gy","链接："+url);
        if (TextUtils.isEmpty(url)) return;
        RetrofitClient.getInstance().createApi().getChannelList1(url)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<PlatformBean1>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PlatformBean1 platformBean) {
                        if (platformBean.getData() == null || platformBean.getData().size() == 0) {
                            return;
                        }
                        mAdapter.setNewData(platformBean.getData());
                        tvNum.setText("共" + platformBean.getData().size() + "个");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getList2() {
        String url = SharePrefUtil.getString("url", "");
        Log.e("gy","链接："+url);
        if (TextUtils.isEmpty(url)) return;
        RetrofitClient.getInstance().createApi().getChannelList2(url)
                .compose(RxUtils.io_main())
                .subscribe(new Consumer<PlatformBean2>() {
                    @Override
                    public void accept(PlatformBean2 platformBean2) throws Exception {

                        mSwipeRefreshLayout.setRefreshing(false);

                        if (platformBean2.getPingtai() != null && platformBean2.getPingtai().size() > 0) {

                            List<PlatformBean1.DataBean> list = new ArrayList<>();
                            for (int i = 0; i < platformBean2.getPingtai().size(); i++) {
                                PlatformBean2.PingtaiBean pingtaiBean = platformBean2.getPingtai().get(i);

                                PlatformBean1.DataBean bean = new PlatformBean1.DataBean();
                                bean.setLatform_type(pingtaiBean.getAddress());
                                bean.setImg(pingtaiBean.getXinimg());
                                bean.setTitle(pingtaiBean.getTitle());
                                bean.setNumber(pingtaiBean.getNumber() + "");
                                list.add(bean);
                            }

                            mAdapter.setNewData(list);
                            tvNum.setText("共" + list.size() + "个");
                        }

                    }
                });
    }


    @Override
    public void OnBannerClick(int position) {
        if (TextUtils.isEmpty(hotBeans.get(position-1).slide_url)) return;

        Uri uri = Uri.parse(hotBeans.get(position-1).slide_url);
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
