package com.xzwzz.orange.module.video;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xzwzz.orange.R;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.VideoListBean;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.MemberUtil;
import com.xzwzz.orange.widget.XzwzzPlayer;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends BaseActivity {

    private String type;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_video_play;
    }

    private MemberUtil.MemberListener mMemberListener = new MemberUtil.MemberListener() {
        @Override
        public void isMemeber() {
        }

        @Override
        public void noMember() {
            DialogHelp.showfreetimeOutDialog(VideoPlayActivity.this, getResources().getString(R.string.app_name) + "平台面向全国招收代理，独立的分销系统，   会员分享方式，让代理真真正正的躺在床上挣钱！开通会员免费观看所有直播！", (View.OnClickListener) v -> {
                startActivity(new Intent(VideoPlayActivity.this, VipActivity.class));
                finish();
            }).show();
        }
    };

    @Override
    protected void initView() {
        super.initView();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        VideoListBean bean = (VideoListBean) bundle.getSerializable("data");
        jzVideoPlayerStandard = findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(bean.video_url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, bean.title);
        jzVideoPlayerStandard.thumbImageView.setVisibility(View.GONE);
        jzVideoPlayerStandard.startVideo();
    }

    private XzwzzPlayer jzVideoPlayerStandard;

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(type) && type.equals("free")) return;
//        MemberUtil.delayCheckMember(new WeakReference<>(mMemberListener));
    }

    @Override
    public void onBackPressed() {
        if (jzVideoPlayerStandard.currentScreen == JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL) {
            super.onBackPressed();
            return;
        }
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
