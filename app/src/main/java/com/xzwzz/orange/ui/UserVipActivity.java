package com.xzwzz.orange.ui;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.ui.adapter.VipAdapter;
import com.xzwzz.orange.bean.BuyVipBean;
import com.xzwzz.orange.utils.DialogHelp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserVipActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private VipAdapter mAdapter;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        mRecyclerView = new RecyclerView(mContext);
        int offest = SizeUtils.dp2px(10f);
        mRecyclerView.setPadding(offest, 0, offest, 0);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return mRecyclerView;
    }

    @Override
    protected void initView() {
        setToolbar("我的会员", true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(8f);
                outRect.set(0, offest, 0, 0);
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new VipAdapter(R.layout.item_select_num, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        RetrofitClient.getInstance().createApi().getVipList("Charge.monthCardList", AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<BuyVipBean>(ProgressDialog.show(this, "", "加载中...")) {
                    @Override
                    protected void onHandleSuccess(List<BuyVipBean> list) {
                        if (list.size() > 0) {
                            BuyVipBean bean = list.get(0);
                            mAdapter.setNewData(bean.list);
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
        BuyVipBean.ListBean item = mAdapter.getItem(position);
        DialogHelp.getConfirmDialog(this, "确认花费" + item.coin + AppConfig.CURRENCY_NAME + "购买" + item.name + "?", (dialog, which) -> {
            RetrofitClient.getInstance().createApi().BuyVip("Charge.getcoinCard", AppContext.getInstance().getLoginUid(), item.id)
                    .compose(RxUtils.io_main())
                    .subscribe(new Observer<BaseBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseBean baseBean) {
                            if (baseBean.data.code == 1001) {
                                ToastUtils.showShort("您的" + AppConfig.CURRENCY_NAME + "不足，请充值");
                                return;
                            }
                            if (baseBean.data.code == 0) {
                                ToastUtils.showShort("开通成功");
                            } else {
                                if (!baseBean.data.msg.isEmpty()) {
                                    ToastUtils.showShort(baseBean.data.msg);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }).show();
    }
}
