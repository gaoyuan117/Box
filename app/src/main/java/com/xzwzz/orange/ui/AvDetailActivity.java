package com.xzwzz.orange.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xzwzz.orange.AppConfig;
import com.xzwzz.orange.AppContext;
import com.xzwzz.orange.R;
import com.xzwzz.orange.api.http.BaseObjObserver;
import com.xzwzz.orange.api.http.RetrofitClient;
import com.xzwzz.orange.api.http.RxUtils;
import com.xzwzz.orange.base.BaseActivity;
import com.xzwzz.orange.bean.VideoDetailBean;
import com.xzwzz.orange.bean.VideoListBean;
import com.xzwzz.orange.module.video.VideoPlayActivity;
import com.xzwzz.orange.ui.adapter.AvDetailAdapter;
import com.xzwzz.orange.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class AvDetailActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    private LinearLayout layoutTips;
    private ImageView videoImg;
    private ImageView imgAd;
    private TextView tvNum;
    private TextView tvTitle;
    private TextView tvWatch;
    private TextView tvCount;
    private RecyclerView recyclerView;

    private String id;
    private List<VideoDetailBean.ListBean> list = new ArrayList<>();
    private AvDetailAdapter adapter;
    private VideoDetailBean detailBean;

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected Object getIdOrView() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        id = getIntent().getStringExtra("id");
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        TextView title = findViewById(R.id.tv_title);

        title.setText("视频详情");

        layoutTips = findViewById(R.id.layout_tips);
        videoImg = findViewById(R.id.img_diamond);
        imgAd = findViewById(R.id.img_ad);
        tvNum = findViewById(R.id.tv_view);
        tvTitle = findViewById(R.id.tv_diamond_title);
        tvCount = findViewById(R.id.tv_diamond);
        tvWatch = findViewById(R.id.tv_watch);
        tvCount.setVisibility(View.GONE);
        tvNum.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler);
        layoutTips.setVisibility(View.GONE);
        imgAd.setVisibility(View.GONE);
        tvWatch.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        adapter = new AvDetailAdapter(R.layout.item_av_detail, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
//        ad();
        videoImg.setOnClickListener(v -> toActivity());
        findViewById(R.id.close).setOnClickListener(v -> layoutTips.setVisibility(View.GONE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        video();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, AvDetailActivity.class);
        intent.putExtra("title", list.get(position).getTitle());
        intent.putExtra("id", list.get(position).getId());
        startActivity(intent);
        finish();
    }

    private void video() {
        RetrofitClient.getInstance().createApi().videoDetail("Home.Videodetails", AppContext.getInstance().getLoginUid(), id)
                .compose(RxUtils.io_main())
                .subscribe(new BaseObjObserver<VideoDetailBean>() {
                    @Override
                    protected void onHandleSuccess(VideoDetailBean bean) {
                        detailBean = bean;
                        GlideUtils.glide(AvDetailActivity.this, bean.getDetails().getVideo_img(), videoImg);

                        GlideUtils.glide(mContext, bean.getDetails().getVideo_img(), videoImg);
                        tvNum.setText(bean.getDetails().getWatch_num() + "");
                        tvTitle.setText(bean.getDetails().getTitle());
                        tvCount.setText(bean.getDetails().getCoin() + "");
                        tvWatch.setText(bean.getDetails().getWatch_num() + "");
                        list.clear();
                        if (bean.getList() == null || bean.getList().size() == 0) return;
                        list.addAll(bean.getList());
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    private void toActivity() {

        if (!AppConfig.IS_MEMBER) {
            dialog();
            return;
        }
        startActivity();
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

    private void startActivity() {
        Bundle bundle = new Bundle();
        VideoListBean bean = new VideoListBean();
        bean.title = detailBean.getDetails().getTitle();
        bean.video_url = detailBean.getDetails().getVideo_url();
        bundle.putSerializable("data", bean);
        ActivityUtils.startActivity(bundle, VideoPlayActivity.class);


    }
}
