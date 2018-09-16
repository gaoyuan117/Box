package com.xzwzz.orange.module.wallper;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.WallperListBean;
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
public class WallperListAdapter extends BaseQuickAdapter<WallperListBean.PhotosUrlBean, BaseViewHolder> {

    public WallperListAdapter() {
        this(new ArrayList<>());
    }

    public WallperListAdapter(@Nullable List<WallperListBean.PhotosUrlBean> data) {
        super(R.layout.item_photo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WallperListBean.PhotosUrlBean item) {
        GlideApp.with(mContext)
                .load(item.url)
                .placeholder(R.color.color_darker_gray)
                .transition(new DrawableTransitionOptions().crossFade())
                .into((ImageView) helper.getView(R.id.iv_photo));
    }
}
