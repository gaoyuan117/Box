package com.xzwzz.orange.pay.WxPay;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class WChatPay {
    IWXAPI msgApi;

    private Activity context;

    public WChatPay(Activity context) {
        this.context = context;
        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(AppConfig.GLOBAL_WX_KEY);
    }

    /**
     * @param price 价格
     * @param num   数量
     * @dw 初始化微信支付
     */
    public void initPay(String price, String num, String changeid) {
        //TODO 初始化微信支付
//        PhoneLiveApi.wxPay(AppContext.getInstance().getLoginUid(), changeid
//                , price, num, new StringCallback() {
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Toast.makeText(AppContext.getInstance(), "获取订单失败", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        JSONArray res = ApiUtils.checkIsSuccess(response);
//                        if (null == res) return;
//                        callWxPay(res);
//                    }
//                });
        RetrofitClient.getInstance().createApi().wxPay("Charge.getWxOrder", AppContext.getInstance().getLoginUid(), changeid, price, num)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean.data.info.size() > 0) {
                            String json = new Gson().toJson(baseBean.data.info.get(0));
                            try {
                                callWxPay(new JSONObject(json));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("获取订单失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void callWxPay(JSONObject signInfo) {
        try {
            PayReq req = new PayReq();
            req.appId = signInfo.getString("appid");
            req.partnerId = signInfo.getString("partnerid");
            req.prepayId = signInfo.getString("prepayid");//预支付会话ID
            req.packageValue = "Sign=WXPay";
            req.nonceStr = signInfo.getString("noncestr");
            req.timeStamp = signInfo.getString("timestamp");
            req.sign = signInfo.getString("sign");
            if (msgApi.sendReq(req)) {
                ToastUtils.showShort("微信支付");
            } else {
                ToastUtils.showShort("请查看您是否安装微信");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
