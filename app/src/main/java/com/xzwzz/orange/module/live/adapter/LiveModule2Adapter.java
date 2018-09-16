package com.xzwzz.orange.module.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.PlatformBean;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyuan on 2018/5/28.
 */

public class LiveModule2Adapter extends BaseQuickAdapter<PlatformBean.DataBean, BaseViewHolder> {
    public LiveModule2Adapter() {
        this(new ArrayList<>());
    }

    public LiveModule2Adapter(@Nullable List<PlatformBean.DataBean> data) {
        super(R.layout.item_live, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlatformBean.DataBean item) {
        helper.setText(R.id.tv_category_name, item.getName());
        helper.setText(R.id.tv_category_num, item.getNum() + "");
        GlideApp.with(mContext)
                .load(item.getLogo())
//                .placeholder(R.color.color_darker_gray)
                .transform(new GlideCircleTransform(mContext))
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_category));
    }
}
