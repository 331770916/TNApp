package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EntrustThreeEntity;
import com.tpyzq.mobile.pangu.data.EntrustTodayEntity;

import java.util.List;


/**
 * Created by 陈新宇 on 2016/10/24.
 * 开放式基金今日委托
 */
public class FundEntrustTodayAdapter extends BaseAdapter {
    private List<EntrustTodayEntity> entrustTodayBeen;
    private List<EntrustThreeEntity> entrustThreeBeen;
    private Context context;

    public FundEntrustTodayAdapter(Context context) {
        this.context = context;
    }

    public void setEntrustTodayBeen(List<EntrustTodayEntity> entrustTodayBeen) {
        this.entrustTodayBeen = entrustTodayBeen;
        notifyDataSetChanged();
    }

    public void setEntrustThreeBean(List<EntrustThreeEntity> entrustThreeBeen) {
        this.entrustThreeBeen = entrustThreeBeen;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (entrustTodayBeen != null && entrustTodayBeen.size() > 0) {
            return entrustTodayBeen.size();
        }
        if (entrustThreeBeen != null && entrustThreeBeen.size() > 0) {
            return entrustThreeBeen.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_transaction_8text, null);
            viewHolder.tv_text1 = (TextView) convertView.findViewById(R.id.tv_text1);
            viewHolder.tv_text2 = (TextView) convertView.findViewById(R.id.tv_text2);
            viewHolder.tv_text3 = (TextView) convertView.findViewById(R.id.tv_text3);
            viewHolder.tv_text4 = (TextView) convertView.findViewById(R.id.tv_text4);
            viewHolder.tv_text5 = (TextView) convertView.findViewById(R.id.tv_text5);
            viewHolder.tv_text6 = (TextView) convertView.findViewById(R.id.tv_text6);
            viewHolder.tv_text7 = (TextView) convertView.findViewById(R.id.tv_text7);
            viewHolder.tv_text8 = (TextView) convertView.findViewById(R.id.tv_text8);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (entrustTodayBeen != null){
            viewHolder.tv_text1.setText(entrustTodayBeen.get(position).FUND_NAME);
            viewHolder.tv_text2.setText(entrustTodayBeen.get(position).FUND_CODE);
            String data = entrustTodayBeen.get(position).ORDER_DATE;
            if (!TextUtils.isEmpty(data)){
                data = data.substring(0,4)+"-"+data.substring(4,6)+"-"+data.substring(6,8);
            }
            viewHolder.tv_text3.setText(data);
            String time = entrustTodayBeen.get(position).ORDER_TIME;
            if (!TextUtils.isEmpty(time) && time.length() == 5){
                time = "0" + time.substring(0,1)+":"+time.substring(1,3)+":"+time.substring(3,5);
            }else {
                time = time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
            }
            viewHolder.tv_text4.setText(time);
            viewHolder.tv_text5.setText(entrustTodayBeen.get(position).TRADE_AMOUNT);
            viewHolder.tv_text6.setVisibility(View.GONE);
            viewHolder.tv_text7.setText(entrustTodayBeen.get(position).BUSINESS_NAME);
            viewHolder.tv_text8.setText(entrustTodayBeen.get(position).STATUS_NAME);
        }else if (entrustThreeBeen != null){
            viewHolder.tv_text1.setText(entrustThreeBeen.get(position).FUND_NAME);
            viewHolder.tv_text2.setText(entrustThreeBeen.get(position).FUND_CODE);
            String data = entrustThreeBeen.get(position).ORDER_DATE;
            if (!TextUtils.isEmpty(data)){
                data = data.substring(0,4)+"-"+data.substring(4,6)+"-"+data.substring(6,8);
            }
            viewHolder.tv_text3.setText(data);
            String time = entrustThreeBeen.get(position).ORDER_TIME;
            if (!TextUtils.isEmpty(time) && time.length() == 5){
                time = "0" + time.substring(0,1)+":"+time.substring(1,3)+":"+time.substring(3,5);
            }else {
                time = time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
            }
            viewHolder.tv_text4.setText(time);
            String ENTRUST_BALANCE = entrustThreeBeen.get(position).ENTRUST_BALANCE;
            String ENTRUST_MONEY = entrustThreeBeen.get(position).ENTRUST_MONEY;
            if (!"0".equals(ENTRUST_BALANCE)) {
                viewHolder.tv_text5.setText(ENTRUST_BALANCE);
            } else if (!"0".equals(ENTRUST_MONEY)) {
                viewHolder.tv_text5.setText(ENTRUST_MONEY);
            } else if ("0".equals(ENTRUST_BALANCE) && "0".equals(ENTRUST_MONEY)) {
                viewHolder.tv_text5.setText(ENTRUST_BALANCE);
            }else {
                viewHolder.tv_text5.setText("--");
            }
            viewHolder.tv_text6.setVisibility(View.GONE);
            viewHolder.tv_text7.setText(entrustThreeBeen.get(position).BUSINESS_NAME);
            viewHolder.tv_text8.setText(entrustThreeBeen.get(position).STATUS_NAME);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_text1;
        TextView tv_text2;
        TextView tv_text3;
        TextView tv_text4;
        TextView tv_text5;
        TextView tv_text6;
        TextView tv_text7;
        TextView tv_text8;
    }
}
