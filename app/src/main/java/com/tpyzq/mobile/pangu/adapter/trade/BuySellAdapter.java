package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class BuySellAdapter extends BaseAdapter {
    private Context context;
    private String[] price, sum;
    private String s;
    private String yesterdayPrice;
    private String stockCode;

    public BuySellAdapter(Context context, String[] price, String[] sum, String s) {
        this.context = context;
        this.price = price;
        this.sum = sum;
        this.s = s;

    }

    public void setYesterdayPrice(String yesterdayPrice,String stockCode) {
        this.yesterdayPrice = yesterdayPrice;
        this.stockCode = stockCode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 5;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_buy_sell, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (s.equals("买")) {
            holder.tv_name.setText(DataUtils.buy[position]);
        } else if (s.equals("卖")){
            holder.tv_name.setText(DataUtils.sell[position]);
        }else if (s.equals("0")){
            holder.tv_name.setText(DataUtils.borrow[position]);
        }else if(s.equals("1")){
            holder.tv_name.setText(DataUtils.loan[position]);
        }
        if (!TextUtils.isEmpty(price[position])) {
            String s_price = price[position];
            if ("- -".equals(s_price) || "0.0".equals(s_price)) {
                holder.tv_price.setText("- -");
                holder.tv_price.setTextColor(ColorUtils.GRAY);
                holder.tv_num.setTextColor(ColorUtils.GRAY);
            } else {
                holder.tv_price.setText(TransitionUtils.fundPirce(stockCode,s_price));
                if (!TextUtils.isEmpty(s_price)) {
                    boolean price_greater = Double.parseDouble(s_price) < Double.parseDouble(yesterdayPrice) ? true : false;
                    boolean price_equal = Double.parseDouble(s_price) == Double.parseDouble(yesterdayPrice) ? true : false;
                    boolean price_less = Double.parseDouble(s_price) > Double.parseDouble(yesterdayPrice) ? true : false;
                    if (price_greater) {
                        holder.tv_price.setTextColor(ColorUtils.GREEN);
                        holder.tv_num.setTextColor(ColorUtils.GREEN);
                    }else if (price_equal){
                        holder.tv_price.setTextColor(ColorUtils.GRAY);
                        holder.tv_num.setTextColor(ColorUtils.GRAY);
                    }else if (price_less){
                        holder.tv_price.setTextColor(ColorUtils.RED);
                        holder.tv_num.setTextColor(ColorUtils.RED);
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(sum[position])) {
            if ("- -".equals(sum[position])) {
                holder.tv_num.setText("- -");
                holder.tv_price.setTextColor(ColorUtils.BLACK);
                holder.tv_num.setTextColor(ColorUtils.BLACK);
            } else {
                String s_num = TransitionUtils.long2string(sum[position]);
                holder.tv_num.setText(s_num);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_price;
        public TextView tv_num;
    }
}
