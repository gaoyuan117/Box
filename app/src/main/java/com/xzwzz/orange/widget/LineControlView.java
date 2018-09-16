package com.xzwzz.orange.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.xzwzz.orange.R;

public class LineControlView extends LinearLayout {
    private String title, content;
    private boolean canNav, openSwitch;
    private int maxLength, IconRes;
    private boolean hasContentIcon;
    private TextView titleView, contentView;
    private ImageView IconView, navigationView, contentIcon;
    private Switch mSwitch;

    public LineControlView(Context context) {
        this(context, null);
    }

    public LineControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineView, 0, 0);
        try {
            title = ta.getString(R.styleable.LineView_title);
            content = ta.getString(R.styleable.LineView_content);
            canNav = ta.getBoolean(R.styleable.LineView_canNavition, false);
            openSwitch = ta.getBoolean(R.styleable.LineView_openSwitch, false);
            maxLength = ta.getInteger(R.styleable.LineView_maxLength, -1);
            IconRes = ta.getResourceId(R.styleable.LineView_iconRes, -1);
            hasContentIcon = ta.getBoolean(R.styleable.LineView_hasContentIcon, false);
            initView();
        } finally {
            ta.recycle();
        }

    }

    private void initView() {
        titleView = new TextView(getContext());
        contentView = new TextView(getContext());
        IconView = new ImageView(getContext());
        navigationView = new ImageView(getContext());
        mSwitch = new Switch(getContext());
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(dp2px(10f), 0, dp2px(10f), 0);

        titleView.setTextColor(Color.parseColor("#333333"));
        titleView.setTextSize(px2sp(dp2px(16)));

        contentView.setGravity(Gravity.RIGHT);

        setTitle(title);
        setContent(content);
        if (maxLength == -1) {
            contentView.setMaxWidth(16);
        } else {
            contentView.setMaxWidth(maxLength);
        }
        navigationView.setImageResource(R.mipmap.arrow_right);

        LayoutParams imgParams = new LayoutParams(dp2px(18f), dp2px(18f));
        imgParams.rightMargin = dp2px(10f);
        LayoutParams titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams contentParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.weight = 1;
        contentParams.rightMargin = dp2px(10f);
        LayoutParams navigationParams = new LayoutParams(dp2px(18f), dp2px(18f));

        if (IconRes != -1) {
            setIconRes(IconRes);
            addView(IconView, imgParams);
        }
        addView(titleView, titleParams);
        addView(contentView, contentParams);
        if (hasContentIcon) {
            contentIcon = new ImageView(getContext());
            addView(contentIcon, new ViewGroup.LayoutParams(SizeUtils.dp2px(48f), ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (openSwitch) {
            addView(mSwitch, titleParams);
        }
        if (canNav) {
            addView(navigationView, navigationParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("LineControlView", "heightMode:" + heightMode + "     " + MeasureSpec.UNSPECIFIED);
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = dp2px(48f);
        }
        Log.d("LineControlView", "heightSize:" + heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    public String getTitle() {
        return titleView.getText().toString();
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(this.title);
    }

    public String getContent() {
        return contentView.getText().toString();
    }

    public void setContent(String content) {
        this.content = content;
        contentView.setText(this.content);
    }

    public void setIconRes(int iconRes) {
        IconRes = iconRes;
        IconView.setImageResource(iconRes);
    }

    public int getIconRes() {
        return IconRes;
    }

    private int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2sp(final float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public ImageView getContentIcon() {
        if (contentIcon == null) {
            return null;
        }
        return contentIcon;
    }
}
