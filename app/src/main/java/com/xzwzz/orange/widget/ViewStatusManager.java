package com.xzwzz.orange.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xzwzz.orange.R;

//
//                          _oo0oo_
//                         o8888888o
//                          88" . "88
//                          (| -_- |)
//                          0\  =  /0
//                      ___/`---'\___
//                      .' \\|     |// '.
//                   / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//                  |   | \\\  -  /// |   |
//                  | \_|  ''\---/''  |_/ |
//                  \  .-\__  '-'  ___/-. /
//               ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//          \  \ `_.   \_ __\ /__ _/   .-` /  /
//=====`-.____`.___ \_____/___.-`___.-'=====
//                           `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
public class ViewStatusManager extends FrameLayout {
    private ValueAnimator mValueAnimator;
    private View loadingView, errorView, emptyView, contentView;
    private StatusChangeListener mStatusChangeListener;

    public ViewStatusManager(@NonNull Context context) {
        this(context, null);
    }

    public ViewStatusManager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewStatusManager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setStatusChangeListener(StatusChangeListener statusChangeListener) {
        mStatusChangeListener = statusChangeListener;
    }

    private void init() {
        mValueAnimator = ValueAnimator.ofFloat(1f, 0f);
        mValueAnimator.setDuration(400);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            setAlpha(value);
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initChildView();
    }

    private void initChildView() {
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("the ViewStatusManager can't have many childs,only one");
        }
        contentView = getChildAt(0);
        errorView = drawErrorView();
        loadingView = drawLoadingView();
        emptyView = drawEmptyView();
        addView(errorView);
        addView(emptyView);
        addView(loadingView);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }

    private View drawEmptyView() {
        LinearLayout empty = new LinearLayout(getContext());
        empty.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        empty.setGravity(Gravity.CENTER);

        ImageView imgview = new ImageView(getContext());
        imgview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        imgview.setImageResource(R.mipmap.icon_empty);

        TextView text = new TextView(getContext());
        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        text.setText("暂无数据...");

        empty.addView(imgview);
        empty.addView(text);
        empty.setLayoutParams(params);

        empty.setOnClickListener(v -> {
            if (mStatusChangeListener != null) {
                mStatusChangeListener.ReLoad();
            }
        });
        return empty;
    }

    private View drawLoadingView() {
        ProgressBar mProgressBar = new ProgressBar(getContext());
        mProgressBar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        return mProgressBar;
    }

    private View drawErrorView() {
        TextView error = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        error.setGravity(Gravity.CENTER);
        error.setText("加载错误...");
        error.setTextSize(32.0f);
        error.setLayoutParams(params);
        error.setOnClickListener(v -> {
            if (mStatusChangeListener != null) {
                mStatusChangeListener.ReLoad();
            }
        });
        return error;
    }

    public void setStatus(ViewStatus status) {
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeStatus(status);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();
    }

    private void changeStatus(ViewStatus status) {
        setAlpha(1f);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        switch (status) {
            case error:
                errorView.setVisibility(VISIBLE);
                break;
            case empty:
                emptyView.setVisibility(VISIBLE);
                break;
            case success:
                contentView.setVisibility(VISIBLE);
                break;
            case loading:
                loadingView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    public enum ViewStatus {
        error,
        empty,
        success,
        loading,
    }

    public interface StatusChangeListener {
        public void ReLoad();
    }
}
