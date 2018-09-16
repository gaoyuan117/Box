package com.xzwzz.orange.ui.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.BaseBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ModifyPwdActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.EditText mEtOldpassword;
    private android.widget.EditText mEtNewpassword;
    private android.widget.EditText mEtConfirmpassword;
    private android.widget.Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("修改密码");
        setListener();
    }

    protected void initView() {
        mEtOldpassword = findViewById(R.id.et_oldpassword);
        mEtNewpassword = findViewById(R.id.et_newpassword);
        mEtConfirmpassword = findViewById(R.id.et_confirmpassword);
        mBtnConfirm = findViewById(R.id.btn_confirm);
    }

    protected void setListener() {
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String oldPassword = mEtOldpassword.getText().toString();
        String newpassword = mEtNewpassword.getText().toString();
        String confirmPassword = mEtConfirmpassword.getText().toString();

        if (oldPassword.length() < 6) {
            ToastUtils.showShort("请输入6-16位老密码");
            return;
        }
        if (newpassword.length() < 6) {
            ToastUtils.showShort("请输入6-16位新密码");
            return;
        }
        if (!confirmPassword.equals(newpassword)) {
            ToastUtils.showShort("请再次输入新密码，确保一致");
            return;
        }

        try {
            RetrofitClient.getInstance().createApi().ModifyPassWord("User.updatePass", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid()
                    , URLEncoder.encode(oldPassword, "UTF-8"), URLEncoder.encode(newpassword, "UTF-8"), URLEncoder.encode(confirmPassword, "UTF-8"))
                    .compose(RxUtils.io_main())
                    .subscribe(new BaseListObserver<BaseBean>(ProgressDialog.show(this, "", "修改密码中...")) {
                        @Override
                        protected void onHandleSuccess(List<BaseBean> list) {
                            if (list.size() > 0) {
                                ToastUtils.showShort("修改密码成功");
                                finish();
                            }
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
