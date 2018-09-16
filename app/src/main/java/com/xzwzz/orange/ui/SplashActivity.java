package com.xzwzz.orange.ui;


import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.ConfigBean;
import com.xzwzz.orange.bean.NovelTermBean;
import com.xzwzz.orange.bean.QqBean;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.SharePrefUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideApp.with(this).load(R.mipmap.splash)
                .into(imageView);
        return imageView;
    }

    @Override
    protected void initData() {
        splah();
        getNovelTerm();
    }

    //获取小说分类
    private void getNovelTerm() {

        RetrofitClient.getInstance().createApi().novelTerm("Home.VideoTerm").compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<NovelTermBean>() {
                    @Override
                    protected void onHandleSuccess(List<NovelTermBean> list) {
                        if (list == null || list.size() == 0) return;
                        AppContext.novelTermList.clear();
                        AppContext.novelTermList.addAll(list);
                    }
                });
    }

    private void splah() {
        getQq();
        getConfig();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                ActivityUtils.startActivity(HomeActivity.class);
                finish();
            });
        }).start();
    }

    private void getQq() {
        if (AppContext.getInstance().getLoginUid() == null || AppContext.getInstance().getLoginUid().equals("0"))
            return;
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
                        }
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
                            if (!TextUtils.isEmpty(AppContext.channelUrl)) {
                                SharePrefUtil.putString("url", AppContext.channelUrl);
                                SharePrefUtil.putString("dataurl", AppContext.channelDataUrl);
                            }

                            SharePrefUtil.putInt("type", bean.channl_type);


                            AppContext.text = bean.keyword;

                            AppConfig.APP_ANDROID_SHARE = bean.app_android;

                            AppConfig.SHARE_TITLE = bean.share_title;
                            AppConfig.SHARE_DES = bean.share_des;

                            AppConfig.MAINTAIN_SWITCC = Integer.parseInt(bean.maintain_switch);
                            AppConfig.apk_ver = bean.apk_ver;
                            AppConfig.apk_url = bean.apk_url;
                            AppConfig.maintain_tips = bean.maintain_tips;
                            AppConfig.VIDEO_URL = bean.video_url;
                            AppConfig.VIDEO_URL = bean.video_vip_url;

                            SharePrefUtil.putString("video_url", bean.video_url);

                        }
                    }
                });
    }


}
