package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.FundAccountEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class AccoundSearchAdapter extends BaseAdapter {
    private Context context;
    List<FundAccountEntity> fundAccountBeans;

    public AccoundSearchAdapter(Context context) {
        this.context = context;
    }

    public void setFundAccountBeans(List<FundAccountEntity> fundAccountBeans) {
        this.fundAccountBeans = fundAccountBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fundAccountBeans != null && fundAccountBeans.size() > 0) {
            return fundAccountBeans.size();
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
            holder.ll_4 = (LinearLayout) convertView.findViewById(R.id.ll_4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_text2.setVisibility(View.GONE);
        holder.tv_text4.setVisibility(View.GONE);
        holder.ll_4.setVisibility(View.GONE);
        holder.tv_text1.setTextAppearance(context, R.style.text_base);
        holder.tv_text1.setText(Helper.getMyDateY_M_D(fundAccountBeans.get(position).OPEN_DATE));
        holder.tv_text3.setText(fundAccountBeans.get(position).TRANS_ACCOUNT);
        holder.tv_text5.setText(fundAccountBeans.get(position).COMPANY_NAME);
        holder.tv_text6.setText(fundAccountBeans.get(position).OPEN_DATE);
        holder.tv_text6.setText(fundAccountBeans.get(position).OFHOLDER_STATUS);

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
        public LinearLayout ll_4;
    }
}
