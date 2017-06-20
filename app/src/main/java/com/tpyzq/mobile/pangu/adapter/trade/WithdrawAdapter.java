package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.WithDrawEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class WithdrawAdapter extends BaseAdapter {
    private Context context;
    List<WithDrawEntity> withDrawBeens;
    public WithdrawAdapter(Context context) {
        this.context = context;
    }
    public void setWithDrawBeens(List<WithDrawEntity> withDrawBeens) {
        this.withDrawBeens = withDrawBeens;
    }

    @Override
    public int getCount() {
        if(withDrawBeens != null && withDrawBeens.size()>0){
            return withDrawBeens.size();
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
        holder.tv_text1.setText(withDrawBeens.get(position).FUND_NAME);
        holder.tv_text2.setText(withDrawBeens.get(position).FUND_CODE);
        String data = withDrawBeens.get(position).ORDER_DATE;
        String time = withDrawBeens.get(position).ORDER_TIME;
        holder.tv_text3.setText(Helper.getMyDateY_M_D(data));
        holder.tv_text4.setText(Helper.getMyDateHMS(time));
        holder.tv_text5.setText(withDrawBeens.get(position).TRADE_AMOUNT);
//        holder.tv_text6.setText(withDrawBeens.get(position).ENTRUST_MONEY);
        holder.tv_text6.setVisibility(View.GONE);

        holder.tv_text7.setText(withDrawBeens.get(position).BUSINESS_NAME);

        holder.tv_text8.setText(withDrawBeens.get(position).STATUS_NAME);

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
