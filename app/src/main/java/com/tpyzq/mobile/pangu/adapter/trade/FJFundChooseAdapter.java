package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.List;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金选择基金
 */

public class FJFundChooseAdapter extends BaseAdapter {
    private Context mContext;
    private List<StructuredFundEntity> mList;
    private int mPoint;

    public FJFundChooseAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<StructuredFundEntity> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setPoint(int point) {
        this.mPoint = point;
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && mList.size() > 0) {
            return mList.get(position);
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fund_list, null);
            viewHolder.stockCode = (TextView) convertView.findViewById(R.id.tv_1);
            viewHolder.stockName = (TextView) convertView.findViewById(R.id.tv_2);
            viewHolder.ivDuiGou = (ImageView) convertView.findViewById(R.id.iv_3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.stockCode.setText(mList.get(position).getStocken_code() + "");
        viewHolder.stockName.setText(mList.get(position).getStoken_name()+"");

        if (mPoint == position) {
            viewHolder.ivDuiGou.setVisibility(View.VISIBLE);
            viewHolder.ivDuiGou.setImageResource(R.mipmap.duigou);
        } else {
            viewHolder.ivDuiGou.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        TextView stockCode;
        TextView stockName;
        ImageView ivDuiGou;
    }
}
