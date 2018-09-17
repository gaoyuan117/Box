package com.aaaa.falas.api.http;


import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/3/27.
 */

public class RxUtils {

    public static <T> ObservableTransformer<T, T> io_main() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (!NetworkUtils.isConnected()) {
//                        ToastUtils.showShort("网络连接异常");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
