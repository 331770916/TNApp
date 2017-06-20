package com.tpyzq.mobile.pangu.adapter.trade;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.TransStockEntity;
import com.tpyzq.mobile.pangu.util.panguutil.JudgeStockUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 陈新宇 on 2016/9/7.
 */
public class TransStockAdapter extends BaseAdapter {
    private Activity context;
    private List<TransStockEntity> transStockBeen;

    public TransStockAdapter(Activity context, ArrayList<TransStockEntity> transStockBeen) {
        this.context = context;
        this.transStockBeen = transStockBeen;
    }

    @Override
    public int getCount() {
        if (transStockBeen != null && transStockBeen.size() > 0) {
            return transStockBeen.size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_stocklist, null);
            holder.tv_text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.tv_text2 = (TextView) convertView.findViewById(R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TransStockEntity transStockBean = transStockBeen.get(position);
        String stockName = transStockBean.stockName;
        String stockCode = transStockBean.stockCode;
        stockCode = JudgeStockUtils.getStockMarket(stockCode)+stockCode.substring(2);
        holder.tv_text1.setText(transStockBean.stockName);
        holder.tv_text2.setText(stockCode);
        return convertView;
    }

    class ViewHolder {
        public TextView tv_text1;
        public TextView tv_text2;
    }
}
