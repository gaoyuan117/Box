package com.xzwzz.orange.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

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
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(10 * 1024 * 1024));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.append(String.class, InputStream.class, new CustomBaseGlideUrlLoader.Factory());
    }
}
