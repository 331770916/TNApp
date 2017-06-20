package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;

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
 * Created by zhangwenbo on 2016/10/21.
 */
public class WillIntoAdapter extends BaseAdapter {

    private ArrayList<NewStockEnitiy> mDatas;
    private View.OnClickListener mOnClickListener;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;

    public WillIntoAdapter(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<NewStockEnitiy> datas) {
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
    public int getItemViewType(int position) {

        int itemViewType = -1;
        String type = mDatas.get(position).getAdapterType();
        if ("0".equals(type)) {
            itemViewType = 0;
        } else if ("1".equals(type)) {
            itemViewType = 1;
        }

        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == 0) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstockissuing_item, null);
            } else if (type == 1) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_item_textview, null);
            }
        }

        if (type == 0) {
            settingType1(convertView);
        }  else if (type == 1) {
            settingType2(convertView, position);
        }

        return convertView;
    }

    public void settingType1(View convertView){
        convertView.findViewById(R.id.daishangshiTxte).setOnClickListener(mOnClickListener);
    }

    private void settingType2(View convertView, int position) {
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

        if (!TextUtils.isEmpty(mDatas.get(position).getIsSueNameBbrOnlIne())) {
            name.setText(mDatas.get(position).getIsSueNameBbrOnlIne());
        } else {
            name.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getSecuCode())) {   //证券代码
            number.setText(Helper.getStockNumber(mDatas.get(position).getSecuCode()));
        } else {
            number.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getIssueprICE())) {    //发行价
            textView1.setText(mDatas.get(position).getIssueprICE());
        } else {
            textView1.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getLoTrateonlIne())) {      //中签率
            textView2.setText(mDatas.get(position).getLoTrateonlIne());
        } else {
            textView2.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getlIstaAte())) {       //上市日期
            textView3.setText(mDatas.get(position).getlIstaAte());
        } else {
            textView3.setText("--");
        }

    }
}
