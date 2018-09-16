package com.xzwzz.orange.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseFragment;
import com.xzwzz.orange.bean.SearchPlayUrlBean;
import com.xzwzz.orange.bean.VideoListBean;
import com.xzwzz.orange.bean.YunSearchBean;
import com.xzwzz.orange.module.video.VideoPlayActivity;
import com.xzwzz.orange.ui.adapter.YunSearchAdapter;
import com.xzwzz.orange.ui.login.LoginActivity;
import com.xzwzz.orange.utils.LoginUtils;
import com.xzwzz.orange.utils.MemberUtil;
import com.xzwzz.orange.utils.SharePrefUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by gaoyuan on 2018/6/22.
 */

public class YunSearchFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private EditText editText;
    private RecyclerView recyclerView;

    private YunSearchAdapter adapter;
    private List<YunSearchBean.DataBean> list = new ArrayList<>();
    private int page = 1;
    private ProgressDialog dialog;
    private String video_url;

    @Override
    public int getLayoutId() {
        return R.layout.yun_search_fragment;
    }

    @Override
    public void initView(View view) {
        video_url = SharePrefUtil.getString("video_url", "");
        dialog = new ProgressDialog(getActivity());
        editText = view.findViewById(R.id.et_search);
        recyclerView = view.findViewById(R.id.recycler);

        view.findViewById(R.id.tv_search).setOnClickListener(this);
        view.findViewById(R.id.tv_nvyou).setOnClickListener(this);
        view.findViewById(R.id.tv_xuesheng).setOnClickListener(this);
        view.findViewById(R.id.tv_renqi).setOnClickListener(this);
        view.findViewById(R.id.tv_dongman).setOnClickListener(this);

        adapter = new YunSearchAdapter(R.layout.item_search, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, recyclerView);

        adapter.setOnItemClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        page = 1;
        switch (v.getId()) {
            case R.id.tv_search:

                break;
            case R.id.tv_nvyou:
                editText.setText("女优");
                break;
            case R.id.tv_xuesheng:
                editText.setText("学生");
                break;

            case R.id.tv_renqi:
                editText.setText("人妻");
                break;
            case R.id.tv_dongman:
                editText.setText("动漫");
                break;
        }

        search();
    }

    private void search() {

        String key = editText.getText().toString();
        if (TextUtils.isEmpty(key)) return;
        if (page == 1) dialog.show();
        RetrofitClient.getInstance().createApi().search(video_url + "?k=" + key + "&p=" + page)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<YunSearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(YunSearchBean yunSearchBean) {
                        if (yunSearchBean == null) return;

                        if (yunSearchBean.getData() == null || yunSearchBean.getData().size() == 0) {
                            adapter.loadMoreEnd();
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(yunSearchBean.getData());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        adapter.loadMoreFail();
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                        adapter.loadMoreComplete();

                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        search();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }

        getPlayUrl(list.get(position).getUrl(), list.get(position).getTitel(), list.get(position).getImg());
    }

    private void getPlayUrl(String url, String title, String img) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort("无法播放");
            return;
        }
        dialog.show();
        RetrofitClient.getInstance().createApi().getPlayUrl(url)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<SearchPlayUrlBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchPlayUrlBean urlBean) {

                        VideoListBean bean = new VideoListBean();
                        bean.title = title;
                        bean.video_img = img;
                        bean.video_url = urlBean.getMp41();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", bean);

                        toActivity(bundle);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        ToastUtils.showShort("连接超时，请重新获取");
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void toActivity(Bundle bundle) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                ActivityUtils.startActivity(bundle, VideoPlayActivity.class);

            }

            @Override
            public void noMember() {
                LoginUtils.vipDialog(getActivity());
            }
        }));
    }
}
