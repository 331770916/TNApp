package com.tpyzq.mobile.pangu.adapter.trade;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/15.
 *  新股日历Adapter
 */
public class CalendarNewStockAdapter extends BaseAdapter {

    private ArrayList<NewStockEnitiy.DataBeanToday> mDatas;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;

    public CalendarNewStockAdapter() {
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<NewStockEnitiy.DataBeanToday> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public int getCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_item_textview, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.newStockItem_NameTxetView);
        TextView number = (TextView) convertView.findViewById(R.id.newStockItem_NumTxetView);
        TextView textView1 = (TextView) convertView.findViewById(R.id.newStockMiddletvTxetView1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.newStockMiddletvTxetView2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.newStockRightTxetView);

        name.setText("--");
        number.setText("--");
        textView1.setText("--");
        textView2.setText("--");
        textView3.setText("--");

        if (!TextUtils.isEmpty(mDatas.get(position).getISSUENAMEABBR_ONLINE())) {
            name.setText(mDatas.get(position).getISSUENAMEABBR_ONLINE());
        }


        if (!TextUtils.isEmpty(mDatas.get(position).getSECUCODE())) {
            number.setText(mDatas.get(position).getAPPLYCODEONLINE());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getISSUEPRICE())) {
            textView1.setText(mFormat1.format(Double.parseDouble(mDatas.get(position).getISSUEPRICE())));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getDILUTEDPERATIO())) {
            textView2.setText(mFormat1.format(Double.parseDouble(mDatas.get(position).getDILUTEDPERATIO())));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getAPPLYMAXONLINEMONEY())) {
            textView3.setText( Helper.long2million(mDatas.get(position).getAPPLYMAXONLINEMONEY()));
        }

        return convertView;
    }
}
