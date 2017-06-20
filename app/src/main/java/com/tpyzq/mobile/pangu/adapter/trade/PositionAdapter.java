package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.PositionEntity;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class PositionAdapter extends BaseAdapter {
    private Context context;
    private List<PositionEntity> datas;

    public PositionAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<PositionEntity> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
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
        holder.tv_text1.setText(datas.get(position).SECU_NAME);     //名称
        holder.tv_text2.setText(datas.get(position).MKT_VAL);       //市值
        String INCOME_AMT = TransitionUtils.string2doubleS3(datas.get(position).INCOME_AMT);
        String PROFIT_RATIO = TransitionUtils.string2doubleS3(datas.get(position).PROFIT_RATIO) + "%";
        if (datas.get(position).INCOME_AMT.contains("-")) {
            holder.tv_text3.setText(INCOME_AMT);    //盈亏
            holder.tv_text4.setText(PROFIT_RATIO);     //比例
            holder.tv_text3.setTextColor(ColorUtils.GREEN);
            holder.tv_text4.setTextColor(ColorUtils.GREEN);
        } else {
            holder.tv_text3.setText("+" + INCOME_AMT);    //盈亏
            holder.tv_text4.setText("+" + PROFIT_RATIO);     //比例
            holder.tv_text3.setTextColor(ColorUtils.RED);
            holder.tv_text4.setTextColor(ColorUtils.RED);
        }
        holder.tv_text5.setText(TransitionUtils.string2doubleS4(datas.get(position).SHARE_QTY));     //持仓
        holder.tv_text6.setText(TransitionUtils.string2doubleS4(datas.get(position).SHARE_AVL));     //可用
        holder.tv_text7.setText(TransitionUtils.string2doubleS3(datas.get(position).MKT_PRICE));     //现价
        holder.tv_text8.setText(TransitionUtils.string2doubleS3(datas.get(position).CURRENT_COST));  //成本
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
