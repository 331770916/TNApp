package com.tpyzq.mobile.pangu.adapter.trade;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/29.
 * 转账记录查询Adapter
 */
public class QueryTransferAdapter extends BaseAdapter {

    private ArrayList<BankAccountEntity> mDatas;
    private SimpleDateFormat mSimpleDateFormat;
    private SimpleDateFormat sdf;

    public QueryTransferAdapter() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf =  new SimpleDateFormat("yyyyMMdd HHmmss");
    }

    public void setDatas(ArrayList<BankAccountEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.query_transfer_item, null);
            viewHolder.businessTv = (TextView) convertView.findViewById(R.id.queryTransferTv1);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.queryTransferTv2);
            viewHolder.priceTv = (TextView) convertView.findViewById(R.id.queryTransferTv3);
            viewHolder.bankTv = (TextView) convertView.findViewById(R.id.queryTransferTv4);
            viewHolder.resultTv = (TextView) convertView.findViewById(R.id.queryTransferTv5);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.businessTv.setText(null);
        viewHolder.dateTv.setText(null);
        viewHolder.priceTv.setText(null);
        viewHolder.bankTv.setText(null);
        viewHolder.resultTv.setText(null);

        viewHolder.businessTv.setText(mDatas.get(position).getBIZ_NAME());

        //张文博逻辑
//        try {
//            Date date = sdf.parse("" + mDatas.get(position).getTRD_DATE()  + " " + mDatas.get(position).getTRAN_TIME());
//            viewHolder.dateTv.setText(mSimpleDateFormat.format(date));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        //王奇改逻辑
        if (!TextUtils.isEmpty(mDatas.get(position).getTRD_DATE())&&!TextUtils.isEmpty(mDatas.get(position).getTRAN_TIME())){
            viewHolder.dateTv.setText(Helper.getMyDateY_M_D(mDatas.get(position).getTRD_DATE())+" "+Helper.getMyDateHMS(mDatas.get(position).getTRAN_TIME()));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getCPTL_AMT())) {
            viewHolder.priceTv.setText(mDatas.get(position).getCPTL_AMT());
        }

        String account = "";
        if (!TextUtils.isEmpty(mDatas.get(position).getEXT_ACC())) {
            account = Helper.getBanksAccountNumber(mDatas.get(position).getEXT_ACC());
        }
        viewHolder.bankTv.setText(mDatas.get(position).getBANK_NAME()+"(" + account +")");
        viewHolder.bankTv.setTextColor(Color.parseColor("#000000"));
        String status = mDatas.get(position).getSTATUS_NAME();
        viewHolder.resultTv.setText(status);

        return convertView;
    }


    private class ViewHolder {
        TextView businessTv;
        TextView dateTv;
        TextView priceTv;
        TextView bankTv;
        TextView resultTv;
    }
}
