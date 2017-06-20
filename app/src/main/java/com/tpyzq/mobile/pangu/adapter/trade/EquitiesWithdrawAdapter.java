package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EquitiesWithDrawEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 * 委托撤单（委托查询）
 */
public class EquitiesWithdrawAdapter extends BaseAdapter {
    private Context context;
    List<EquitiesWithDrawEntity> equitiesWithdrawBeans;

    public EquitiesWithdrawAdapter(Context context) {
        this.context = context;
    }

    public void setEquitiesWithdrawBeans(List<EquitiesWithDrawEntity> equitiesWithdrawBeans) {
        this.equitiesWithdrawBeans = equitiesWithdrawBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (equitiesWithdrawBeans != null && equitiesWithdrawBeans.size() > 0) {
            return equitiesWithdrawBeans.size();
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
        holder.tv_text1.setText(equitiesWithdrawBeans.get(position).SECU_NAME);
        String time = equitiesWithdrawBeans.get(position).ORDER_TIME;
        holder.tv_text2.setText(Helper.formateDate(time));

        holder.tv_text3.setText(TransitionUtils.string2doubleS3(equitiesWithdrawBeans.get(position).PRICE));
        holder.tv_text4.setText(TransitionUtils.string2doubleS3(equitiesWithdrawBeans.get(position).MATCHED_PRICE));
        holder.tv_text5.setText(TransitionUtils.string2doubleS4(equitiesWithdrawBeans.get(position).QTY));
        holder.tv_text6.setText(TransitionUtils.string2doubleS4(equitiesWithdrawBeans.get(position).MATCHED_QTY));
        switch (equitiesWithdrawBeans.get(position).ENTRUST_STATUS) {
            case "0":
                holder.tv_text7.setText("可撤");
                break;
            case "1":
                holder.tv_text7.setText("待报");
                break;
            case "2":
                holder.tv_text7.setText("已报");
                break;
            case "3":
                holder.tv_text7.setText("已报待撤");
                break;
            case "4":
                holder.tv_text7.setText("部成待撤");
                break;
            case "6":
                holder.tv_text7.setText("已撤");
                break;
            case "7":
                holder.tv_text7.setText("部成");
                break;
            case "8":
                holder.tv_text7.setText("已成");
                break;
            case "9":
                holder.tv_text7.setText("废单");
                break;
        }

        String status = equitiesWithdrawBeans.get(position).ENTRUST_BS;
        if ("1".equals(status)) {
            holder.tv_text8.setText("买");
            holder.tv_text8.setTextColor(ColorUtils.RED);
        } else if ("2".equals(status)) {
            holder.tv_text8.setText("卖");
            holder.tv_text8.setTextColor(ColorUtils.GREEN);
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
