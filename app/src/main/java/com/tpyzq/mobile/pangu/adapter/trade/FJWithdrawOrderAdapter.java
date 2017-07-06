package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.List;

/**
 * 分级基金撤单Adapter
 */

public class FJWithdrawOrderAdapter extends BaseAdapter {
    private List<StructuredFundEntity> mList;
    private LayoutInflater layoutInflater;

    public FJWithdrawOrderAdapter(Context context, List<StructuredFundEntity> list) {
        this.mList = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_fj_entrusted_query, parent, false);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(mList.get(position).getStoken_name());
        viewHolder.tv2.setText(mList.get(position).getStocken_code());
        viewHolder.tv4.setText(mList.get(position).getBusiness_name());
        viewHolder.tv5.setText(Helper.formateDate1(mList.get(position).getCurr_date()));
        viewHolder.tv6.setText(Helper.getMyDateHMS(mList.get(position).getReport_time()));
        String entrust_bs = mList.get(position).getEntrust_status();
        switch (entrust_bs) {
            case "0":
                viewHolder.tv3.setText("未报");
                break;
            case "1":
                viewHolder.tv3.setText("待报");
                break;
            case "2":
                viewHolder.tv3.setText("已报");
                break;
            case "3":
                viewHolder.tv3.setText("已报待撤");
                break;
            case "4":
                viewHolder.tv3.setText("部成待撤");
                break;
            case "5":
                viewHolder.tv3.setText("部撤");
                break;
            case "6":
                viewHolder.tv3.setText("已撤");
                break;
            case "7":
                viewHolder.tv3.setText("部成");
                break;
            case "8":
                viewHolder.tv3.setText("已成");
                break;
            case "9":
                viewHolder.tv3.setText("废单");
                break;
            case "A":
                viewHolder.tv3.setText("已报待改(港股)");
                break;
            case "B":
                viewHolder.tv3.setText("预埋单撤单(港股)");
                break;
            case "C":
                viewHolder.tv3.setText("正报");
                break;
            case "D":
                viewHolder.tv3.setText("撤废");
                break;
            case "E":
                viewHolder.tv3.setText("部成待改(港股)");
                break;
            case "F":
                viewHolder.tv3.setText("预埋单废单(港股)");
                break;
            case "G":
                viewHolder.tv3.setText("单腿成交");
                break;
            case "H":
                viewHolder.tv3.setText("待审核(港股)");
                break;
            case "J":
                viewHolder.tv3.setText("复核未通过(港股)");
                break;
            case "M":
                viewHolder.tv3.setText("Wait for Confirming");
                break;
            case "U":
                viewHolder.tv3.setText("已确认待撤");
                break;
            case "V":
                viewHolder.tv3.setText("已确认");
                break;
            case "W":
                viewHolder.tv3.setText("待确认");
                break;
            case "X":
                viewHolder.tv3.setText("预成交");
                break;
            case "Y":
                viewHolder.tv3.setText("购回待确认");
                break;
            case "Z":
                viewHolder.tv3.setText("已购回");
                break;
            default:
                viewHolder.tv3.setText("--");
                break;
        }


        return convertView;
    }


    class ViewHolder {
        TextView tv1, tv2, tv3, tv4, tv6, tv5;
    }
}
