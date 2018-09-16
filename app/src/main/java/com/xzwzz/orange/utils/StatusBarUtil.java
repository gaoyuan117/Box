package com.xzwzz.orange.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Project_Name :b_160428_kaoqin
 * @package_Name :com.xzwzz.signname.util
 * @AUTHOR :xzwzz@vip.QqBean.com
 * @DATE :2018/3/14  14:12
 */
public class StatusBarUtil {
    //静态实例
    private static StatusBarUtil instance;
    //默认颜色和透明度
    private int DEFAULT_COLOR = 0;
    private float DEFAULT_ALPHA = 0f;
    //判断是否为魅族4版本以上，MIUI6版本以上
    private boolean isFlyme4Later, isMIUI6Later;

    private StatusBarUtil() {
        isFlyme4Later = (Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find());
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("get", String.class);
            String invoke = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            invoke = invoke.replace("v", "");
            invoke = invoke.replace("V", "");
            isMIUI6Later = Integer.parseInt(invoke) >= 6;
            Log.d("StatusBarUtil", "isMIUI6Later:" + isMIUI6Later);
        } catch (Exception e) {
            e.printStackTrace();
            isMIUI6Later = false;
        }
    }

    public static StatusBarUtil getInstance() {
        if (instance == null) {
            instance = new StatusBarUtil();
        }
        return instance;
    }

    /**
     * 沉浸式处理
     */
    public void immersive(Activity activity, @ColorRes int color, float alpha) {
        immersive(activity.getWindow(), color, alpha);
    }

    public void immersive(Activity activity, @ColorRes int color) {
        immersive(activity, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public void immersive(Activity activity) {
        immersive(activity, DEFAULT_COLOR);
    }

    public void immersive(Window window, @ColorRes int color) {
        immersive(window, DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    public void immersive(Window window) {
        immersive(window, DEFAULT_COLOR);
    }

    public void immersive(Window window, @ColorRes int color, float alpha) {
        if (Build.VERSION.SDK_INT > 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mixtureColor(color, alpha));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentView((ViewGroup) window.getDecorView(), color, alpha);
        } else if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT > 19) {
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    /**
     * 创建假的透明栏
     */
    private void setTranslucentView(ViewGroup container, int color, float alpha) {
        if (Build.VERSION.SDK_INT >= 19) {
            int mixtureColor = mixtureColor(color, alpha);
            View translucentView = container.findViewById(android.R.id.custom);
            if (translucentView == null && mixtureColor != 0) {
                translucentView = new View(container.getContext());
                translucentView.setId(android.R.id.custom);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(container.getContext()));
                container.addView(translucentView, lp);
            }
            if (translucentView != null) {
                translucentView.setBackgroundColor(mixtureColor);
            }
        }
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }

    /**
     * 颜色和透明度混合
     */
    private int mixtureColor(int color, float alpha) {

        int a = 0;
        if ((color & -0x1000000) == 0) {
            a = 0xff;
        } else {
            a = color;
        }
        return color & 0x00ffffff;
    }

    //字体变黑
    @TargetApi(Build.VERSION_CODES.M)
    public void darkMode(Window window, int color, float alpha) {
        if (isFlyme4Later) {
            darkModeForFlyme4(window, true);
        } else if (isMIUI6Later) {
            darkModeForMIUI6(window, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            darkModeForM(window, true);
        }
        immersive(window, color, alpha);
    }

    public void darkMode(Activity activity, int color, float alpha) {
        darkMode(activity.getWindow(), color, alpha);
    }

    public void darkMode(Activity activity) {
        darkMode(activity.getWindow(), DEFAULT_COLOR, DEFAULT_ALPHA);
    }

    /**
     * android 6.0设置字体颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private void darkModeForM(Window window, Boolean dark) {
        //        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        if (dark) {
            systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUiVisibility = systemUiVisibility & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     */
    private boolean darkModeForFlyme4(Window window, Boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams e = window.getAttributes();
                Field darkFlag = e.getClass().getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = e.getClass().getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(e);
                if (dark) {
                    value = value | bit;
                } else {
                    value = value & bit;
                }

                meizuFlags.setInt(e, value);
                window.setAttributes(e);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    /**
     * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
     * http://dev.xiaomi.com/doc/p=4769/
     */
    private boolean darkModeForMIUI6(Window window, Boolean darkmode) {
        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");

            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (darkmode) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
            if (darkmode) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                int flag = window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     */
    public void setPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     */
    public void setPaddingSmart(Context context, View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context);//增高
            }
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    public void setHeightAndPadding(Context context, View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);//增高
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     */
    public void setMargin(Context context, View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).topMargin += getStatusBarHeight(context);//增高
            }
            view.setLayoutParams(lp);
        }
    }
}
