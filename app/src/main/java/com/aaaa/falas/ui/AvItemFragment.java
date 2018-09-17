package com.aaaa.falas.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aaaa.falas.AppContext;
import com.aaaa.falas.ui.login.LoginActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.aaaa.falas.R;
import com.aaaa.falas.api.http.BaseListObserver;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.base.BaseFragment;
import com.aaaa.falas.bean.AvVideoListBean;
import com.aaaa.falas.ui.adapter.AvAdapter;

import java.util.ArrayList;
import java.util.List;

public class AvItemFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recycler;

    private List<AvVideoListBean> list = new ArrayList<>();
    private AvAdapter adapter;
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_av_item;
    }

    @Override
    public void initView(View view) {
        id = getArguments().getString("id");
        Log.e("gy", "id:" + id);
        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(6f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 2 == 1) {
                    outRect.set(offest / 2, offest, offest / 2, 0);
                } else if (position % 2 == 2) {
                    outRect.set(offest / 2, offest, 0, 0);
                }
            }
        });

        adapter = new AvAdapter(R.layout.item_av, list);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        getVideoList();
    }

    public void getVideoList() {
        RetrofitClient.getInstance().createApi().videoList("Home.VideoList", id).compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<AvVideoListBean>() {
                    @Override
                    protected void onHandleSuccess(List<AvVideoListBean> avList) {
                        if (avList == null || avList.size() == 0) return;
                        list.clear();
                        list.addAll(avList);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!AppContext.getInstance().isLogin()) {
            ActivityUtils.startActivity(LoginActivity.class);
            return;
        }
        AvVideoListBean bean = list.get(position);
        toActivity(bean);
    }

    private void toActivity(AvVideoListBean bean) {
        Bundle bundle = new Bundle();
        bundle.putString("title", bean.getTitle());
        bundle.putString("url", bean.getVideo_url());
        bundle.putString("id", bean.getId());
        bundle.putSerializable("type", "av");
        ActivityUtils.startActivity(bundle, AvDetailActivity.class);
    }
}
