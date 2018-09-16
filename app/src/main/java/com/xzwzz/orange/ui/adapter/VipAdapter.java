package com.xzwzz.orange.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.BuyVipBean;

import java.util.List;

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
//               佛祖保佑         永无BUG
public class VipAdapter extends BaseQuickAdapter<BuyVipBean.ListBean, BaseViewHolder> {

    public VipAdapter(int layoutResId, @Nullable List<BuyVipBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BuyVipBean.ListBean item) {
        helper.setText(R.id.tv_diamondsnum, item.name);
        helper.setText(R.id.btn_preice_text, item.coin + AppConfig.CURRENCY_NAME);
        helper.setVisible(R.id.img_icon_select_num, false);
    }
}
