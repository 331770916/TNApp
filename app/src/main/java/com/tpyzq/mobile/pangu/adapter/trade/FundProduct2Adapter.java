package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundChangeEntity;

import java.util.List;


/**
 * Created by zhangwenbo on 2016/8/12.
 * 基金转换产品的列表的Adapter
 */
public class FundProduct2Adapter extends BaseAdapter {
    private Context context;
    private List<FundChangeEntity> fundChangeBeen;
    private int point = -1;
    private String[] way;

    public FundProduct2Adapter(Context context) {
        this.context = context;
    }

    public void setData(List<FundChangeEntity> fundChangeBeen) {
        this.fundChangeBeen = fundChangeBeen;
    }

    public void setWay(String[] way) {
        this.way = way;
    }

    @Override
    public int getCount() {
        if (fundChangeBeen != null && fundChangeBeen.size() > 0) {
            return fundChangeBeen.size();
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
            holder.tv_fund.setText(fundChangeBeen.get(position).FUND_NAME + "\t\t" + fundChangeBeen.get(position).FUND_CODE);
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