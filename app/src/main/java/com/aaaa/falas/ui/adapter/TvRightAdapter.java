package com.aaaa.falas.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.aaaa.falas.R;
import com.aaaa.falas.bean.TvTermBean;

import java.util.ArrayList;
import java.util.List;

/*
 * @Project_Name :Sweet
 * @package_Name :com.xzwzz.orange.ui
 * @AUTHOR      :xzwzz@vip.qq.com
 * @DATE        :2018/6/12
 */
public class TvRightAdapter extends BaseQuickAdapter<TvTermBean.tvListbean, BaseViewHolder> {
    public TvRightAdapter() {
        super(R.layout.item_tv_right, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, TvTermBean.tvListbean item) {
        helper.setText(R.id.tv_title, item.name);
    }
}
