package com.xzwzz.orange.module.video;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.VideoBean;
import com.xzwzz.orange.glide.GlideApp;

import java.util.ArrayList;
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
//
//               佛祖保佑         永无BUG
public class VideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {
    public VideoAdapter() {
        this(new ArrayList<>());
    }

    public VideoAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.item_video, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        helper.setText(R.id.tv_category_name, "#" + item.name);
        GlideApp.with(mContext)
                .load(item.thumb)
                .placeholder(R.color.color_darker_gray)
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_category));
    }
}
