package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 作者：刘泽鹏 on 2016/11/8 16:35
 */
public class NewStockSubAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NewStockEnitiy> list;
    private DecimalFormat mFormat1;

    public NewStockSubAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<NewStockEnitiy> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.new_stock_subscribe_listview,null);
            viewHolder.tvNewStockSubName = (TextView) convertView.findViewById(R.id.tvNewStockSubName);
            viewHolder.tvNewStockSubCode = (TextView) convertView.findViewById(R.id.tvNewStockSubCode);
            viewHolder.tvNewStockSubPrice = (TextView) convertView.findViewById(R.id.tvNewStockSubPrice);
            viewHolder.tvNewStockSubPercent = (TextView) convertView.findViewById(R.id.tvNewStockSubPercent);
            viewHolder.tvNewStockSubNameValue = (TextView) convertView.findViewById(R.id.tvNewStockSubNameValue);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mFormat1 = new DecimalFormat("#0.000");
        NewStockEnitiy enitiy = list.get(position);
        String isToday = enitiy.getIsToday();
        viewHolder.tvNewStockSubName.setText(enitiy.getName());
        viewHolder.tvNewStockSubCode.setText(enitiy.getNumber());
        viewHolder.tvNewStockSubPrice.setText(mFormat1.format(Double.parseDouble(enitiy.getIsSuepRice())));
        viewHolder.tvNewStockSubPercent.setText(mFormat1.format(Double.parseDouble(enitiy.getWeIghtedPeraioO())));
        viewHolder.tvNewStockSubNameValue.setText(Helper.long2million(enitiy.getAppLymaxonlIneMoney()));

        return convertView;
    }

    class ViewHolder{
        TextView tvNewStockSubName;             //名称
        TextView tvNewStockSubCode;             //代码
        TextView tvNewStockSubPrice;            //发行价
        TextView tvNewStockSubPercent;          //市盈率
        TextView tvNewStockSubNameValue;        //市值
    }
}
