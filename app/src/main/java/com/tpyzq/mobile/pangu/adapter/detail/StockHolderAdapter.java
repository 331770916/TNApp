package com.tpyzq.mobile.pangu.adapter.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StockHolderTop10Entity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 * 股票股东列表
 */
public class StockHolderAdapter extends BaseAdapter {
    private Context context;
    List<StockHolderTop10Entity> stockHolderTop10Beens;

    public StockHolderAdapter(Context context) {
        this.context = context;
    }

    public void setStockHolderTop10Beans(List<StockHolderTop10Entity> stockHolderTop10Beens) {
        this.stockHolderTop10Beens = stockHolderTop10Beens;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (stockHolderTop10Beens != null && stockHolderTop10Beens.size()>0){
            return stockHolderTop10Beens.size();
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stockholder_top10, null);
            holder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            holder.tv_change = (TextView) convertView.findViewById(R.id.tv_change);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_company_name.setText(stockHolderTop10Beens.get(position).SHLIST);
        holder.tv_rate.setText(TransitionUtils.string2doubleS(stockHolderTop10Beens.get(position).PCTOFTOTALSHARES)+"%");
        holder.tv_change.setText(TransitionUtils.string2doubleS(stockHolderTop10Beens.get(position).HOLDSUMCHANGERATE)+"%");


        return convertView;
    }

    class ViewHolder {
        public TextView tv_company_name;
        public TextView tv_rate;
        public TextView tv_change;
    }
}
