package com.xzwzz.orange.module.live;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.ChannelDataBean;
import com.xzwzz.orange.bean.UserInfoBean;
import com.xzwzz.orange.bean.ZhuBoBean;
import com.xzwzz.orange.module.AbsModuleActivity;
import com.xzwzz.orange.module.live.adapter.LiveChannelAdapter;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.MemberUtil;
import com.xzwzz.orange.utils.SharePrefUtil;
import com.xzwzz.orange.utils.StatusBarUtil;
import com.xzwzz.orange.widget.ViewStatusManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.functions.Consumer;

public class LiveChannel2Activity extends AbsModuleActivity {
    private String id, logo;
    private ProgressDialog mProgressDialog;
    private View headView;
    private ImageView imgLogo;
    private TextView tvNum, tvName;
    private String title1;
    private Random random;

    @Override
    protected void initView() {
        super.initView();
        Bundle extras = getIntent().getExtras();
        title1 = extras.getString("plamform");
        logo = extras.getString("logo");
        setToolbar(title1);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> finish());
        id = extras.getString("id");
        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.loading);

        headView = View.inflate(this, R.layout.view_channel_head, null);
        imgLogo = headView.findViewById(R.id.img_channel_logo);
        tvNum = headView.findViewById(R.id.tv_num);
        tvName = headView.findViewById(R.id.tv_name);
        tvName.setText(title1);
        Glide.with(this).load(logo).into(imgLogo);

        mAdapter.addHeaderView(headView);

    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                if (position == 0) return;
                int offest = SizeUtils.dp2px(5f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 2 == 1) {
                    outRect.set(offest / 2, offest, offest, 0);
                }
            }
        });
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new LiveChannelAdapter();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void loadData() {
        random = new Random();
        String url = SharePrefUtil.getString("dataurl", "");
        if (TextUtils.isEmpty(url)) return;
        RetrofitClient.getInstance().createApi().getChannelData2(url + id)
                .compose(RxUtils.io_main())
                .subscribe(zhuBoBean -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (zhuBoBean.getZhubo() != null && zhuBoBean.getZhubo().size() > 0) {

                        List<ChannelDataBean.DataBean> list = new ArrayList<>();

                        for (int i = 0; i < zhuBoBean.getZhubo().size(); i++) {
                            ChannelDataBean.DataBean bean = new ChannelDataBean.DataBean();
                            ZhuBoBean.ZhuboBean zhuboBean = zhuBoBean.getZhubo().get(i);
                            bean.setBigpic(zhuboBean.getImg());
                            bean.setName(zhuboBean.getTitle());
                            bean.setUrl(zhuboBean.getAddress());
                            bean.setNum(random.nextInt(1000) + 100 + "");

                            list.add(bean);
                        }
                        mAdapter.setNewData(list);
                        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.success);
                        tvNum.setText(list.size() + "");
                    } else {
                        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.empty);
                    }
                });


    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ChannelDataBean.DataBean item = (ChannelDataBean.DataBean) adapter.getItem(position);
        goRoomV1(item);
    }


    private void goRoomV1(ChannelDataBean.DataBean item) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", item);
                ActivityUtils.startActivity(bundle, LivePlayActivity.class);
            }

            @Override
            public void noMember() {
                dialog();
            }
        }));

    }

    private void dialog() {
        Dialog dialog = new Dialog(this, R.style.wx_dialog);
        View view = View.inflate(this, R.layout.dialog_commom, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText("账号到期，请续费");
        TextView tvClose = view.findViewById(R.id.tv_close);
        tvClose.setText("续费");
        tvClose.setOnClickListener(v -> {
            ActivityUtils.startActivity(VipActivity.class);
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginUtils.isWifiProxy(this)) {
            finish();
        }
        isMember();
    }

    private void isMember() {
        RetrofitClient.getInstance().createApi().getBaseUserInfo("User.getBaseInfo", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<UserInfoBean>() {
                    @Override
                    protected void onHandleSuccess(List<UserInfoBean> list) {
                        if (list.size() > 0) {
                            UserInfoBean bean = list.get(0);
                            AppConfig.IS_MEMBER = (bean.is_member == 1);
                        }
                    }
                });
    }


}
