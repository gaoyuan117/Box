package com.aaaa.falas.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aaaa.falas.R;
import com.aaaa.falas.base.BaseActivity;

import java.lang.invoke.MethodHandle;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.custom.IRenderView;

public class PlayTvActivity extends BaseActivity {

    private tv.danmaku.ijk.media.player.custom.IjkVideoView ijkPlayer;
    private android.widget.ImageView loadingView;
    private AnimationDrawable animationDrawable;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_play_tv;
    }

    @Override
    protected void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        ijkPlayer = findViewById(R.id.ijk_player);
        loadingView = findViewById(R.id.loading_View);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.icon_back);
        toolbar.setNavigationOnClickListener(v -> finish());
//        ((TextView)findViewById(R.id.toolbar_title)).setText(title);

        animationDrawable = (AnimationDrawable) loadingView.getBackground();
        animationDrawable.setOneShot(false);
        animationDrawable.start();

        ijkPlayer.setVideoPath(url);
        ijkPlayer.setAspectRatio(IRenderView.AR_MATCH_PARENT);
        ijkPlayer.setListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {

            }
        }, new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                animationDrawable.stop();
                loadingView.setVisibility(View.GONE);
            }
        }, new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }
        }, new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                return false;
            }
        });
        ijkPlayer.start();
        handler.sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(1);
                if (toolbar.getVisibility() == View.GONE) {
                    toolbar.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(1, 5000);
                } else if (toolbar.getVisibility() == View.VISIBLE) {
                    toolbar.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (toolbar.getVisibility() == View.VISIBLE) {
                toolbar.setVisibility(View.GONE);
            }
        }
    };


    boolean mBackPressed = false;

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
}
