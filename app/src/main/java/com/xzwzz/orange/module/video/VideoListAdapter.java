package com.xzwzz.orange.module.video;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.VideoListBean;
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
public class VideoListAdapter extends BaseQuickAdapter<VideoListBean, BaseViewHolder> {
    public VideoListAdapter(@Nullable List<VideoListBean> data) {
        super(R.layout.item_live_channel, data);
    }

    public VideoListAdapter() {
        this(new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListBean item) {
        helper.setText(R.id.tv_name, item.title);
        helper.setVisible(R.id.tv_peoplenum, false);
        GlideApp.with(mContext)
                .load(item.video_img)
                .placeholder(R.color.color_darker_gray)
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_cover));
        helper.setVisible(R.id.iv_avatar, false);
//        GlideApp.with(mContext)
//                .load(item.video_img)
//                .transform(new GlideCircleTransform(mContext))
//                .transition(new DrawableTransitionOptions().crossFade())
//                .thumbnail(0.5f)
//                .into((ImageView) helper.getView(R.id.iv_avatar));
    }
}
