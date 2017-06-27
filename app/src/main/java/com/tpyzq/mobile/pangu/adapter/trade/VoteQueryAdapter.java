package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

/**
 * 投票查询Adapter
 */

public class VoteQueryAdapter extends BaseAdapter {
    private Context context;

    public VoteQueryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_vote_query, parent, false);

        // 点击显示隐藏部分
        TextView tv1 = (TextView) convertView.findViewById(R.id.TextView1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.TextView1);
        TextView tv3 = (TextView) convertView.findViewById(R.id.TextView1);
        TextView tv4 = (TextView) convertView.findViewById(R.id.TextView1);
        tv1.setText("委托日期");
        tv2.setText("委托数量");
        tv3.setText("证券名称");
        tv4.setText("委托编号");

        return convertView;
    }
}
