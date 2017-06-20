package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;

import android.support.v4.content.ContextCompat;
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
 */
public class NewStockListAdapter extends BaseAdapter {

    private ArrayList<NewStockEnitiy> mDatas;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;
    private ViewHodler viewHodler = null;

    public void setDatas (ArrayList<NewStockEnitiy> datas) {
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
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
            return  mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_item_textview, null);
            viewHodler = new ViewHodler();
            viewHodler.name = (TextView) convertView.findViewById(R.id.newStockItem_NameTxetView);
            viewHodler.number = (TextView) convertView.findViewById(R.id.newStockItem_NumTxetView);
            viewHodler.textView1 = (TextView) convertView.findViewById(R.id.newStockMiddletvTxetView1);
            viewHodler.textView2 = (TextView) convertView.findViewById(R.id.newStockMiddletvTxetView2);
            viewHodler.textView3 = (TextView) convertView.findViewById(R.id.newStockRightTxetView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }



        viewHodler.name.setText("--");
        viewHodler.number.setText("--");
        viewHodler.textView1.setText("--");
        viewHodler.textView2.setText("--");
        viewHodler.textView3.setText("--");

        if (!TextUtils.isEmpty(mDatas.get(position).getName())) {                 //名称
            viewHodler.name.setText(mDatas.get(position).getName());
        } else if (!TextUtils.isEmpty(mDatas.get(position).getIsSueNameBbrOnlIne())) {
            viewHodler.name.setText(mDatas.get(position).getIsSueNameBbrOnlIne());
        }


        if (!TextUtils.isEmpty(mDatas.get(position).getNumber())) {               //证券代码
            viewHodler.number.setText(Helper.getStockNumber(mDatas.get(position).getNumber()));
        } else if (!TextUtils.isEmpty(mDatas.get(position).getSecuCode())) {   //证券代码
            viewHodler.number.setText(Helper.getStockNumber(mDatas.get(position).getSecuCode()));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getmTime())) {              //上市日期
            viewHodler.textView1.setText(mDatas.get(position).getmTime());
        } else if (!TextUtils.isEmpty(mDatas.get(position).getIssueprICE())) {    //发行价
            viewHodler.textView1.setText(mDatas.get(position).getIssueprICE());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getNewPrice())) {             //现价

            if (Helper.isDecimal(mDatas.get(position).getNewPrice())) {
                viewHodler.textView2.setText(mFormat1.format(Double.parseDouble(mDatas.get(position).getNewPrice())));
            } else {
                viewHodler.textView2.setText(mDatas.get(position).getNewPrice());
            }

        } else if (!TextUtils.isEmpty(mDatas.get(position).getLoTrateonlIne())) {      //中签率
            viewHodler.textView2.setText(mDatas.get(position).getLoTrateonlIne());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getAmountOfIncrease())) {     //累计涨幅

            if (Helper.isDecimal(mDatas.get(position).getAmountOfIncrease())) {
                double  doubleAmountOfIncrease = Double.parseDouble(mDatas.get(position).getAmountOfIncrease());
                viewHodler. textView3.setText(mFormat2.format(doubleAmountOfIncrease));

                if (doubleAmountOfIncrease > 0) {
                    viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (doubleAmountOfIncrease == 0) {
                    viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else if (doubleAmountOfIncrease < 0) {
                    viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }

            } else {
                if (Helper.isPersent(mDatas.get(position).getAmountOfIncrease())) {

                    Number amountNumberIncrease = Helper.persentToNumber(mDatas.get(position).getAmountOfIncrease());
                    viewHodler.textView3.setText(mDatas.get(position).getAmountOfIncrease());

                    if (amountNumberIncrease.doubleValue() > 0) {
                        viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (amountNumberIncrease.doubleValue() == 0) {
                        viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (amountNumberIncrease.doubleValue() < 0) {
                        viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }

                } else {
                    viewHodler.textView3.setText(mDatas.get(position).getAmountOfIncrease());
                    viewHodler.textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                }



            }

        } else if (!TextUtils.isEmpty(mDatas.get(position).getlIstaAte())) {       //上市日期
            viewHodler.textView3.setText(mDatas.get(position).getlIstaAte());
        }


        if (!TextUtils.isEmpty(mDatas.get(position).getZdz())) {
            double zdz = Double.parseDouble(mDatas.get(position).getZdz());
            if (zdz > 0) {
                viewHodler.textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));

            } else if (zdz == 0) {
                viewHodler.textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            } else if (zdz < 0) {
                viewHodler.textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            }

        }

        return convertView;
    }

    class ViewHodler {
        TextView name;
        TextView number;
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}
