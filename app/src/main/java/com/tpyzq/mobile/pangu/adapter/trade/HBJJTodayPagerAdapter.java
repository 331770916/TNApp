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
 * 作者：刘泽鹏 on 2016/8/23 16:32
 * 货币基金委托查询    今日界面的适配器
 */
public class HBJJTodayPagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public HBJJTodayPagerAdapter(Context context) {
        this.context=context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if( list != null && list.size() > 0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if( list != null && list.size() > 0 ){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_currencyfund_entrustquery,null);
            viewHolder.tv_stockName= (TextView) convertView.findViewById(R.id.tv_HBJJWTstockName);
            viewHolder.tv_stockCode= (TextView) convertView.findViewById(R.id.tv_HBJJWTstockCode);
            viewHolder.tv_Data= (TextView) convertView.findViewById(R.id.tv_HBJJWTData);
            viewHolder.tv_Time= (TextView) convertView.findViewById(R.id.tv_HBJJWTTime);
            viewHolder.tv_EntrustNumber= (TextView) convertView.findViewById(R.id.tv_HBJJWTEntrustNumber);
            viewHolder.tv_EntrustMoney= (TextView) convertView.findViewById(R.id.tv_HBJJWTEntrustMoney);
            viewHolder.tv_type= (TextView) convertView.findViewById(R.id.tv_HBJJWTtype);
            viewHolder.tv_state= (TextView) convertView.findViewById(R.id.tv_HBJJWTstate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tv_stockName.setText(map.get("tv_stockName"));
        viewHolder.tv_stockCode.setText(map.get("tv_stockCode"));
        viewHolder.tv_Data.setText(map.get("tv_Data"));
        viewHolder.tv_Time.setText(map.get("tv_Time"));
        viewHolder.tv_EntrustNumber.setText(map.get("tv_EntrustNumber"));
        viewHolder.tv_EntrustMoney.setText(map.get("tv_EntrustMoney"));
        viewHolder.tv_type.setText(map.get("tv_type"));
        viewHolder.tv_state.setText(map.get("tv_state"));

        return convertView;
    }

    private class ViewHolder{
        TextView tv_stockName;
        TextView tv_stockCode;
        TextView tv_Data;
        TextView tv_Time;
        TextView tv_EntrustNumber;
        TextView tv_EntrustMoney;
        TextView tv_type;
        TextView tv_state;
    }

}
