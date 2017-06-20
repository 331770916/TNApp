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
 * 作者：刘泽鹏 on 2016/9/6 19:56
 */
public class OTC_EntrustTodayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public OTC_EntrustTodayAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list !=null && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list !=null && list.size()>0){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_otc_entrust_today,null);
            viewHolder.tv_OTCEntrustTodayStockName = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayStockName);
            viewHolder.tv_OTCEntrustTodaystockCode = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodaystockCode);
            viewHolder.tv_OTCEntrustTodayData = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayData);
            viewHolder.tv_OTCEntrustTodayTime = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayTime);
            viewHolder.tv_OTCEntrustTodayMoney = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayMoney);
            viewHolder.tv_OTCEntrustTodayNumber = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayNumber);
            viewHolder.tv_OTCEntrustTodayType = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayType);
            viewHolder.tv_OTCEntrustTodayState = (TextView) convertView.findViewById(R.id.tv_OTCEntrustTodayState);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tv_OTCEntrustTodayStockName.setText(map.get("tv_stockName"));
        viewHolder.tv_OTCEntrustTodaystockCode.setText(map.get("tv_stockCode"));
        viewHolder.tv_OTCEntrustTodayData.setText(map.get("tv_Data"));
        viewHolder.tv_OTCEntrustTodayTime.setText(map.get("tv_Time"));
        viewHolder.tv_OTCEntrustTodayMoney.setText(map.get("tv_EntrustMoney"));
        viewHolder.tv_OTCEntrustTodayNumber.setText(map.get("tv_EntrustNumber"));
        viewHolder.tv_OTCEntrustTodayType.setText(map.get("tv_type"));
        viewHolder.tv_OTCEntrustTodayState.setText(map.get("tv_state"));

        return convertView;
    }

    class ViewHolder{
        TextView tv_OTCEntrustTodayStockName;       //名称
        TextView tv_OTCEntrustTodaystockCode;       //代码
        TextView tv_OTCEntrustTodayData;            //日期
        TextView tv_OTCEntrustTodayTime;            //时间
        TextView tv_OTCEntrustTodayMoney;           //金额
        TextView tv_OTCEntrustTodayNumber;          //份额
        TextView tv_OTCEntrustTodayType;            //类型
        TextView tv_OTCEntrustTodayState;           //状态
    }

}
