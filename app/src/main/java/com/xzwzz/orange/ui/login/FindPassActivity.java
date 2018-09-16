package com.xzwzz.orange.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.utils.SimpleUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FindPassActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.EditText mEtPhonenumber;
    private android.widget.EditText mEtVerificacode;
    private android.widget.TextView mBtnPhoneLoginSendCode;
    private android.widget.EditText mEtPassword;
    private android.widget.EditText mEtPasswordagain;
    private android.widget.Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        initView();
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("找回密码");
        setListener();
    }


    protected void initView() {
        mEtPhonenumber = findViewById(R.id.et_phonenumber);
        mEtVerificacode = findViewById(R.id.et_verificacode);
        mBtnPhoneLoginSendCode = findViewById(R.id.btn_phone_login_send_code);
        mEtPassword = findViewById(R.id.et_password);
        mEtPasswordagain = findViewById(R.id.et_passwordagain);
        mBtnConfirm = findViewById(R.id.btn_confirm);
    }

    protected void setListener() {
        mBtnPhoneLoginSendCode.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_login_send_code:
                getVerficaCode();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        String phone = mEtPhonenumber.getText().toString();
        String pwd = mEtPassword.getText().toString();
        String pwdAgain = mEtPasswordagain.getText().toString();
        String verficaCode = mEtVerificacode.getText().toString();
        if (phone.length() != 11) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            ToastUtils.showShort("请输入6-16位密码");
            return;
        }
        if (TextUtils.isEmpty(verficaCode)) {
            ToastUtils.showShort("请输入验证码");
            return;
        }

        RetrofitClient.getInstance().createApi().FindPassWord("Login.userFindPass", phone, pwd, pwdAgain, verficaCode)
                .compose(RxUtils.io_main()).subscribe(new BaseListObserver<BaseBean>() {
            @Override
            protected void onHandleSuccess(List<BaseBean> list) {
                ToastUtils.showShort("找回密码成功");
                finish();
            }
        });
    }

    private void getVerficaCode() {
        String phone = mEtPhonenumber.getText().toString();
        if (phone.length() != 11) {
            ToastUtils.showShort("请输入正确的手机号");
            return;
        }
        RetrofitClient.getInstance().createApi().getCode("Login.getForgetCode", phone).compose(RxUtils.io_main())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean.data.info != null) {
                            SimpleUtils.startTimer(new WeakReference<>(mBtnPhoneLoginSendCode), "发送验证码", 60, 1);
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
}
