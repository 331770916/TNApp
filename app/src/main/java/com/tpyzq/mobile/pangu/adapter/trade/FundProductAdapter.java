package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundEntity;

import java.util.List;


/**
 * Created by 刘泽鹏 on 2016/8/12.
 * 基金产品的列表的Adapter
 */
public class FundProductAdapter extends BaseAdapter {
    private Context context;
    private List<FundEntity> fundBeens;
    private int point = -1;
    private String[] way;

    public FundProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FundEntity> fundBeens) {
        this.fundBeens = fundBeens;
        notifyDataSetChanged();
    }

    public void setWay(String[] way) {
        this.way = way;
    }

    @Override
    public int getCount() {
        if (fundBeens != null && fundBeens.size() > 0) {
            return fundBeens.size();
        }
        if (way != null && way.length > 0) {
            return way.length;
        }
        return 0;
    }

    public void setPoint(int point) {
        this.point = point;
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fund_subs, null);
            holder.tv_fund = (TextView) convertView.findViewById(R.id.tv_fund);
            holder.iv_choose = (ImageView) convertView.findViewById(R.id.iv_choose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (way == null) {
            holder.tv_fund.setText(fundBeens.get(position).fund_name + "\t\t" + fundBeens.get(position).fund_code);
        } else {
            holder.tv_fund.setText(way[position]);
        }
        if (point == position) {
            holder.iv_choose.setVisibility(View.VISIBLE);
            holder.iv_choose.setImageResource(R.mipmap.duigou);
        } else {
            holder.iv_choose.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        public TextView tv_fund;
        public ImageView iv_choose;
    }
}