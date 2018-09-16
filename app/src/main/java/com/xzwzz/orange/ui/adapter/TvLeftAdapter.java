package com.xzwzz.orange.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.TvTermBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @Project_Name :Sweet
 * @package_Name :com.xzwzz.orange.ui
 * @AUTHOR      :xzwzz@vip.qq.com
 * @DATE        :2018/6/12
 */
import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.TvTermBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @Project_Name :Sweet
 * @package_Name :com.xzwzz.orange.ui
 * @AUTHOR      :xzwzz@vip.qq.com
 * @DATE        :2018/6/12
 */
public class TvLeftAdapter extends BaseQuickAdapter<TvTermBean, BaseViewHolder> {
    public TvLeftAdapter() {
        super(R.layout.item_tv_left, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, TvTermBean item) {
        if (item.isSelect) {
            helper.setVisible(R.id.view_select, true);
            helper.setTextColor(R.id.tv_title, Color.parseColor("#FB3E38"));
        } else {
            helper.setVisible(R.id.view_select, false);
            helper.setTextColor(R.id.tv_title, Color.parseColor("#444444"));
        }
        helper.setText(R.id.tv_title, item.name);
    }
}