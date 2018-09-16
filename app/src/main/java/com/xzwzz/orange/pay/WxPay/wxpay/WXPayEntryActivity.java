package com.xzwzz.orange.pay.WxPay.wxpay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xzwzz.orange.R;
import com.xzwzz.orange.pay.alipay.alipay.Constants;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
  private final String TAG = this.getClass().getName();

  private IWXAPI api;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.pay_result);

    api = WXAPIFactory.createWXAPI(this, Constants.WxPay.APP_ID);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq req) {
  }

  @Override
  public void onResp(BaseResp resp) {
    Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

    if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
      WxPay.getWxPay().onRes(resp.errCode);
      finish();
    }
  }
}