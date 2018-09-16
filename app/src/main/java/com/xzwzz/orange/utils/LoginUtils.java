package com.xzwzz.orange.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.HttpArray;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.BaseBean;
import com.xzwzz.orange.ui.VipActivity;
import com.xzwzz.orange.ui.login.LoginActivity;

import java.lang.ref.WeakReference;
import java.util.Random;

import io.reactivex.Observer;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

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
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
public class LoginUtils {
    public static boolean getLoginStatus() {
        return AppContext.getInstance().isLogin();
    }

    public static void ExitLoginStatus() {
        AppContext.getInstance().cleanLoginInfo();
        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityUtils.finishAllActivities();
        AppContext.getInstance().startActivity(intent);
    }

    public static void tokenIsOutTime(Observer<HttpArray<BaseBean>> obs) {
        String loginUid = AppContext.getInstance().getLoginUid();
        if (loginUid.equals("0")) return;
        RetrofitClient.getInstance().createApi().checkToken("User.iftoken", AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken())
                .compose(RxUtils.io_main())
                .subscribe(obs);
    }

    /**
     * 获取随机验证码
     *
     * @return
     */
    public static String getNum() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {

            Random rand = new Random();
            int num = rand.nextInt(10);

            if (i == 3) {
                sb.append(num);
            } else {
                sb.append(num + " ");
            }
        }

        LogUtils.e("随机验证码：" + sb.toString().replaceAll(" ", ""));
        return sb.toString();
    }




    public static void vipDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.wx_dialog);
        View view = View.inflate(activity, R.layout.dialog_commom, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText("账号到期，请续费");
        TextView tvClose = view.findViewById(R.id.tv_close);
        tvClose.setText("续费");
        tvClose.setOnClickListener(v -> {
            startActivity(VipActivity.class);
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }


}
