package com.xzwzz.orange.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.DownLoadService;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.ConfigBean;

import java.io.File;
import java.util.List;

/**
 * 更新管理类
 */

public class UpdateManager {

    private Activity mContext;

    private boolean isShow = false;

    private String newVersion;
    private ProgressDialog _waitDialog;
    private String apkurl;
    private Dialog dialog;


    public UpdateManager(Activity context, String newVersion, String apkurl, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
        this.newVersion = newVersion;
        this.apkurl = apkurl;
    }

    public UpdateManager(Activity context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }

    //检测是否需要更新
    public void checkUpdate() {
//        if (isShow) {
//            showCheckDialog();
//        }
//        if (newVersion == null || apkurl == null) {
//            RetrofitClient.getInstance().createApi().getConfig("Home.getConfig")
//                    .compose(RxUtils.io_main())
//                    .subscribe(new BaseListObserver<ConfigBean>(ProgressDialog.show(mContext, "", "正在获取版本信息")) {
//                        @Override
//                        protected void onHandleSuccess(List<ConfigBean> list) {
//                            if (list.size() > 0) {
//                                ConfigBean bean = list.get(0);
//                                UpdateManager.this.newVersion = bean.apk_ver;
//                                UpdateManager.this.apkurl = bean.apk_url;
//                                checkUpdate();
//                            }
//                        }
//                    });
//            return;
//        }
//
//
//        hideCheckDialog();
        if (TextUtils.isEmpty(AppConfig.apk_ver) || TextUtils.isEmpty(AppConfig.apk_url)) return;

        UpdateManager.this.newVersion = AppConfig.apk_ver;
        UpdateManager.this.apkurl = AppConfig.apk_url;
        if (!String.valueOf(AppUtils.getAppVersionName()).equals(newVersion)) {
            showUpdateInfo(apkurl);
        } else {
            if (isShow) {
                ToastUtils.showShort("您的版本已是最新");
            }
        }
    }

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog(mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    //弹窗提示
    private void showUpdateInfo(final String apiUrl) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("发现新版本!");
//        builder.setNegativeButton("取消", (dialogInterface, i) -> {
//
//        });
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            upDataApp(apiUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(apiUrl));
            mContext.startActivity(intent);
            dialog.dismiss();
            mContext.finish();

        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    //下载app
    private void upDataApp(String apiUrl) {
        if (!".apk".equals(apiUrl.substring(apiUrl.length() - 4, apiUrl.length()))) {
//            ToastUtils.showShort("安装包不存在");
            return;
        }
        //判断是否存在
        File file = new File(AppConfig.DEFAULT_SAVE_FILE_PATH + "app.apk");
        if (file.exists()) {
            file.delete();
        }
        Intent intent = new Intent(mContext, DownLoadService.class);
        intent.putExtra("apkurl", apiUrl);
        mContext.startService(intent);
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }
}
