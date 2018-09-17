package com.aaaa.falas.ui;

import android.app.ProgressDialog;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.R;
import com.aaaa.falas.api.http.BaseListObserver;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.base.BaseActivity;
import com.aaaa.falas.bean.BaseBean;
import com.aaaa.falas.bean.ProfitBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserProfitActivity extends BaseActivity implements View.OnClickListener {

    private android.widget.TextView mTvMoneyName;
    private android.widget.TextView mTvMoney;
    private android.widget.EditText mEtName;
    private android.widget.EditText mEtAliaccount;
    private android.widget.EditText mEtMoney;
    private android.widget.Button mBtnConfirm;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_money;
    }

    @Override
    protected void initView() {

        mTvMoneyName = findViewById(R.id.tv_moneyName);
        mTvMoney = findViewById(R.id.tv_money);
        mEtName = findViewById(R.id.et_name);
        mEtAliaccount = findViewById(R.id.et_aliaccount);
        mEtMoney = findViewById(R.id.et_money);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        setToolbar("我的收益", true);
        mTvMoneyName.setText("可提现金额");

    }

    @Override
    protected void setListener() {
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = mEtName.getText().toString();
        String aliName = mEtAliaccount.getText().toString();
        String money = mEtMoney.getText().toString();
        if (name.isEmpty()) {
            ToastUtils.showShort("请输入姓名");
            return;
        }
        if (aliName.isEmpty()) {
            ToastUtils.showShort("请输入支付宝帐号");
            return;
        }
        if (money.isEmpty()) {
            ToastUtils.showShort("请输入提现金额");
            return;
        }
        RetrofitClient.getInstance().createApi().requestCash("User.setCash", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid()
                , aliName, name, money)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        ToastUtils.showShort(baseBean.data.msg + "");
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void initData() {
        RetrofitClient.getInstance().createApi().getWithdraw("User.getProfit", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<ProfitBean>(ProgressDialog.show(this, "", "加载中...")) {
                    @Override
                    protected void onHandleSuccess(List<ProfitBean> list) {
                        if (list.size() > 0) {
                            ProfitBean bean = list.get(0);
                            mTvMoney.setText(bean.todaycash);
                        }
                    }
                });
    }
}
