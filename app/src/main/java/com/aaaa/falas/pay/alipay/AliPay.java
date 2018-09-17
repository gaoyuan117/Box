package com.aaaa.falas.pay.alipay;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import com.aaaa.falas.AppConfig;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.api.http.BaseListObserver;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.base.BaseActivity;
import com.aaaa.falas.bean.HttpPaybean;

import java.util.List;

//支付宝配置信息调用支付类
public class AliPay {
    public static final String TAG = "alipay-sdk";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private static final int ALIPAY = 3;

    //    private UserDiamondsActivity mPayActivity;
    private BaseActivity mPayActivity;
    private String rechargeNum;

    public AliPay(BaseActivity payActivity) {
        this.mPayActivity = payActivity;
    }

    public interface PayListener {
        void success();

        void fail(String msg);
    }

    public void initPay(final String money, String num, final String type, final String changeid, PayListener listener) {
        rechargeNum = num;
        final String subject = rechargeNum + AppConfig.CURRENCY_NAME;
        final String body = rechargeNum + AppConfig.CURRENCY_NAME;
        final String total_fee = money;
        String uid = AppContext.getInstance().getLoginUid();
        //服务器异步通知页面路径,需要自己定义  参数 notify_url，如果商户没设定，则不会进行该操作
        final String url = AppConfig.AP_LI_PAY_NOTIFY_URL;
        //获取订单号码


        RetrofitClient.getInstance().createApi().aliPay("Charge.getAliOrder", uid, money, type, changeid, rechargeNum)
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<HttpPaybean>() {
                    @Override
                    protected void onHandleSuccess(List<HttpPaybean> list) {
                        if (list.size() > 0) {
                            HttpPaybean paybean = list.get(0);
                            if (paybean.pay_url == null || paybean.pay_url.isEmpty()) {
                                Toast.makeText(mPayActivity, "链接错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(paybean.pay_url);
                            intent.setData(content_url);
                            mPayActivity.startActivity(intent);

//                            String mOut_trade_no = res.getJSONObject(0).getJSONObject("img_url").optString("orderno");
//                            if ("1".equals(type)) {
//                                com.xzwzz.sweetbox.pay.alipay.alipay.AliPay aliPay = new com.xzwzz.sweetbox.pay.alipay.alipay.AliPay(mPayActivity);
//                                aliPay.payV2(money, "充值", mOut_trade_no, new com.xzwzz.sweetbox.pay.alipay.alipay.AliPay.AlipayCallBack() {
//                                    @Override
//                                    public void onSuccess() {
//                                        listener.success();
//                                    }
//
//                                    @Override
//                                    public void onDeeling() {
//
//                                    }
//
//                                    @Override
//                                    public void onCancle() {
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(String msg) {
//                                        listener.fail(msg);
//                                    }
//                                });
//
//                            } else if ("2".equals(type)) {
//                                WxPay.getWxPay().pay(mPayActivity, mOut_trade_no, "充值", money, code -> {
//                                    if (code == 0) {
//                                        listener.success();
//
//                                    } else {
//                                        listener.fail("");
//                                    }
//                                });
//                            }
                        }
                    }
                });
    }


    private void AldiaoYong(final String payInfo) {
        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(mPayActivity);
            // 调用支付接口
            String result = alipay.pay(payInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Result resultObj = new Result((String) msg.obj);

                    String resultStatus = resultObj.toString();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultObj.getResultStatus(), "9000")) {
                        ToastUtils.showShort("支付成功");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultObj.getResultStatus(), "8000")) {
                            ToastUtils.showShort("支付结果确认中");
                        } else {
                            ToastUtils.showShort("支付失败");
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    ToastUtils.showShort("检查结果为：" + msg.obj);
                    break;
                }
                case ALIPAY: {

                    AldiaoYong((String) msg.obj);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String out_trade_no, String subject, String body, String price, String url) {
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径 //服务器异步通知页面路径  参数 notify_url，如果商户没设定，则不会进行该操作
        orderInfo += "&notify_url=" + "\"" + url + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m〜15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Keys.PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
