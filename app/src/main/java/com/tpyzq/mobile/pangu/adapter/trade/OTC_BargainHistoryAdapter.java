package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：刘泽鹏 on 2016/9/7 14:02
 */
public class OTC_BargainHistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public OTC_BargainHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size()>0){
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_bargain_query,null);
            viewHolder.tv_OTCBargainQueryStockName = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryStockName);
            viewHolder.tv_OTCBargainQuerystockCode = (TextView) convertView.findViewById(R.id.tv_OTCBargainQuerystockCode);
            viewHolder.tv_OTCBargainQueryData = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryData);
            viewHolder.tv_OTCBargainQueryTime = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryTime);
            viewHolder.tv_OTCBargainQueryBusinessName = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryBusinessName);
            viewHolder.tv_OTCBargainQueryMoney = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryMoney);
            viewHolder.tv_OTCBargainQueryShare = (TextView) convertView.findViewById(R.id.tv_OTCBargainQueryShare);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tv_OTCBargainQueryStockName.setText(map.get("tv_stockName"));
        viewHolder.tv_OTCBargainQuerystockCode.setText(map.get("tv_stockCode"));
        viewHolder.tv_OTCBargainQueryData.setText(map.get("tv_Data"));
        viewHolder.tv_OTCBargainQueryTime.setText(map.get("tv_Time"));
        viewHolder.tv_OTCBargainQueryBusinessName.setText(map.get("tv_businessName"));
        viewHolder.tv_OTCBargainQueryMoney.setText(map.get("tv_businessMoney"));
        viewHolder.tv_OTCBargainQueryShare.setText(map.get("tv_businessShare"));

        return convertView;
    }

    class ViewHolder{
        TextView tv_OTCBargainQueryStockName;           //名称
        TextView tv_OTCBargainQuerystockCode;           //代码
        TextView tv_OTCBargainQueryData;                //日期
        TextView tv_OTCBargainQueryTime;                ///时间
        TextView tv_OTCBargainQueryBusinessName;        //业务名称
        TextView tv_OTCBargainQueryMoney;               //金额
        TextView tv_OTCBargainQueryShare;               //份额
    }
}
