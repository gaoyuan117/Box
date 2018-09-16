package com.xzwzz.orange.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.PromotionBean;

import java.util.ArrayList;

/*
 * @Project_Name :SweetBox
 * @package_Name :com.xzwzz.sweetbox.ui.adapter
 * @AUTHOR      :xzwzz@vip.QqBean.com
 * @DATE        :2018/4/20
 */
public class PromotionAdapter extends BaseQuickAdapter<PromotionBean, BaseViewHolder> {
    public PromotionAdapter() {
        super(R.layout.item_promotion, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, PromotionBean item) {
        helper.setText(R.id.tv_title, item.getUser_nicename());
        helper.setText(R.id.tv_id, "ID:" + item.getId());
        helper.setText(R.id.tv_income, "总收益：" + item.getVotes() + "元");
    }
}
