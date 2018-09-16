package com.xzwzz.orange.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseListObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.bean.TvTermBean;
import com.xzwzz.orange.ui.adapter.TvLeftAdapter;
import com.xzwzz.orange.ui.adapter.TvRightAdapter;

import java.util.ArrayList;
import java.util.List;

public class TvActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private android.support.v7.widget.RecyclerView leftRv;
    private android.support.v7.widget.RecyclerView rightRv;
    private TvLeftAdapter leftmAdapter;
    private TvRightAdapter rightmAdapter;
    private TextView tvTitle;
    private com.xzwzz.orange.widget.ViewStatusManager viewStatusmanager;

    private ArrayList<TvTermBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        initView();
        initData();
        setListener();
    }

    protected void initView() {

        leftRv = findViewById(R.id.left_rv);
        rightRv = findViewById(R.id.right_rv);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("卫视直播");
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        leftRv.setLayoutManager(new LinearLayoutManager(this));
        rightRv.setLayoutManager(new LinearLayoutManager(this));

        leftRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rightRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        leftmAdapter = new TvLeftAdapter();
        rightmAdapter = new TvRightAdapter();

        leftRv.setAdapter(leftmAdapter);
        rightRv.setAdapter(rightmAdapter);
        viewStatusmanager = findViewById(R.id.view_statusmanager);
    }

    protected void initData() {
        data = new ArrayList<>();
        leftmAdapter.setNewData(data);
        getTerm();
    }

    protected void setListener() {
        leftmAdapter.setOnItemClickListener(this);
        rightmAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == leftmAdapter) {
            getDataById(position);
        } else if (adapter == rightmAdapter) {
            TvTermBean.tvListbean item = (TvTermBean.tvListbean) adapter.getItem(position);
            Intent intent = new Intent(this, PlayTvActivity.class);
            intent.putExtra("title", item.name);
//            intent.putExtra("url", "http://res.cloudinary.com/xzwzztest/video/upload/v1528271687/video/oxv7vw6eh943myem55tk.mp4");
            intent.putExtra("url", item.url);
            startActivity(intent);
        }
    }

    private void getTerm() {
        RetrofitClient.getInstance().createApi().tvTerm("home.TvTerm")
                .compose(RxUtils.io_main())
                .subscribe(new BaseListObserver<TvTermBean>(viewStatusmanager) {
                    @Override
                    protected void onHandleSuccess(List<TvTermBean> list) {
                        data.addAll(list);
                        leftmAdapter.notifyDataSetChanged();
                        getDataById(0);
                    }
                });
    }

    private void getDataById(final int position) {
        if (data.get(position).isSelect) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (i != position) {
                data.get(i).isSelect = false;
            } else {
                data.get(i).isSelect = true;
            }
        }
        leftmAdapter.notifyDataSetChanged();
        List localList = data.get(position).list;
        if (localList != null && localList.size() > 0) {
            rightmAdapter.setNewData(localList);
        } else {
            RetrofitClient.getInstance().createApi().tvList("home.TvList", data.get(position).term_id)
                    .compose(RxUtils.io_main())
                    .subscribe(new BaseListObserver<TvTermBean.tvListbean>() {
                        @Override
                        protected void onHandleSuccess(List<TvTermBean.tvListbean> list) {
                            if (list != null && list.size() != 0) {
                                data.get(position).list = list;
                                rightmAdapter.setNewData(data.get(position).list);
                            }
                        }
                    });
        }
    }


}
