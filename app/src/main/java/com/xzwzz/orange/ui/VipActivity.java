package com.xzwzz.orange.ui;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.UserInfoBean;
import com.xzwzz.orange.bean.VipBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VipActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private TextView textView, tvWx;

    private String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        initView();
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText("会员续费");
    }


    protected void initView() {

        editText = findViewById(R.id.et_kami);
        textView = findViewById(R.id.text);
        textView.setText(AppContext.text + "");
        findViewById(R.id.img_nianka).setOnClickListener(this);
        findViewById(R.id.img_yueka).setOnClickListener(this);
        findViewById(R.id.img_jika).setOnClickListener(this);
        findViewById(R.id.img_zhongshen).setOnClickListener(this);

        findViewById(R.id.btn_jihuo).setOnClickListener(this);

        findViewById(R.id.tv_kefu).setOnClickListener(this);

        tvWx = findViewById(R.id.tv_wx);
        tvWx.setOnClickListener(this);
        tvWx.setText(AppConfig.QQ);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_yueka:
                ka(AppConfig.YUE);
                break;
            case R.id.img_jika:
                ka(AppConfig.JI);
                break;
            case R.id.img_nianka:
                ka(AppConfig.YEAR);
                break;

            case R.id.img_zhongshen:
                ka(AppConfig.FOREVER);
                break;

            case R.id.tv_kefu:
                toQq();
                break;

            case R.id.btn_jihuo:
                kami();
                break;
            case R.id.tv_wx:
                copy();
                break;
        }
    }

    private void copy() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(AppConfig.QQ);
        ToastUtils.showShort("复制成功：" + AppConfig.QQ);
    }


    private void ka(String url) {
        if (TextUtils.isEmpty(url)) {
            wxDialog();

        } else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void wxDialog() {
        Dialog dialog = new Dialog(this, R.style.wx_dialog);
        View view = View.inflate(this, R.layout.dialog_commom, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText("请联系客服购买卡密\n" + AppConfig.QQ);
        view.findViewById(R.id.tv_close).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }


    private void kami() {
        s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            ToastUtils.showShort("请输入卡密");
            return;
        }
        RetrofitClient.getInstance().createApi().kami("Charge.exchange", AppContext.getInstance().getLoginUid(), s)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<VipBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VipBean vipBean) {
                        if (vipBean.getData().getCode() == 0) {
                            ToastUtils.showShort("续费成功");
                            isMember();
                            finish();
                        } else {
                            ToastUtils.showShort(vipBean.getData().getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

    private void toQq() {
        if (isQQClientAvailable(VipActivity.this)) {
            // 跳转到客服的QQ
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + AppConfig.QQ;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
            if (isValidIntent(VipActivity.this, intent)) {
                startActivity(intent);
            }
        }
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 Uri是否有效
     */
    public boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

}
