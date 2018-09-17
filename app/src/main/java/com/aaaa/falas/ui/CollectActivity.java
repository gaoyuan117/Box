package com.aaaa.falas.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.aaaa.falas.AppContext;
import com.aaaa.falas.R;
import com.aaaa.falas.base.BaseActivity;
import com.aaaa.falas.bean.ChannelDataBean;
import com.aaaa.falas.bean.ChannelDataBean1;
import com.aaaa.falas.bean.CollectBean;
import com.aaaa.falas.bean.CollectBeanDao;
import com.aaaa.falas.module.live.LivePlayActivity;
import com.aaaa.falas.module.live.adapter.LiveChannelAdapter;
import com.aaaa.falas.utils.MemberUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    private CollectBeanDao collectBeanDao;
    private LiveChannelAdapter adapter;
    private List<ChannelDataBean.DataBean> mList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildPosition(view);
                int offest = SizeUtils.dp2px(5f);
                if (position % 2 == 0) {
                    outRect.set(offest, offest, offest / 2, 0);
                } else if (position % 2 == 1) {
                    outRect.set(offest / 2, offest, offest, 0);
                }
            }
        });

        adapter = new LiveChannelAdapter();
        recyclerView.setAdapter(adapter);

        findViewById(R.id.img_back).setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("我的收藏");

        adapter.setOnItemClickListener(this);

        adapter.setOnItemLongClickListener(this);
    }

    @Override
    protected void initData() {
        collectBeanDao = AppContext.getDaoInstant().getCollectBeanDao();
        List<CollectBean> list = collectBeanDao.queryBuilder().list();
        if (list == null || list.size() == 0) return;
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            ChannelDataBean.DataBean bean = new ChannelDataBean.DataBean();
            CollectBean collectBean = list.get(i);
            bean.setBigpic(collectBean.getImg());
            bean.setName(collectBean.getName());
            bean.setUrl(collectBean.getUrl());
            bean.setNum(collectBean.getNum());
            bean.setUid(collectBean.getId());
            mList.add(bean);
        }

        adapter.setNewData(mList);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        goRoom(mList.get(position));
    }

    private void goRoom(ChannelDataBean.DataBean item) {
        MemberUtil.delayCheckMember(new WeakReference<>(new MemberUtil.MemberListener() {
            @Override
            public void isMemeber() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", item);
                ActivityUtils.startActivity(bundle, LivePlayActivity.class);
            }

            @Override
            public void noMember() {
                dialog();
            }
        }));
    }

    private void dialog() {
        Dialog dialog = new Dialog(this, R.style.wx_dialog);
        View view = View.inflate(this, R.layout.dialog_commom, null);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText("账号到期，请续费");
        TextView tvClose = view.findViewById(R.id.tv_close);
        tvClose.setText("续费");
        tvClose.setOnClickListener(v -> {
            ActivityUtils.startActivity(VipActivity.class);
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

        new AlertDialog.Builder(this).setMessage("是否移除该收藏")
                .setNegativeButton("取消", (dialog, which) -> {

                })
                .setPositiveButton("确定", (dialog, which) -> {

                    CollectBean collectBean = new CollectBean();
                    collectBean.setUrl(mList.get(position).getUrl());
                    collectBean.setNum(mList.get(position).getNum());
                    collectBean.setName(mList.get(position).getName());
                    collectBean.setImg(mList.get(position).getBigpic());

                    collectBeanDao.deleteByKey(mList.get(position).getUid());

                    mList.remove(position);
                    adapter.notifyDataSetChanged();

                }).show();

        return true;
    }
}
