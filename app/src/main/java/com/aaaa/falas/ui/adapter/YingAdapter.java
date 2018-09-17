package com.aaaa.falas.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.aaaa.falas.R;
import com.aaaa.falas.bean.YingListBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gaoyuan on 2018/6/22.
 */

public class YingAdapter extends BaseQuickAdapter<YingListBean, BaseViewHolder> {

    public YingAdapter(int layoutResId, @Nullable List<YingListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YingListBean item) {
        CircleImageView imageView = helper.getView(R.id.img_logo);

        Glide.with(mContext).load(item.getImg_url()).into(imageView);

        helper.setText(R.id.tv_name, item.getTitle());

    }
}
