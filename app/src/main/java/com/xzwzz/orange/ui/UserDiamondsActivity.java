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
import com.xzwzz.orange.bean.BalanceBean;
import com.xzwzz.orange.pay.WxPay.WChatPay;
import com.xzwzz.orange.pay.alipay.AliPay;
import com.xzwzz.orange.pay.alipay.Keys;
import com.xzwzz.orange.pay.alipay.alipay.Constants;
import com.xzwzz.orange.ui.adapter.UserDiamondsAdapter;
import com.xzwzz.orange.utils.DialogHelp;

import java.util.ArrayList;
import java.util.List;

public class UserDiamondsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    public static final int ALI_PAY = 1, WX_PAY = 2;
    private android.widget.TextView mTvMoney;
    private android.support.v7.widget.RecyclerView mRecyclerWallet;
    private UserDiamondsAdapter mAdapter;
    private WChatPay mWChatPay;
    private AliPay mAliPayUtils;
    private boolean openAli, openWx;
    private AliPay.PayListener mPayListener = new AliPay.PayListener() {
        @Override
        public void success() {
            ToastUtils.showShort("支付成功");
            loadData();
        }

        @Override
        public void fail(String msg) {
            ToastUtils.showShort("支付失败");

        }
    };

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mAliPayUtils = new AliPay(this);

        mAdapter = new UserDiamondsAdapter(R.layout.item_select_num, new ArrayList<>());
        mRecyclerWallet.setAdapter(mAdapter);

        RetrofitClient.getInstance().createApi().getBalance("User.getBalance", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<BalanceBean>(ProgressDialog.show(this, "", "加载中...")) {
                    @Override
                    protected void onHandleSuccess(List<BalanceBean> list) {
                        if (list.size() > 0) {
                            BalanceBean bean = list.get(0);
                            mAdapter.setNewData(bean.rules);
                            mTvMoney.setText(bean.coin);

                            //微信支付appid
                            AppConfig.GLOBAL_WX_KEY = bean.wx_appid;

                            //支付宝
                            Keys.DEFAULT_PARTNER = bean.aliapp_partner;
                            Keys.DEFAULT_SELLER = bean.aliapp_seller_id;
                            Keys.PRIVATE = bean.aliapp_key_android;


                            Constants.AliPay.APPID = bean.aliapp_partner;
                            Constants.AliPay.RSA_PRIVATE = bean.aliapp_key_android;

                            Constants.WxPay.APP_ID = bean.wx_appid;
                            Constants.WxPay.WX_SHOP_NUM = bean.wx_mchid;
                            Constants.WxPay.KEY = bean.wx_key;

                            openWx = "1".equals(bean.wx_switch);
                            openAli = "1".equals(bean.aliapp_switch);
                        }
                    }
                });
        loadData();
    }

    @Override
    protected void initView() {
        setToolbar("我的金币", true);

        mTvMoney = findViewById(R.id.tv_money);
        mRecyclerWallet = findViewById(R.id.recycler_wallet);

        mRecyclerWallet.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerWallet.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(8f);
                outRect.set(0, offest, 0, 0);
            }
        });
    }

    private void loadData() {
        RetrofitClient.getInstance().createApi().getBalance("User.getBalance", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<BalanceBean>(ProgressDialog.show(this, "", "加载中...")) {
                    @Override
                    protected void onHandleSuccess(List<BalanceBean> list) {
                        if (list.size() > 0) {
                            BalanceBean bean = list.get(0);
                            mAdapter.setNewData(bean.rules);
                            mTvMoney.setText(bean.coin);

                            //微信支付appid
                            AppConfig.GLOBAL_WX_KEY = bean.wx_appid;

                            //支付宝
                            Keys.DEFAULT_PARTNER = bean.aliapp_partner;
                            Keys.DEFAULT_SELLER = bean.aliapp_seller_id;
                            Keys.PRIVATE = bean.aliapp_key_android;


                            Constants.AliPay.APPID = bean.aliapp_partner;
                            Constants.AliPay.RSA_PRIVATE = bean.aliapp_key_android;

                            Constants.WxPay.APP_ID = bean.wx_appid;
                            Constants.WxPay.WX_SHOP_NUM = bean.wx_mchid;
                            Constants.WxPay.KEY = bean.wx_key;

                            openWx = "1".equals(bean.wx_switch);
                            openAli = "1".equals(bean.aliapp_switch);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DialogHelp.getSelectDialog(UserDiamondsActivity.this, new String[]{"支付宝", "微信"}, (dialogInterface, i) -> {
            int pay_mode = i == 0 ? ALI_PAY : WX_PAY;
            BalanceBean.RulesBean bean = mAdapter.getItem(position);
            actionPay(bean.money, bean.coin, bean.id, pay_mode);

        }).create().show();
    }

    private void actionPay(String money, String num, String changeid, int mode) {

        if (mode == ALI_PAY && checkPayMode(mode)) {

            mAliPayUtils.initPay(money, num, "1", changeid, mPayListener);

        } else if (checkPayMode(mode)) {

//            mWChatPay.initPay(money, num, changeid);
            mAliPayUtils.initPay(money, num, "2", changeid, mPayListener);
        }
    }

    //检查支付配置
    private boolean checkPayMode(int mode) {

        if (mode == ALI_PAY) {
            if (openAli) {
                return true;
            } else {

                ToastUtils.showShort("支付宝未开启");
                return false;
            }
        } else if (mode == WX_PAY) {
            if (openWx) {
                return true;
            } else {
                ToastUtils.showShort("微信未开启");
                return false;
            }
        }

        return false;

    }
}
