package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.SuccessTransactionEntity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;

/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class SuccessAdapter extends BaseAdapter {
    private Context context;
    List<SuccessTransactionEntity> successTransactionBeans;

    public SuccessAdapter(Context context) {
        this.context = context;
    }

    public void setSuccessTransactionBeans(List<SuccessTransactionEntity> successTransactionBeans) {
        this.successTransactionBeans = successTransactionBeans;
    }

    @Override
    public int getCount() {
        if (successTransactionBeans != null && successTransactionBeans.size()>0){
            return successTransactionBeans.size();
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
        holder.tv_text7.setVisibility(View.GONE);
        holder.tv_text1.setText(successTransactionBeans.get(position).SECU_NAME);
        holder.tv_text2.setText(TransitionUtils.string2doubleS3(successTransactionBeans.get(position).MATCHED_PRICE));
        String year = successTransactionBeans.get(position).TRD_DATE;
        year = year.substring(0,4)+"-"+year.substring(4,6)+"-"+year.substring(6,8);
        holder.tv_text3.setText(year);
        String time = successTransactionBeans.get(position).MATCHED_TIME;
        if (time.length() == 5) {
            time = "0" + time;
        }
        time = time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
        holder.tv_text4.setText(time);
        String state = successTransactionBeans.get(position).ENTRUST_BS;
        if ("1".equals(state)){
            state = "买入";
            holder.tv_text5.setTextColor(Color.RED);
        }else if ("2".equals(state)){
            state = "卖出";
            holder.tv_text5.setTextColor(Color.GREEN);
        }
        holder.tv_text5.setText(state);
        holder.tv_text6.setText((int)(successTransactionBeans.get(position).MATCHED_QTY)+"");
        holder.tv_text8.setText(TransitionUtils.string2doubleDown(successTransactionBeans.get(position).MATCHED_AMT));
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
