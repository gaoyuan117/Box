package com.xzwzz.orange.module.video;

import android.content.Intent;
import android.graphics.Rect;
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
import com.xzwzz.orange.bean.VideoListBean;
import com.xzwzz.orange.module.AbsModuleActivity;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.MemberUtil;

import java.lang.ref.WeakReference;
import java.util.List;

public class VideoListActivity extends AbsModuleActivity {
    private String id;

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new VideoListAdapter();
    }

    @Override
    protected void initView() {
        super.initView();
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("name");
        setToolbar(title, true);
        id = extras.getString("id");
    }

    @Override
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

    @Override
    protected void loadData() {
        RetrofitClient.getInstance().createApi().getVideoListData("Home.VideoList", id).compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<VideoListBean>(mViewStatusManager, mSwipeRefreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<VideoListBean> list) {
                        mAdapter.setNewData(list);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                VideoListBean bean = (VideoListBean) mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", bean);
                ActivityUtils.startActivity(bundle, VideoPlayActivity.class);
            }

            @Override
            public void noMember() {
                DialogHelp.showfreetimeOutDialog(VideoListActivity.this, getResources().getString(R.string.app_name) + "平台面向全国招收代理，独立的分销系统，   会员分享方式，让代理真真正正的躺在床上挣钱！开通会员免费观看所有直播！", (View.OnClickListener) v -> {
                    startActivity(new Intent(VideoListActivity.this, VipActivity.class));
                }).show();
            }
        }));
    }
}
