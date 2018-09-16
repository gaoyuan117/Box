package com.xzwzz.orange.module.video;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.VideoBean;
import com.xzwzz.orange.module.AbsModuleActivity;

import java.util.List;

public class VideoModuleActivity extends AbsModuleActivity {
    @Override
    protected BaseQuickAdapter getAdapter() {
        return new VideoAdapter();
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbar("视频", true);
    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(10f);
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
    protected void loadData() {
        RetrofitClient.getInstance().createApi().getVideoList().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<VideoBean>(mViewStatusManager, mSwipeRefreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<VideoBean> list) {
                        mAdapter.setNewData(list);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        VideoBean item = (VideoBean) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("name", item.name);
        bundle.putString("id", item.term_id);
        ActivityUtils.startActivity(bundle, VideoListActivity.class);


    }
}
