package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListEntity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.ArrayList;


/**
 * 作者：刘泽鹏 on 2016/8/29 19:25
 */
public class OTC_ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OTC_SubscriptionListEntity> list;
    private int point = -1;

    public OTC_ProductAdapter(Context context) {
        this.context = context;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setList(ArrayList<OTC_SubscriptionListEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_product, null);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.OTC_ProductListName);
            viewHolder.productCode = (TextView) convertView.findViewById(R.id.OTC_ProductListCode);
            viewHolder.ivDuiGou = (ImageView) convertView.findViewById(R.id.ivDuiGou);
            viewHolder.Xian = convertView.findViewById(R.id.mXian);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OTC_SubscriptionListEntity listIntent = list.get(position);
        viewHolder.productName.setText(listIntent.getStockName());
        viewHolder.productCode.setText("(" + listIntent.getStockCode() + ")");
        if (listIntent.isFlag()) {
            viewHolder.ivDuiGou.setBackgroundResource(R.mipmap.duigou);
        }

        if (position == point) {
            viewHolder.ivDuiGou.setBackgroundResource(R.mipmap.duigou);
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.Xian.getLayoutParams());
        lp.setMargins(TransitionUtils.dp2px(20, context), 0, 0, 0);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(viewHolder.Xian.getLayoutParams());
        lp2.setMargins(0, 0, 0, 0);
        if (position == list.size() - 1) {
            viewHolder.Xian.setLayoutParams(lp2);
        } else {
            viewHolder.Xian.setLayoutParams(lp);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView productName;
        TextView productCode;
        View Xian;
        ImageView ivDuiGou;
    }

}
