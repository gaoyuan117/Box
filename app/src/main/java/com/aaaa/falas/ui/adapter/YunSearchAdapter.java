package com.aaaa.falas.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.aaaa.falas.R;
import com.aaaa.falas.bean.YunSearchBean;
import com.aaaa.falas.glide.GlideApp;

import java.util.List;

/**
 * Created by gaoyuan on 2018/6/22.
 */

public class YunSearchAdapter extends BaseQuickAdapter<YunSearchBean.DataBean, BaseViewHolder> {

    public YunSearchAdapter(int layoutResId, @Nullable List<YunSearchBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YunSearchBean.DataBean item) {
        ImageView imageView = helper.getView(R.id.img_logo);
        Glide.with(mContext).load(item.getImg()).into(imageView);
        helper.setText(R.id.tv_title, item.getTitel())
                .setText(R.id.tv_time, item.getTime());
    }
}
