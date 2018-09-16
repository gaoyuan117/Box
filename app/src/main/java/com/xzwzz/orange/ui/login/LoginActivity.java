package com.xzwzz.orange.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.ConfigBean;
import com.xzwzz.orange.bean.UserBean;
import com.xzwzz.orange.ui.HomeActivity;
import com.xzwzz.orange.ui.MainActivity;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.SharePrefUtil;
import com.xzwzz.orange.utils.StatusBarUtil;
import com.xzwzz.orange.view.RoundImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {

    private RoundImageView ivLogo;
    private android.widget.Button btnDologin;
    private android.widget.TextView tvFindPass;
    private android.widget.Button btnDoReg;
    private android.widget.EditText etUsername;
    private android.widget.EditText etPassword;
    private android.widget.LinearLayout mLlOtherlogin;
    private android.widget.ImageView mIvOtherLoginQq;
    private android.widget.ImageView mIvOtherLoginWechat;
    private String type;
    //    private String[] names = {QQ.NAME, Wechat.NAME};
    private PlatformDb mPlatDB;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        StatusBarUtil.getInstance().darkMode(this);
        ivLogo = findViewById(R.id.iv_logo);
        btnDologin = findViewById(R.id.btn_dologin);
        tvFindPass = findViewById(R.id.tv_findPass);
        btnDoReg = findViewById(R.id.btn_doReg);
        ivLogo.setImageBitmap(ImageUtils.toRoundCorner(BitmapFactory.decodeResource(getResources(), R.mipmap.logo), 5));
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        mLlOtherlogin = findViewById(R.id.ll_otherlogin);
        mIvOtherLoginQq = findViewById(R.id.iv_other_login_qq);
        mIvOtherLoginWechat = findViewById(R.id.iv_other_login_wechat);
        if (!AppConfig.openOtherLogin) {
            mLlOtherlogin.setVisibility(View.GONE);
        }

        findViewById(R.id.img_close).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getConfig();
    }

    @Override
    protected void setListener() {
        tvFindPass.setOnClickListener(this);
        btnDologin.setOnClickListener(this);
        btnDoReg.setOnClickListener(this);
        //微信登录
        mIvOtherLoginQq.setOnClickListener(v -> {
            ToastUtils.showShort("正在登录...");
            type = "wx";
//            otherLogin(names[1]);
        });
        //QQ登录
        mIvOtherLoginWechat.setOnClickListener(v -> {
            ToastUtils.showShort("正在登录...");
            type = "QqBean";
//            otherLogin(names[0]);
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dologin:
                login();
                break;
            case R.id.btn_doReg:
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.tv_findPass:
                ActivityUtils.startActivity(FindPassActivity.class);
                break;
            case R.id.img_close:
                finish();
                break;
            default:
                break;
        }
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        try {
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (username.length() != 11) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastUtils.showShort("请输入6-16位密码");
            return;
        }
        initCallBack();
        RetrofitClient.getInstance().createApi().login("Login.userLogin", username, password).compose(RxUtils.io_main())
                .subscribe(callback);
    }

    private void NextActivity() {
        ActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    private void otherLogin(String name) {
        ToastUtils.showShort("正在授权登录...");
        Platform other = ShareSDK.getPlatform(name);
        other.showUser(null);//执行登录，登录后在回调里面获取用户资料
        other.SSOSetting(false);  //设置false表示使用SSO授权方式
        other.setPlatformActionListener(this);
        other.removeAccount(true);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(() -> {


        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        runOnUiThread(() -> {
            if (i == 8) {
                ToastUtils.showShort("未安装微信客户端");
            } else {
                ToastUtils.showShort("授权登录失败");
            }
        });
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showShort("授权已取消");
    }

    private BaseListObserver<UserBean> callback;

    private void initCallBack() {
        if (callback == null) {
            callback = new BaseListObserver<UserBean>(ProgressDialog.show(this, "", "登录中...")) {
                @Override
                protected void onHandleSuccess(List<UserBean> list) {
                    if (list != null & list.size() > 0) {
                        AppContext.getInstance().saveUserInfo(list.get(0));
                        
                        if (LoginUtils.isWifiProxy(LoginActivity.this)) {
                            ToastUtils.showShort("登录失败");
                        } else {
                            getQq();

                        }
                    }
                }
            };
        }
    }


    private void getQq() {
        RetrofitClient.getInstance().createApi().getQq("User.getqq", AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(qqBean -> {
                    if (qqBean.getData().getCode() == 0) {
                        AppConfig.QQ = qqBean.getData().getInfo().get(0).getQq();
                        AppConfig.YUE = qqBean.getData().getInfo().get(0).getYueka_url();
                        AppConfig.JI = qqBean.getData().getInfo().get(0).getJika_url();
                        AppConfig.YEAR = qqBean.getData().getInfo().get(0).getNianka_url();
                        AppConfig.FOREVER = qqBean.getData().getInfo().get(0).getZhongshenka_url();
                        AppConfig.CODE = qqBean.getData().getInfo().get(0).getInvitation_code();

//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void getConfig() {
        RetrofitClient.getInstance().createApi().getConfig("Home.getConfig")
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<ConfigBean>() {
                    @Override
                    protected void onHandleSuccess(List<ConfigBean> list) {
                        if (list.size() > 0) {
                            ConfigBean bean = list.get(0);
                            AppConfig.TICK_NAME = bean.name_votes;
                            AppConfig.CURRENCY_NAME = bean.name_coin;
                            if (!bean.enter_tip_level.isEmpty()) {
                                AppConfig.JOIN_ROOM_ANIMATION_LEVEL = Integer.parseInt(bean.enter_tip_level);
                            } else {
                                AppConfig.JOIN_ROOM_ANIMATION_LEVEL = 0;
                            }
                            AppConfig.ROOM_CHARGE_SWITCH = Integer.parseInt(bean.live_cha_switch);
                            AppConfig.ROOM_PASSWORD_SWITCH = Integer.parseInt(bean.live_pri_switch);
                            AppConfig.ROOM_TIME_SWITCH = Integer.parseInt(bean.live_tim_switch);

                            AppContext.channel_type = bean.channl_type;

                            if (bean.channl_type == 1) {//主要采集器
                                AppContext.channelUrl = bean.channl_url;
                                AppContext.channelDataUrl = bean.zhubo_url;
                            } else {
                                AppContext.channelUrl = bean.channl_url2;
                                AppContext.channelDataUrl = bean.zhubo_url2;
                            }

                            AppContext.text = bean.keyword;

                            AppConfig.APP_ANDROID_SHARE = bean.app_android;

                            AppConfig.SHARE_TITLE = bean.share_title;
                            AppConfig.SHARE_DES = bean.share_des;

                            AppConfig.MAINTAIN_SWITCC = Integer.parseInt(bean.maintain_switch);
                            AppConfig.apk_ver = bean.apk_ver;
                            AppConfig.apk_url = bean.apk_url;
                            AppConfig.maintain_tips = bean.maintain_tips;
                            AppConfig.VIDEO_URL = bean.video_url;
                            SharePrefUtil.putString("video_url", bean.video_url);
                        }
                    }
                });
    }

}
