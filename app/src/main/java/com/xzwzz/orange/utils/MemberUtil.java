package com.xzwzz.orange.utils;

import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

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
public class MemberUtil {

    public static void delayCheckMember(WeakReference<MemberListener> listener, int second) {
        if (second == 0) {
            if (isMember()) {
                listener.get().isMemeber();
            } else {
                listener.get().noMember();
            }
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    AppContext.getHandler().post(() -> {
                        if (isMember()) {
                            listener.get().isMemeber();
                        } else {
                            listener.get().noMember();
                        }
                    });
                }
            }, second * 1000);
        }
    }

    public static void delayCheckMember(WeakReference<MemberListener> listener) {
        delayCheckMember(listener, 0);
    }

    public static boolean isMember() {
        return AppConfig.IS_MEMBER;
    }

    public interface MemberListener {
        void isMemeber();

        void noMember();
    }
}
