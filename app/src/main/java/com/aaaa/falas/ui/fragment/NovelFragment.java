package com.aaaa.falas.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.aaaa.falas.R;
import com.aaaa.falas.api.http.BaseListObserver;
import com.aaaa.falas.api.http.RetrofitClient;
import com.aaaa.falas.api.http.RxUtils;
import com.aaaa.falas.base.BaseFragment;
import com.aaaa.falas.bean.BookBean;
import com.aaaa.falas.module.book.BookAdapter;
import com.aaaa.falas.module.book.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class NovelFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    BookAdapter adapter;
    List<BookBean> data = new ArrayList<>();
    String id;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_novel;
    }

    @Override
    public void initView(View view) {

        id = getArguments().getString("id");
        LogUtils.e("id：" + id);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new BookAdapter(data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        RetrofitClient.getInstance().createApi().getBookList(id).compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<BookBean>() {
                    @Override
                    protected void onHandleSuccess(List<BookBean> list) {
                        if (list == null || list.size() == 0) return;
                        data.clear();
                        data.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookBean item = (BookBean) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", item.id);
        bundle.putString("name", item.post_title);
        ActivityUtils.startActivity(bundle, BookDetailActivity.class);
    }
}
