package com.xzwzz.orange.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.NoSuchPropertyException;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
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
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.module.book.BookModuleActivity;
import com.xzwzz.orange.module.live.LiveModuleActivity;
import com.xzwzz.orange.module.video.VideoModuleActivity;
import com.xzwzz.orange.module.wallper.WallperModuleActivity;
import com.xzwzz.orange.ui.LiveModule2Activity;
import com.xzwzz.orange.ui.TelevitionActivity;
import com.xzwzz.orange.ui.WebViewActivity;
import com.xzwzz.orange.ui.adapter.CategoryAdapter;
import com.xzwzz.orange.bean.CategoryBean;
import com.xzwzz.orange.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class MainFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BGABanner.Delegate {
    private android.support.v7.widget.Toolbar mToolbar;
    private cn.bingoogolapple.bgabanner.BGABanner mBanner;
    private android.widget.TextView mTvTitle;
    private android.support.v7.widget.RecyclerView mRecyclerMain;
    private CategoryAdapter mAdapter;
    List<HotBean.BannerBean> hotBeans = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view) {

        mToolbar = view.findViewById(R.id.toolbar);
        mBanner = view.findViewById(R.id.banner);
        mBanner.setDelegate(this);

        mTvTitle = view.findViewById(R.id.tv_title);
        mRecyclerMain = view.findViewById(R.id.recycler_main);
        StatusBarUtil.getInstance().setPaddingSmart(getActivity(), mToolbar);


        mRecyclerMain.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(10f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest, 0);
                } else if (position % 2 == 1) {
                    outRect.set(0, offest, offest, 0);
                }
            }
        });
    }

    @Override
    public void initData() {
        ArrayList<CategoryBean> list = new ArrayList<>();
//        list.add(new CategoryBean("直播专区", R.mipmap.ic_live));
        list.add(new CategoryBean("VIP影视", R.mipmap.ic_live));
        list.add(new CategoryBean("优选图片", R.mipmap.ic_wallper));
        list.add(new CategoryBean("原创小说", R.mipmap.ic_book));
        list.add(new CategoryBean("精彩视频", R.mipmap.ic_video));
        mAdapter = new CategoryAdapter(R.layout.item_category, list);
        mRecyclerMain.setAdapter(mAdapter);
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

        RetrofitClient.getInstance().createApi().getBanner().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<HotBean>() {
                    @Override
                    protected void onHandleSuccess(List<HotBean> list) {
                        if (list.size() > 0) {

                            List<HotBean.BannerBean> slide = list.get(0).slide;
                            hotBeans.clear();
                            hotBeans.addAll(slide);
                            if (slide.size() > 1) {
                                mBanner.setAutoPlayAble(true);
                            } else {
                                mBanner.setAutoPlayAble(false);
                            }
                            ArrayList<String> result = new ArrayList<>();
                            for (HotBean.BannerBean bean : slide) {
                                result.add(bean.slide_pic);
                            }
                            mBanner.setData(result, null);
                            mBanner.setAdapter((BGABanner.Adapter<ImageView, String>) (bgaBanner, view, o, i) -> GlideApp.with(getActivity())
                                    .load(o)
                                    .transition(new DrawableTransitionOptions().crossFade())
                                    .placeholder(R.drawable.placeholder_banner)
                                    .into(view));
                        }
                    }
                });
    }

    @Override
    protected void setListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CategoryBean item = mAdapter.getItem(position);

        switch (item.getTitle()) {
            case "VIP影视":
                ActivityUtils.startActivity(WebViewActivity.class);
                break;
            case "优选图片":
                ActivityUtils.startActivity(WallperModuleActivity.class);

                break;
            case "原创小说":
                ActivityUtils.startActivity(BookModuleActivity.class);
                break;
            case "精彩视频":
                ActivityUtils.startActivity(VideoModuleActivity.class);
                break;
            default:
                throw new NoSuchPropertyException("unknown error,please see ModuleMainActivity");
        }

    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        if (TextUtils.isEmpty(hotBeans.get(position).slide_url)) return;

        Uri uri = Uri.parse(hotBeans.get(position).slide_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
