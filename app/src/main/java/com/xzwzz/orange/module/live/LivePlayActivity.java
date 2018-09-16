package com.xzwzz.orange.module.live;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.ChannelDataBean;
import com.xzwzz.orange.bean.ChatBean;
import com.xzwzz.orange.bean.CollectBean;
import com.xzwzz.orange.bean.CollectBeanDao;
import com.xzwzz.orange.bean.PlatformBean1;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.ui.adapter.ChatAdapter;
import com.xzwzz.orange.utils.DialogHelp;
import com.xzwzz.orange.utils.MemberUtil;
import com.xzwzz.orange.utils.SharePrefUtil;
import com.xzwzz.orange.utils.SpannableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.custom.IRenderView;
import tv.danmaku.ijk.media.player.custom.IjkVideoView;

public class LivePlayActivity extends BaseActivity implements View.OnClickListener {
    private IjkVideoView ijkPlayer;
    boolean mBackPressed = false;
    private android.widget.TextView mTvLiveName;
    private android.widget.TextView mTvLiveNum;
    private android.widget.ImageView mIjkJubao;
    private android.widget.ImageView mIjkGuanbi;
    private android.widget.ImageView mIjkPingbi;
    private android.widget.ImageView mIvAvatar;
    private AnimationDrawable animationDrawable;
    private android.widget.ImageView loadingView;
    private android.widget.TextView tvTips;
    private LinearLayout layout;
    private ChannelDataBean.DataBean bean;
    private CollectBeanDao collectBeanDao;
    private RecyclerView recycler;
    private ChatAdapter adapter;
    private List<String> list = new ArrayList<>();
    private int[] s = new int[9];
    private int num = 1;
    private Disposable disposable;
    private EditText editText;
    private TextView tvSend;
    private InputMethodManager imm;


    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_live_play;
    }

    @Override
    protected void initView() {
        collectBeanDao = AppContext.getDaoInstant().getCollectBeanDao();

        mTvLiveName = findViewById(R.id.tv_live_name);
        mTvLiveNum = findViewById(R.id.tv_live_num);
        mIjkJubao = findViewById(R.id.ijk_jubao);
        mIjkGuanbi = findViewById(R.id.ijk_guanbi);
        mIjkPingbi = findViewById(R.id.ijk_pingbi);
        mIvAvatar = findViewById(R.id.iv_avatar);
        loadingView = findViewById(R.id.loading_View);
        layout = findViewById(R.id.layout);
        tvTips = findViewById(R.id.tv_tips);
        tvSend = findViewById(R.id.tv_send);
        editText = findViewById(R.id.et_input);
        findViewById(R.id.img_collect).setOnClickListener(this);

        list.add("系统消息:本平台已接入聊天室实时信息流,欢迎收看");

        s[0] = 1;
        s[1] = 2;
        s[2] = 2;
        s[3] = 2;
        s[4] = 3;
        s[5] = 3;
        s[6] = 4;
        s[7] = 4;
        s[8] = 5;

        adapter = new ChatAdapter(R.layout.item_chat, list);
        num = (int) ((Math.random() * 10));

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        timer(num);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();

                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showShort("请输入内容");
                    return;
                }
                String user_nicename = AppContext.getInstance().getLoginUser().user_nicename;

                if (TextUtils.isEmpty(user_nicename)) {
                    user_nicename = "未知";
                }

                list.add(user_nicename + "：" + s);
                recycler.smoothScrollToPosition(list.size());
                adapter.notifyDataSetChanged();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                editText.setText("");
            }
        });
    }


    private void timer(int num) {
        Observable.timer(num, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        getList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }


    @Override
    protected void initData() {
        ijkPlayer = findViewById(R.id.ijk_player);
        Bundle extras = getIntent().getExtras();
        bean = (ChannelDataBean.DataBean) extras.getSerializable("data");

        if (!(bean != null && bean.getUrl() != null && !bean.getUrl().isEmpty())) {
            ToastUtils.showShort("暂无法获取主播信息，请稍后重试");
            finish();
            return;
        }
        mTvLiveName.setText(bean.getName());
        mTvLiveNum.setText(bean.getNum());
        GlideApp.with(this)
                .load(bean.getBigpic())
                .transform(new GlideCircleTransform(mContext))
                .into(mIvAvatar);

        ijkPlayer.setVideoPath(bean.getUrl());
        ijkPlayer.setAspectRatio(IRenderView.AR_MATCH_PARENT);
        animationDrawable = (AnimationDrawable) loadingView.getBackground();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
        ijkPlayer.setListener(mp -> {

        }, mp -> {
            // 把动画资源设置为imageView的背景,也可直接在XML里面设置
            animationDrawable.stop();
            loadingView.setVisibility(View.GONE);
        }, (mp, what, extra) -> false, (mp, what, extra) -> false);
        ijkPlayer.start();
    }

    @Override
    protected void setListener() {
        mIjkGuanbi.setOnClickListener(this);
        mIjkJubao.setOnClickListener(this);
        mIjkPingbi.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ijkPlayer.stopPlayback();
        ijkPlayer.release(true);
        ijkPlayer.destroyDrawingCache();
        ijkPlayer.stopBackgroundPlay();
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ijkPlayer.pause();
        if (mBackPressed || !ijkPlayer.isBackgroundPlayEnabled()) {
            ijkPlayer.stopPlayback();
            ijkPlayer.release(true);
            ijkPlayer.destroyDrawingCache();
            ijkPlayer.stopBackgroundPlay();
        } else {
            ijkPlayer.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ijk_jubao:
                DialogHelp.getConfirmDialog(this, "是否举报该主播?", (dialog, which) -> finish()).show();
                break;
            case R.id.ijk_guanbi:
                onBackPressed();
                break;
            case R.id.img_collect://搜藏
                CollectBean collectBean = new CollectBean();
                collectBean.setImg(bean.getBigpic());
                collectBean.setName(bean.getName());
                collectBean.setNum(bean.getNum());
                collectBean.setUrl(bean.getUrl());
                collectBeanDao.insertOrReplace(collectBean);
                ToastUtils.showShort("收藏成功");

                break;
            case R.id.ijk_pingbi:
                if (mIjkJubao.getVisibility() == View.VISIBLE || mIjkGuanbi.getVisibility() == View.VISIBLE) {
                    mIjkJubao.setVisibility(View.GONE);
                    mIjkGuanbi.setVisibility(View.GONE);
                    tvTips.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);

                } else if (mIjkJubao.getVisibility() == View.GONE || mIjkGuanbi.getVisibility() == View.GONE) {
                    mIjkJubao.setVisibility(View.VISIBLE);
                    mIjkGuanbi.setVisibility(View.VISIBLE);
                    tvTips.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void getList() {
        RetrofitClient.getInstance().createApi().getChat("User.getbar")
                .compose(RxUtils.io_main())
                .subscribe(new Observer<ChatBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ChatBean bean) {
                        if (bean.getRet() == 200) {
                            if (bean.getData().getCode() == 0) {
                                if (bean.getData().getInfo().get(0) == null || bean.getData().getInfo().get(0).size() == 0)
                                    return;
                                list.add(bean.getData().getInfo().get(0).get(0).getContent());
                                recycler.smoothScrollToPosition(list.size());
                                adapter.notifyDataSetChanged();
                                num = (int) ((Math.random() * 10));
                                timer(num);

                            }
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


}
