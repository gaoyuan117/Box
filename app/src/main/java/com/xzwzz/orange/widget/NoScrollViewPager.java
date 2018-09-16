package com.xzwzz.orange.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Project_Name :xiaoniao
 * @package_Name :com.vlive.aiqiji.widget
 * @AUTHOR :xzwzz@vip.QqBean.com
 * @DATE :2018/3/23  13:46
 */
public class NoScrollViewPager extends ViewPager {
    // the sliding page switch
    private boolean isSlidingEnable = false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable && super.onInterceptTouchEvent(ev);
    }

    public void setSlidingEnable(boolean slidingEnable) {
        this.isSlidingEnable = slidingEnable;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false);
    }
}
