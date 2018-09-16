package com.xzwzz.orange.api.http;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.widget.ViewStatusManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by admin on 2017/3/27.
 */

public abstract class BaseListObserver<T> implements Observer<HttpArray<T>> {
    private static final String TAG = "BaseObjObserver";
    private boolean isToast = true;//是否toast
    private ViewStatusManager mViewStatusManager;
    private SwipeRefreshLayout refreshLayout;//下拉刷新
    private ProgressDialog mProgressBar;

    protected BaseListObserver() {
        this(null, null, true);
    }

    protected BaseListObserver(ProgressDialog progressBar) {
        this(null, null, true);
        this.mProgressBar = progressBar;
        if (mProgressBar != null) {
            mProgressBar.show();
        }
    }

    protected BaseListObserver(SwipeRefreshLayout refreshLayout) {
        this(null, refreshLayout, true);
    }

    protected BaseListObserver(boolean isToast) {
        this(null, true);
    }

    protected BaseListObserver(ViewStatusManager mViewStatusManager) {
        this(mViewStatusManager, true);
    }

    protected BaseListObserver(ViewStatusManager mViewStatusManager, SwipeRefreshLayout refreshLayout) {
        this(mViewStatusManager, refreshLayout, true);
    }

    protected BaseListObserver(ViewStatusManager mViewStatusManager, boolean isToast) {
        this(mViewStatusManager, null, isToast);
    }

    protected BaseListObserver(ViewStatusManager mViewStatusManager, SwipeRefreshLayout refreshLayout, boolean isToast) {
        this.mViewStatusManager = mViewStatusManager;
        this.refreshLayout = refreshLayout;
        this.isToast = isToast;
        if (mViewStatusManager != null) {
            mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.loading);
        }
    }

    @Override
    public void onNext(HttpArray<T> value) {
        if (mProgressBar != null) {
            mProgressBar.dismiss();
        }
        if (value.ret == 200) {
            if (value.data.code == 0) {
                List<T> t = value.data.info;
                if (mViewStatusManager != null) {
                    if (t.size() == 0) {
                        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.empty);
                    } else {
                        mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.success);
                    }
                }
                onHandleSuccess(t);
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            } else if (value.data.code == 700) {
                onHandleError(value.data.code, value.data.msg);
                LoginUtils.ExitLoginStatus();
            } else {
                onHandleError(value.data.code, value.data.msg);
                Log.d(TAG, value.data.code + "----" + value.data.msg);
            }
        } else {
            onHandleError(value.ret, value.msg);
        }
    }


    @Override
    public void onError(Throwable e) {
        if (mProgressBar != null) {
            mProgressBar.dismiss();
        }
        e.printStackTrace();
        if (mViewStatusManager != null) {
            mViewStatusManager.setStatus(ViewStatusManager.ViewStatus.error);
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setEnabled(false);
        }

    }

    @Override
    public void onComplete() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    protected abstract void onHandleSuccess(List<T> list);

    protected void onHandleError(int code, String msg) {
        //是否Toast，默认true
        if (isToast && !msg.isEmpty()) {
            ToastUtils.showShort(msg);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        //显示对话框
//        if (!TextUtils.isEmpty(message)) {
//            progressDialog = new ProgressDialog(mContext);
//            progressDialog.setMessage(message);
//            progressDialog.show();
//        }
    }
}
