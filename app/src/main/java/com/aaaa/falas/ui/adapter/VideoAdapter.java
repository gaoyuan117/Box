package com.aaaa.falas.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aaaa.falas.R;
import com.aaaa.falas.bean.VideoListBean;
import com.aaaa.falas.bean.YunVideoBean;

import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private Context context;
    private List<VideoListBean> list;

    public VideoAdapter(Context context, List<VideoListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_yun_video, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTiele.setText(list.get(i).title);
        return view;
    }

    class ViewHolder {
        TextView tvTiele;

        public ViewHolder(View view) {
            tvTiele = view.findViewById(R.id.tv_yun_name);
        }
    }
}
