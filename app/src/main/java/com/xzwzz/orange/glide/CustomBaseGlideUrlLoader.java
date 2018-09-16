package com.xzwzz.orange.glide;

import android.support.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CustomBaseGlideUrlLoader extends BaseGlideUrlLoader<String> {
    private static final ModelCache<String, GlideUrl> urlCache = new ModelCache<>(150);
    private static final Pattern PATTERN = Pattern.compile("__w-((?:-?\\d+)+)__");

    protected CustomBaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> concreteLoader, ModelCache<String, GlideUrl> modelCache) {
        super(concreteLoader, modelCache);
    }

    @Override
    protected String getUrl(String model, int width, int height, Options options) {
        Matcher matcher = PATTERN.matcher(model);
        int bestBucket;
        if (matcher.find()) {
            List<String> list = Arrays.asList(matcher.group(1).split("-"));
            ArrayList<String> result = new ArrayList<>();
            for (String s : list) {
                if (!s.isEmpty()) {
                    result.add(s);
                }
            }
            for (String s : result) {
                bestBucket = Integer.parseInt(s);
                if (bestBucket >= width) {
                    break;
                }
            }
        }
        return model;
    }


    @Override
    public boolean handles(@NonNull String s) {
        return true;
    }

    static class Factory implements ModelLoaderFactory<String, InputStream> {
        @NonNull
        @Override
        public ModelLoader<String, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new CustomBaseGlideUrlLoader(multiFactory.build(GlideUrl.class, InputStream.class), urlCache);
        }

        @Override
        public void teardown() {

        }
//        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
//            return CustomBaseGlideUrlLoader(multiFactory.build(GlideUrl::class.java, InputStream::class.java), urlCache)
//        }
//
//        override fun teardown() {
//
//        }
    }
}
