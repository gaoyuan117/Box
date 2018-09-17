package com.aaaa.falas.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.aaaa.falas.AppConfig;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.R;
import com.aaaa.falas.api.http.BaseListObserver;
import com.aaaa.falas.api.http.HttpResult;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.base.BaseFragment;
import com.aaaa.falas.bean.InvitationBean;
import com.aaaa.falas.bean.QrCodeBean;
import com.aaaa.falas.bean.UserBean;
import com.aaaa.falas.bean.UserInfoBean;
import com.aaaa.falas.glide.GlideApp;
import com.aaaa.falas.glide.GlideCircleTransform;
import com.aaaa.falas.ui.ShareActivity;
import com.aaaa.falas.ui.UserInfoDetailActivity;
import com.aaaa.falas.ui.UserProfitActivity;
import com.aaaa.falas.ui.SettingActivity;
import com.aaaa.falas.ui.VipActivity;
import com.aaaa.falas.ui.WebViewActivity;
import com.aaaa.falas.ui.login.LoginActivity;
import com.aaaa.falas.ui.login.ModifyPwdActivity;
import com.aaaa.falas.utils.DialogHelp;
import com.aaaa.falas.utils.LoginUtils;
import com.aaaa.falas.utils.ShareUtil;
import com.aaaa.falas.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MineFragment extends BaseFragment implements View.OnClickListener, PlatformActionListener {
    private android.widget.LinearLayout mLlInformation;
    private android.widget.TextView mTvName;
    private android.widget.TextView mTvId;
    private android.widget.TextView mTvInvitate;
    private android.widget.TextView tvVipTime;
    private com.aaaa.falas.widget.LineControlView mQrcodeMine;
    private com.aaaa.falas.widget.LineControlView mWalletMine;
    private com.aaaa.falas.widget.LineControlView mVipMine;
    private com.aaaa.falas.widget.LineControlView mGotmonyMine;
    private com.aaaa.falas.widget.LineControlView mUimoduleItemMainListTv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view) {
        mLlInformation = view.findViewById(R.id.ll_information);
        mTvName = view.findViewById(R.id.tv_name);
        tvVipTime = view.findViewById(R.id.tv_vip_time);
        mTvId = view.findViewById(R.id.tv_id);
        mTvInvitate = view.findViewById(R.id.tv_invitate);
        mQrcodeMine = view.findViewById(R.id.qrcode_mine);
        mWalletMine = view.findViewById(R.id.wallet_mine);
        view.findViewById(R.id.kefu).setOnClickListener(this);
        view.findViewById(R.id.tuijian_mine).setOnClickListener(this);
        view.findViewById(R.id.change_pwd).setOnClickListener(this);
        mVipMine = view.findViewById(R.id.vip_mine);
        mGotmonyMine = view.findViewById(R.id.gotmony_mine);
        mUimoduleItemMainListTv = view.findViewById(R.id.uimodule_item_main_list_tv);

//        StatusBarUtil.getInstance().setPaddingSmart(getActivity(), mLlInformation);
        view.findViewById(R.id.btn_exitLogin).setOnClickListener(this);
    }

    @Override
    public void initData() {
        UserBean loginUser = AppContext.getInstance().getLoginUser();


        mTvId.setText("ID:" + loginUser.id);
        mTvName.setText("昵称:" + loginUser.user_nicename);
        flushUserInfo();
    }

    @Override
    protected void setListener() {
        mQrcodeMine.setOnClickListener(this);
        mWalletMine.setOnClickListener(this);
        mVipMine.setOnClickListener(this);
        mGotmonyMine.setOnClickListener(this);
        mUimoduleItemMainListTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_mine://二维码
                showQrCode();
                break;
            case R.id.wallet_mine://钱包
//                ActivityUtils.startActivity(UserDiamondsActivity.class);
//                ActivityUtils.startActivity(WebViewActivity.class);
                ShareUtil.share(getActivity(), this);

                break;
            case R.id.kefu:

                if (isQQClientAvailable(getActivity())) {
                    // 跳转到客服的QQ
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + AppConfig.QQ;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
                    if (isValidIntent(getActivity(), intent)) {
                        startActivity(intent);
                    }
                }

                break;
            case R.id.vip_mine://会员
                ActivityUtils.startActivity(VipActivity.class);
                break;
            case R.id.gotmony_mine://收益
                ActivityUtils.startActivity(UserProfitActivity.class);
                break;
            case R.id.uimodule_item_main_list_tv://设置
                ActivityUtils.startActivity(SettingActivity.class);
                break;
            case R.id.iv_avatar:
                if (AppConfig.openModifyInformation) {
                    ActivityUtils.startActivity(UserInfoDetailActivity.class);
                }
                break;

            case R.id.tuijian_mine://推荐好友
                ActivityUtils.startActivity(ShareActivity.class);
                break;
            case R.id.change_pwd://修改密码
                ActivityUtils.startActivity(ModifyPwdActivity.class);
                break;
            case R.id.btn_exitLogin:

                DialogHelp.getConfirmDialog(getActivity(), "确定要退出登录吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppContext.getInstance().cleanLoginInfo();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }).show();


                break;
            default:
                break;
        }
    }

    private void showQrCode() {
        RetrofitClient.getInstance().createApi().getMyQrcode(AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new Observer<QrCodeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QrCodeBean qrCodeBean) {
                        if (qrCodeBean != null && qrCodeBean.data != null && qrCodeBean.data.qrcode != null) {
                            DialogHelp.showQrCodeDialog(getActivity(), AppConfig.MAIN_URL + qrCodeBean.data.qrcode);
                        } else {
                            ToastUtils.showShort("获取二维码出错");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        flushUserInfo();
    }

    @Override
    protected void onUserVisible() {
        flushUserInfo();
    }

    private void flushUserInfo() {
        String loginUid = AppContext.getInstance().getLoginUid();
        if (loginUid.equals("0")) return;
        RetrofitClient.getInstance().createApi().getBaseUserInfo("User.getBaseInfo", AppContext.getInstance().getToken(), AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<UserInfoBean>() {
                    @Override
                    protected void onHandleSuccess(List<UserInfoBean> list) {
                        if (list.size() > 0) {
                            UserInfoBean bean = list.get(0);
                            AppConfig.IS_MEMBER = (bean.is_member == 1);
//                            if (AppConfig.IS_MEMBER) {
//                                mVipMine.setContent("到期时间" + bean.member_validity);
//                            } else {
//                                mVipMine.setContent("");
//                            }

                            tvVipTime.setText(bean.member_validity);

                            if (bean.user_nicename.isEmpty()) {
                                mTvName.setText("昵称:" + "未知");
                            } else {
                                mTvName.setText("昵称:" + bean.user_nicename);
                            }

                            mTvId.setText("ID:" + bean.id);
                        }
                    }
                });
//        RetrofitClient.getInstance().createApi().getInvitationInfo("User.inv_friends", AppContext.getInstance().getLoginUid())
//                .compose(RxUtils.io_main())
//                .subscribe(new BaseListObserver<InvitationBean>() {
//                    @Override
//                    protected void onHandleSuccess(List<InvitationBean> list) {
//                        if (list.size() > 0) {
//                            InvitationBean bean = list.get(0);
//                            mTvInvitate.setText("邀请人数:" + bean.total_nums);
//                        }
//                    }
//                });
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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showLong("分享成功");
        shareComplete();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showLong("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showLong("分享取消");
    }

    @SuppressLint("CheckResult")
    private void shareComplete() {
        RetrofitClient.getInstance().createApi().share("User.sharenum", AppContext.getInstance().getLoginUid())
                .compose(RxUtils.io_main())
                .subscribe(httpResult -> flushUserInfo());
    }
}
