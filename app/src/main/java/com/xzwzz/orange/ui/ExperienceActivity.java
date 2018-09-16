package com.xzwzz.orange.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.VideoListBean;
import com.xzwzz.orange.module.video.VideoListAdapter;
import com.xzwzz.orange.module.video.VideoPlayActivity;
import com.xzwzz.orange.widget.ViewStatusManager;

import java.util.List;

/**
 * 体验专区
 */
public class ExperienceActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ViewStatusManager mViewStatusManager;
    protected RecyclerView mRecyclerView;
    protected VideoListAdapter mAdapter;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_module;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbar("体验专区", true);
        mViewStatusManager = findViewById(R.id.viewstatusmanager);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mRecyclerView = findViewById(R.id.recycler_live);
        setRecyclerView();

    }


    @Override
    protected void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mViewStatusManager.setStatusChangeListener(() -> {
            loadData();
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new VideoListAdapter();

        View footView = View.inflate(this, R.layout.foot_view, null);
        footView.findViewById(R.id.btn_more).setOnClickListener(v -> startActivity(new Intent(this, VipActivity.class)));
        mAdapter.addFooterView(footView);
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(10f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 2 == 1) {
                    outRect.set(offest / 2, offest, offest, 0);
                }
            }
        });
    }

    protected void loadData() {
        RetrofitClient.getInstance().createApi().tiyan("Home.VideoList2").compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<VideoListBean>(mViewStatusManager, mSwipeRefreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<VideoListBean> list) {
                        mAdapter.setNewData(list);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        VideoListBean bean =  mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bean);
        bundle.putString("type","free");
        ActivityUtils.startActivity(bundle, VideoPlayActivity.class);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
