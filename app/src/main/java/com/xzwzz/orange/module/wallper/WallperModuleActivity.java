package com.xzwzz.orange.module.wallper;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.WallperBean;
import com.xzwzz.orange.module.AbsModuleActivity;
import com.xzwzz.orange.ui.UserVipActivity;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.MemberUtil;

import java.lang.ref.WeakReference;
import java.util.List;

public class WallperModuleActivity extends AbsModuleActivity {

    @Override
    protected void initView() {
        super.initView();
        setToolbar("图片", true);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new WallperAdapter();
    }

    @Override
    protected void loadData() {
        RetrofitClient.getInstance().createApi().getWallperList().compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<WallperBean>(mViewStatusManager, mSwipeRefreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<WallperBean> list) {
                        mAdapter.setNewData(list);
                    }
                });
    }

    @Override
    protected void setRecyclerView() {
        super.setRecyclerView();
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(2f);
                outRect.set(0, offest, 0, 0);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                WallperBean item = (WallperBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", item.id);
                bundle.putString("title", item.title);
                ActivityUtils.startActivity(bundle, WallperListActivity.class);
            }

            @Override
            public void noMember() {
                DialogHelp.showfreetimeOutDialog(WallperModuleActivity.this, getResources().getString(R.string.app_name) + "平台面向全国招收代理，独立的分销系统，   会员分享方式，让代理真真正正的躺在床上挣钱！开通会员免费观看所有直播！", (View.OnClickListener) v -> {
                    startActivity(new Intent(WallperModuleActivity.this, UserVipActivity.class));
                    finish();
                }).show();
            }
        }));
    }
}
