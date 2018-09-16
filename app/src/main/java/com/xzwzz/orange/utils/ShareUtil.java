package com.xzwzz.orange.utils;

import android.app.Activity;
import android.content.Context;

import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
public class ShareUtil {

    public static void share(Activity activity, PlatformActionListener listener) {
        String url = AppConfig.APP_ANDROID_SHARE + "?code=" + AppContext.getInstance().getLoginUid();
        share(activity, AppConfig.SHARE_TITLE, AppConfig.SHARE_DES, url, listener);
    }

    private static void share(final Context context, String des, String title, String shareUrl, PlatformActionListener listener) {
        final OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(des);
        oks.setImageUrl("http://www.ildoudou.com/20171123170134.png");
        oks.setUrl(shareUrl);
        oks.setSiteUrl(shareUrl);
        oks.setTitleUrl(shareUrl);
        oks.setSite(context.getString(R.string.app_name));
        if (listener!=null){
            oks.setCallback(listener);

        }
        oks.show(context);
    }
}
