package com.aaaa.falas.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.bean.PlatformBean;
import com.aaaa.falas.bean.PlatformBean2;
import com.aaaa.falas.module.AbsModuleActivity;
import com.aaaa.falas.module.live.LiveChannel2Activity;
import com.aaaa.falas.module.live.adapter.LiveModule2Adapter;
import com.aaaa.falas.widget.ViewStatusManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class LiveModule2Activity extends AbsModuleActivity {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new LiveModule2Adapter();
    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
//        mRecyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_darker_gray));
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

        RetrofitClient.getInstance().createApi().getChannelList2(AppContext.channelUrl)
                .compose(RxUtils.io_main())
                .subscribe(new Consumer<PlatformBean2>() {
                    @Override
                    public void accept(PlatformBean2 platformBean2) throws Exception {

                        mSwipeRefreshLayout.setRefreshing(false);
                        if (platformBean2.getPingtai() != null && platformBean2.getPingtai().size() > 0) {
                            List<PlatformBean.DataBean> list = new ArrayList<>();
                            for (int i = 0; i < platformBean2.getPingtai().size(); i++) {
                                PlatformBean2.PingtaiBean pingtaiBean = platformBean2.getPingtai().get(i);
                                if (pingtaiBean.getNumber().equals("0") || TextUtils.isEmpty(pingtaiBean.getNumber()))
                                    continue;
                                PlatformBean.DataBean bean = new PlatformBean.DataBean();
                                bean.setId(pingtaiBean.getAddress());
                                bean.setLogo(pingtaiBean.getXinimg());
                                bean.setName(pingtaiBean.getTitle());
                                bean.setNum(pingtaiBean.getNumber());
                                list.add(bean);
                            }

                            mAdapter.setNewData(list);
                            mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.success);
                        }

                    }
                });

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PlatformBean.DataBean item = (PlatformBean.DataBean) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("plamform", item.getName());
        bundle.putString("id", item.getId());
        ActivityUtils.startActivity(bundle, LiveChannel2Activity.class);
    }
}
