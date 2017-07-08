package com.tpyzq.mobile.pangu.adapter.myself;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;


/**
 * anthor:Created by tianchen on 2017/3/23.
 * email:963181974@qq.com
 */

public class ChooseRSAdapter extends BaseAdapter {

    private Context context;
    private int point;

    public ChooseRSAdapter(Context context) {
        this.context = context;
    }

    public void setPoint(int point) {
        this.point = point;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return DataUtils.relationship_name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_rs, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(DataUtils.relationship_name[position]);
        if (position == point) {
            viewHolder.iv_icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_icon.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        ImageView iv_icon;
    }
}
