package com.xzwzz.orange.module;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.R;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.widget.ViewStatusManager;

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
public abstract class AbsModuleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ViewStatusManager mViewStatusManager;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_module;
    }

    @Override
    protected void initView() {
        mViewStatusManager = findViewById(R.id.viewstatusmanager);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mRecyclerView = findViewById(R.id.recycler_live);
        setRecyclerView();
        mAdapter = getAdapter();
    }

    @Override
    protected void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
        mViewStatusManager.setStatusChangeListener(() -> {
            loadData();
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    protected abstract BaseQuickAdapter getAdapter();

    @Override
    public void onRefresh() {
        loadData();
    }


    protected abstract void loadData();

    protected void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                int position = parent.getChildPosition(view);
//                int offest = SizeUtils.dp2px(10f);
//                if (position % 3 == 0) {
//                    outRect.set(offest, offest, offest / 2, 0);
//                } else if (position % 3 == 1) {
//                    outRect.set(offest / 2, offest, offest / 2, 0);
//                } else if (position % 3 == 2) {
//                    outRect.set(offest / 2, offest, offest, 0);
//                }
//            }
//        });
    }

    @Override
    public abstract void onItemClick(BaseQuickAdapter adapter, View view, int position);
}
