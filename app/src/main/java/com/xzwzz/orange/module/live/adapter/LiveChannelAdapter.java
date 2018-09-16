package com.xzwzz.orange.module.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.ChannelDataBean;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;


public class LiveChannelAdapter extends BaseQuickAdapter<ChannelDataBean.DataBean, BaseViewHolder> {
    public LiveChannelAdapter() {
        this(new ArrayList<>());
    }

    public LiveChannelAdapter(@Nullable List<ChannelDataBean.DataBean> data) {
        super(R.layout.item_channel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelDataBean.DataBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_peoplenum, item.getNum());
        GlideApp.with(mContext)
                .load(item.getBigpic())
                .placeholder(R.color.color_darker_gray)
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_cover));
        GlideApp.with(mContext)
                .load(item.getBigpic())
                .transform(new GlideCircleTransform(mContext))
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_avatar));
    }
}
