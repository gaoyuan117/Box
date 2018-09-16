package com.xzwzz.orange.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.xzwzz.orange.module.video.VideoPlayActivity;

import cn.jzvd.JZVideoPlayerStandard;

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
public class XzwzzPlayer extends JZVideoPlayerStandard {
    public XzwzzPlayer(Context context) {
        super(context);
    }

    public XzwzzPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        if (currentScreen == SCREEN_WINDOW_NORMAL && v.getId() == cn.jzvd.R.id.back) {
            ActivityUtils.finishActivity(VideoPlayActivity.class);
            return;
        }
        super.onClick(v);
    }

    @Override
    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        if (backButton.getVisibility() == GONE) {
            backButton.setVisibility(VISIBLE);
        }
    }
}
