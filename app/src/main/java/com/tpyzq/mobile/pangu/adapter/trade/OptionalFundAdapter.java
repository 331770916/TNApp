package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundSubsEntity;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/9/7.
 */
public class OptionalFundAdapter extends BaseAdapter {
    private Context context;
    List<FundSubsEntity> data;

    public void setData(List<FundSubsEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public OptionalFundAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (data != null && data.size() > 0) {
            return data.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction_8text_2, null);
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

        holder.tv_text5.setVisibility(View.GONE);
        holder.tv_text1.setText(data.get(position).FUND_NAME);
        holder.tv_text2.setText(data.get(position).FUND_CODE);
        holder.tv_text3.setText(data.get(position).FUND_VAL);
        holder.tv_text4.setText(data.get(position).OPEN_SHARE);
        holder.tv_text6.setText(data.get(position).OFUND_RISKLEVEL_NAME);
        holder.tv_text7.setText(data.get(position).FUND_COMPANY_NAME);
        String text8 = "";
        switch (data.get(position).STATUS) {
            case "0":
                text8 = "正常开放";
                break;
            case "1":
                text8 = "认购时期";
                break;
            case "2":
                text8 = "停止赎回";
                break;
            case "3":
                text8 = "停止申购";
                break;
            case "4":
                text8 = "封闭期";
                break;
            case "5":
                text8 = "停止交易";
                break;
            case "6":
                text8 = "基金终止";
                break;
            case "7":
                text8 = "权益登记";
                break;
            case "8":
                text8 = "红利发放";
                break;
            default:
                break;
        }
        holder.tv_text8.setText(text8);


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
