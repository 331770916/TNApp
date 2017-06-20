package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.OptionalStockEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class FreeStockAdapter extends BaseAdapter {
    private Context context;
    private List<OptionalStockEntity> optionalStockBeen;

    public FreeStockAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置自选股数据
     *
     * @param optionalStockBeen
     */
    public void setOptionalStockBeen(List<OptionalStockEntity> optionalStockBeen) {
        this.optionalStockBeen = optionalStockBeen;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (optionalStockBeen != null && optionalStockBeen.size() > 0) {
            return optionalStockBeen.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction_8text, null);
            holder.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            holder.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            holder.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
            holder.tv_text4 = (TextView) convertView.findViewById(R.id.tv_text4);
            holder.tv_text5 = (TextView) convertView.findViewById(R.id.tv_text5);
            holder.tv_text6 = (TextView) convertView.findViewById(R.id.tv_text6);
            holder.tv_text7 = (TextView) convertView.findViewById(R.id.tv_text7);
            holder.tv_text8 = (TextView) convertView.findViewById(R.id.tv_text8);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_text4.setVisibility(View.GONE);
        holder.tv_text1.setText(optionalStockBeen.get(position).stockName);
        String stockcode = optionalStockBeen.get(position).stockCode;
        String nowPrice = optionalStockBeen.get(position).nowPrice;
        if ("1".equals(stockcode.substring(0, 1))) {
            holder.tv_text2.setText("SH" +stockcode.substring(2));     //SH 1 SZ 2
        } else {
            holder.tv_text2.setText("SZ" + stockcode.substring(2));     //SH 1 SZ 2
        }

        if (Helper.isDecimal(nowPrice)) {
            holder.tv_text3.setText(TransitionUtils.fundPirce(stockcode, nowPrice));
        }
        String ttm = optionalStockBeen.get(position).TTM;
        if (Helper.isDecimal(ttm) && Double.parseDouble(ttm) < 0) {
            holder.tv_text5.setTextColor(ColorUtils.GREEN);
            holder.tv_text6.setTextColor(ColorUtils.GREEN);
        } else if (Helper.isDecimal(ttm) && Double.parseDouble(ttm) > 0) {
            holder.tv_text5.setTextColor(ColorUtils.RED);
            holder.tv_text6.setTextColor(ColorUtils.RED);
        } else if (Helper.isDecimal(ttm) && Double.parseDouble(ttm) == 0) {
            holder.tv_text5.setTextColor(ColorUtils.BLACK);
            holder.tv_text6.setTextColor(ColorUtils.BLACK);
        }
        holder.tv_text5.setText(optionalStockBeen.get(position).TTMR);
        holder.tv_text6.setText(optionalStockBeen.get(position).TTM);
        holder.tv_text7.setTextColor(ColorUtils.RED);
        holder.tv_text8.setTextColor(ColorUtils.GREEN);
        String high = optionalStockBeen.get(position).hign;
        String low = optionalStockBeen.get(position).low;
        String before = optionalStockBeen.get(position).beforePrice;
        if (Helper.isDecimal(high) && Double.valueOf(high) > Double.valueOf(before)) {
            holder.tv_text7.setTextColor(ColorUtils.RED);
        } else if (Helper.isDecimal(high) && Double.valueOf(high) == Double.valueOf(before)) {
            holder.tv_text7.setTextColor(ColorUtils.BLACK);
        } else if (Helper.isDecimal(high) && Double.valueOf(high) < Double.valueOf(before)) {
            holder.tv_text7.setTextColor(ColorUtils.GREEN);
        }
        if (Helper.isDecimal(low) && Double.valueOf(low) > Double.valueOf(before)) {
            holder.tv_text8.setTextColor(ColorUtils.RED);
        } else if (Helper.isDecimal(low) && Double.valueOf(low) == Double.valueOf(before)) {
            holder.tv_text8.setTextColor(ColorUtils.BLACK);
        } else if (Helper.isDecimal(low) && Double.valueOf(low) < Double.valueOf(before)) {
            holder.tv_text8.setTextColor(ColorUtils.GREEN);
        }
        if (Helper.isDecimal(optionalStockBeen.get(position).hign)) {
            holder.tv_text7.setText(TransitionUtils.string2doubleS(optionalStockBeen.get(position).hign));
        }
        if (Helper.isDecimal(optionalStockBeen.get(position).low)) {
            holder.tv_text8.setText(TransitionUtils.string2doubleS(optionalStockBeen.get(position).low));
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tv_text1;
        public TextView tv_text2;
        public TextView tv_text3;
        public TextView tv_text4;
        public TextView tv_text5;
        public TextView tv_text6;
        public TextView tv_text7;
        public TextView tv_text8;
    }
}
