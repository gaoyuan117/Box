package com.xzwzz.orange.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.bean.QqBean;
import com.xzwzz.orange.bean.UserBean;
import com.xzwzz.orange.ui.HomeActivity;
import com.xzwzz.orange.ui.MainActivity;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.SimpleUtils;
import com.xzwzz.orange.utils.StatusBarUtil;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private android.widget.EditText etPhonenumber;
    private android.widget.EditText etVerificacode;
    private android.widget.TextView btnPhoneLoginSendCode;
    private android.widget.EditText etPassword;
    private android.widget.EditText etPasswordagain;
    private android.widget.EditText etRecommend;
    private android.widget.Button btnDologin;
    private String yzm;

    EditText etYzm;
    TextView tvYzm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("注册");
        setListener();
    }


    protected void initView() {

        etYzm = findViewById(R.id.et_yzm);
        tvYzm = findViewById(R.id.tv_yzm);
        tvYzm.setOnClickListener(this);
        yzm = LoginUtils.getNum();
        tvYzm.setText(yzm);

        etPhonenumber = findViewById(R.id.et_phonenumber);
        etVerificacode = findViewById(R.id.et_verificacode);
        btnPhoneLoginSendCode = findViewById(R.id.btn_phone_login_send_code);
        etPassword = findViewById(R.id.et_password);
        etPasswordagain = findViewById(R.id.et_passwordagain);
        etRecommend = findViewById(R.id.et_recommend);
        btnDologin = findViewById(R.id.btn_doReg);

    }

    protected void setListener() {
        btnDologin.setOnClickListener(this);
        btnPhoneLoginSendCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_login_send_code:
                getVerficaCode();
                break;
            case R.id.btn_doReg:
                register();
                break;
            case R.id.tv_yzm:
                yzm = LoginUtils.getNum();
                tvYzm.setText(yzm);
                break;
            default:
                break;
        }
    }

    private void getVerficaCode() {
        String phone = etPhonenumber.getText().toString();
        if (phone.length() != 11) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        RetrofitClient.getInstance().createApi().getCode("Login.getCode", phone).compose(RxUtils.io_main())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean.data.info != null) {
                            SimpleUtils.startTimer(new WeakReference<>(btnPhoneLoginSendCode), "发送验证码", 60, 1);
                            ToastUtils.showShort("短信验证码已发送，请查收");
                        } else {
                            ToastUtils.showShort(baseBean.msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("网络错误，请稍后再试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void register() {
        String phone = etPhonenumber.getText().toString();
        String pwd = etPassword.getText().toString();
//        String pwdAgain = etPasswordagain.getText().toString();
        try {
            pwd = URLEncoder.encode(pwd, "UTF-8");
//            pwdAgain = URLEncoder.encode(pwdAgain, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String verficaCode = etVerificacode.getText().toString();
        String recommend = etRecommend.getText().toString();
        if (phone.length() != 11) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
//        if (verficaCode.isEmpty()) {
//            ToastUtils.showShort("请输入验证码");
//            return;
//        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtils.showShort("请输入6-16位密码");
            return;
        }
//        if (!pwd.equals(pwdAgain)) {
//            ToastUtils.showShort("两次输入的密码不一样");
//            return;
//        }
        if (recommend.isEmpty()) {
            ToastUtils.showShort("请输入邀请人ID");
            return;
        }

        if (!etYzm.getText().toString().equals(yzm.replaceAll(" ", ""))) {
            ToastUtils.showShort("验证码错误");
            return;
        }

        RetrofitClient.getInstance().createApi().register("Login.userReg", phone, pwd, verficaCode, recommend).compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<UserBean>(ProgressDialog.show(this, "", "注册中...")) {
                    @Override
                    protected void onHandleSuccess(List<UserBean> list) {
                        if (list.size() > 0) {
                            AppContext.getInstance().saveUserInfo(list.get(0));
                            getQq();
                        }
                    }
                });
    }


    private void getQq() {
        RetrofitClient.getInstance().createApi().getQq("User.getqq", AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new Consumer<QqBean>() {
                    @Override
                    public void accept(QqBean qqBean) throws Exception {
                        if (qqBean.getData().getCode() == 0) {
                            AppConfig.QQ = qqBean.getData().getInfo().get(0).getQq();
                            AppConfig.YUE = qqBean.getData().getInfo().get(0).getYueka_url();
                            AppConfig.JI = qqBean.getData().getInfo().get(0).getJika_url();
                            AppConfig.YEAR = qqBean.getData().getInfo().get(0).getNianka_url();
                            AppConfig.FOREVER = qqBean.getData().getInfo().get(0).getZhongshenka_url();
                            AppConfig.CODE = qqBean.getData().getInfo().get(0).getInvitation_code();

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
