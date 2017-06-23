package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.List;

/**
 * 分级基金撤单Adapter
 */

public class FJWithdrawOrderAdapter extends BaseAdapter {
    private List<StructuredFundEntity> mList;
    private LayoutInflater layoutInflater;

    public FJWithdrawOrderAdapter(Context context, List<StructuredFundEntity> list) {
        this.mList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_fj_entrusted_query, parent, false);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv3.setTextColor(Color.RED);


        return convertView;
    }


    class ViewHolder {
        TextView tv1, tv2, tv3, tv4, tv6, tv5;
    }
}
