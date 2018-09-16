package com.xzwzz.orange.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.ChannelDataBean2;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyuan on 2018/5/16.
 */

public class LiveChannelAdapter2 extends BaseQuickAdapter<ChannelDataBean2.DataBean, BaseViewHolder> {
    public LiveChannelAdapter2() {
        this(new ArrayList<>());
    }

    public LiveChannelAdapter2(@Nullable List<ChannelDataBean2.DataBean> data) {
        super(R.layout.item_live_channel, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelDataBean2.DataBean item) {
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_peoplenum, item.getPep()+"");
        GlideApp.with(mContext)
                .load(item.getImg())
                .placeholder(R.color.color_darker_gray)
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_cover));
        GlideApp.with(mContext)
                .load(item.getImg())
                .transform(new GlideCircleTransform(mContext))
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_avatar));
    }
}
