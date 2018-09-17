package com.aaaa.falas.ui;

import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.aaaa.falas.R;
import com.aaaa.falas.base.BaseActivity;
import com.aaaa.falas.ui.login.ModifyPwdActivity;
import com.aaaa.falas.utils.DialogHelp;
import com.aaaa.falas.utils.LoginUtils;
import com.aaaa.falas.utils.UpdateManager;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private com.aaaa.falas.widget.LineControlView mResetSetting;
    private com.aaaa.falas.widget.LineControlView mChecklastetSetting;
    private android.widget.Button mBtnExitLogin;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mResetSetting = findViewById(R.id.reset_setting);
        mChecklastetSetting = findViewById(R.id.checklastet_setting);
        mBtnExitLogin = findViewById(R.id.btn_exitLogin);
        setToolbar("设置", true);
    }

    @Override
    protected void setListener() {
        mResetSetting.setOnClickListener(this);
        mChecklastetSetting.setOnClickListener(this);
        mBtnExitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_setting:
                ActivityUtils.startActivity(ModifyPwdActivity.class);
                break;
            case R.id.checklastet_setting:
                checkOut();
                break;
            case R.id.btn_exitLogin:
                DialogHelp.getConfirmDialog(this, "确定要退出登录吗？", (dialog, which) -> LoginUtils.ExitLoginStatus()).show();
                break;
            default:
                break;
        }
    }

    private void checkOut() {
        new UpdateManager(this, true).checkUpdate();
    }
}
