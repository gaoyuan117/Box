package com.xzwzz.orange.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseFragment;
import com.xzwzz.orange.bean.ChannelDataBean;
import com.xzwzz.orange.bean.HotBean;
import com.xzwzz.orange.bean.TvTermBean;
import com.xzwzz.orange.bean.YingListBean;
import com.xzwzz.orange.module.live.LivePlayActivity;
import com.xzwzz.orange.ui.TvActivity;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.ui.WebViewActivity;
import com.xzwzz.orange.ui.adapter.YingAdapter;
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

public class YingFragment extends BaseFragment implements OnBannerClickListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private android.support.v7.widget.Toolbar mToolbar;
    private Banner mBanner;
    private List<HotBean.BannerBean> hotBeans = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private YingAdapter adapter;
    private List<YingListBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ying;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler);
        mToolbar = view.findViewById(R.id.toolbar);
        mBanner = view.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
//        StatusBarUtil.getInstance().setPaddingSmart(getActivity(), mToolbar);

        view.findViewById(R.id.img_ying).setOnClickListener(this);
        view.findViewById(R.id.img_wei).setOnClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                if (position == 0) return;
                int offest = SizeUtils.dp2px(1f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 2 == 1) {
                    outRect.set(offest / 2, offest, offest / 2, 0);
                }
            }
        });

        adapter = new YingAdapter(R.layout.item_ying, list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        getBanner();
        getData();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void OnBannerClick(int position) {

        if (TextUtils.isEmpty(hotBeans.get(position - 1).slide_url)) return;

        Uri uri = Uri.parse(hotBeans.get(position - 1).slide_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    private void getBanner() {
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

    @Override
    public void onClick(View v) {

        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (v.getId()) {
            case R.id.img_ying://VIP影视

//                toActivity(WebViewActivity.class);

                break;
            case R.id.img_wei://卫视直播
//                toActivity(TvActivity.class);

                break;
        }
    }

    private void getData() {
        RetrofitClient.getInstance().createApi().yingList("Home.MoviesLinkList")
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<YingListBean>() {
                    @Override
                    protected void onHandleSuccess(List<YingListBean> lists) {
                        if (lists == null || lists.size() == 0) return;
                        list.clear();
                        list.addAll(lists);
                        adapter.notifyDataSetChanged();

                    }
                });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toActivity(list.get(position).getUrl(),list.get(position).getTitle());
    }

    private void toActivity(String url,String title) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                startActivity(intent);
            }

            @Override
            public void noMember() {
                LoginUtils.vipDialog(getActivity());
            }
        }));
    }

}
