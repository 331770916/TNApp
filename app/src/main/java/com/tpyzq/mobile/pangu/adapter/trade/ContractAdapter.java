package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.ContractEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class ContractAdapter extends BaseAdapter {
    private Context context;
    List<ContractEntity> contractBeen;

    public ContractAdapter(Context context) {
        this.context = context;
    }

    public void setContractBeans(List<ContractEntity> contractBeen) {
        this.contractBeen = contractBeen;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (contractBeen != null && contractBeen.size() > 0) {
            return contractBeen.size();
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
        holder.ll_4.setVisibility(View.GONE);
        holder.tv_text1.setText(contractBeen.get(position).FUND_NAME);
        holder.tv_text3.setText(contractBeen.get(position).FUND_CODE);
        switch (contractBeen.get(position).IS_SIGN){
            case "0":
                holder.tv_text4.setText("已签约");
                holder.tv_text4.setTextColor(ColorUtils.WATHET);
                break;
            case "1":
                holder.tv_text4.setText("未签约");
                holder.tv_text4.setTextColor(ColorUtils.ORANGE);
                break;
        }
        holder.tv_text5.setText(contractBeen.get(position).FUND_COMPANY_NAME);
        holder.tv_text6.setText(contractBeen.get(position).FUND_COMPANY);
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
