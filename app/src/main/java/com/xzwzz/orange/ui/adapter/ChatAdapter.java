package com.xzwzz.orange.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzwzz.orange.R;
import com.xzwzz.orange.utils.SpannableUtils;

import java.util.List;

public class ChatAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ChatAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        TextView textView = helper.getView(R.id.tv_title);

        item = item.replaceAll(":", "：");

        if (item.contains("系统消息")) {
            textView.setTextColor(mContext.getResources().getColor(R.color.text_green));
            textView.setText(SpannableUtils.showDiffColor(item, 0, 5, Color.parseColor("#f0cd49")));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setText(SpannableUtils.showDiffColor(item, 0, item.indexOf("："), Color.parseColor("#f0cd49")));
        }

    }

}
