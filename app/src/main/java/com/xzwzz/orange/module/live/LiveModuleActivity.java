package com.xzwzz.orange.module.live;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.PlatformBean1;
import com.xzwzz.orange.module.AbsModuleActivity;
import com.xzwzz.orange.module.live.adapter.LiveModuleAdapter;
import com.xzwzz.orange.widget.ViewStatusManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LiveModuleActivity extends AbsModuleActivity {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new LiveModuleAdapter();
    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(2f);
                if (position % 3 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 3 == 1) {
                    outRect.set(offest / 2, offest, offest / 2, 0);
                } else if (position % 3 == 2) {
                    outRect.set(offest / 2, offest, offest, 0);
                }
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbar("直播", true);
        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.loading);
    }

    @Override
    protected void loadData() {
        RetrofitClient.getInstance().createApi().getChannelList1(AppContext.channelUrl).compose(RxUtils.io_main()).subscribe(new Observer<PlatformBean1>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PlatformBean1 platformBean) {
                if (platformBean.getData()!=null&&platformBean.getData().size() == 0) {
                    mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.empty);
                    return;
                }
                mAdapter.setNewData(platformBean.getData());
                mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.success);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (mViewStatusManager != null) {
                    mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.error);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PlatformBean1.DataBean item = (PlatformBean1.DataBean) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("plamform", item.getTitle());
        bundle.putString("id", item.getLatform_type());
        ActivityUtils.startActivity(bundle, LiveChannelActivity.class);
    }
}


