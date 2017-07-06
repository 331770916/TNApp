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
 * Created by 刘泽鹏 on 2016/8/12.
 */
public class CurrencyFundRevokeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String,String>> list;

    public CurrencyFundRevokeAdapter(Context context) {
        this.context=context;
    }

    public void setList(ArrayList<HashMap<String,String>> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null && list.size() > 0 ){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null && list.size() > 0 ){
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
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_currency_fund_revoke,null);
            viewHolder.tvGuPiaoMingCheng= (TextView) convertView.findViewById(R.id.tvGuPiaoMingCheng);
            viewHolder.tvGuPiaoDaiMa= (TextView) convertView.findViewById(R.id.tvGuPiaoDaiMa);
            viewHolder.tvRiqi= (TextView) convertView.findViewById(R.id.tvRiqi);
            viewHolder.tvShiJian= (TextView) convertView.findViewById(R.id.tvShiJian);
            viewHolder.tvShuLiang= (TextView) convertView.findViewById(R.id.tvShuLiang);
            viewHolder.tvJinE= (TextView) convertView.findViewById(R.id.tvJinE);
            viewHolder.tvLeiXing= (TextView) convertView.findViewById(R.id.tvLeiXing);
            viewHolder.tvZhuangTai= (TextView) convertView.findViewById(R.id.tvZhuangTai);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        viewHolder.tvGuPiaoMingCheng.setText(map.get("tv_stockName"));
        viewHolder.tvGuPiaoDaiMa.setText(map.get("tv_stockCode"));
        viewHolder.tvRiqi.setText(map.get("tv_Data"));
        viewHolder.tvShiJian.setText(map.get("tv_Time"));
        viewHolder.tvShuLiang.setText(map.get("tv_EntrustNumber"));
        viewHolder.tvJinE.setText(map.get("tv_EntrustMoney"));
        viewHolder.tvLeiXing.setText(map.get("tv_type"));
        viewHolder.tvZhuangTai.setText(map.get("tv_state"));

        return convertView;
    }

    private class ViewHolder{
        TextView tvGuPiaoMingCheng;
        TextView tvGuPiaoDaiMa;
        TextView tvRiqi;
        TextView tvShiJian;
        TextView tvShuLiang;
        TextView tvJinE;
        TextView tvLeiXing;
        TextView tvZhuangTai;
    }
}
