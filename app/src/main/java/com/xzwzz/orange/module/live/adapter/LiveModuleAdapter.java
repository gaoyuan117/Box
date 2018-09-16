package com.xzwzz.orange.module.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.bean.PlatformBean1;
import com.xzwzz.orange.glide.GlideApp;
import com.xzwzz.orange.glide.GlideCircleTransform;

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
public class LiveModuleAdapter extends BaseQuickAdapter<PlatformBean1.DataBean, BaseViewHolder> {
    public LiveModuleAdapter() {
        this(new ArrayList<>());
    }

    public LiveModuleAdapter(@Nullable List<PlatformBean1.DataBean> data) {
        super(R.layout.item_live, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlatformBean1.DataBean item) {
        helper.setText(R.id.tv_category_name, item.getTitle());
        helper.setText(R.id.tv_category_num, item.getNumber() + "");
        GlideApp.with(mContext)
                .load(item.getImg())
                .error(R.mipmap.ic_launcher)
                .transform(new GlideCircleTransform(mContext))
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) helper.getView(R.id.iv_category));
    }
}
