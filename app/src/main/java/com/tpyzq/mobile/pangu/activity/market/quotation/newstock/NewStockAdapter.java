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
 * 新股日历Adapter
 */
public class NewStockAdapter extends BaseAdapter {

    private ArrayList<NewStockEnitiy> mDatas;
    private View.OnClickListener mOnClickListener;
    private DecimalFormat mFormat2;

    public NewStockAdapter(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if (convertView == null) {
            if (type == 0) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstockmarket_item, null);
            } else if (type == 1) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_item_textview, null);
            }
        }

        if (type == 0) {
            settingType1(convertView);
        } else if (type == 1) {
            settingType2(convertView, position);
        }

        return convertView;
    }

    public void settingType1(View convertView){
        convertView.findViewById(R.id.XinGuText).setOnClickListener(mOnClickListener);
    }

    /**
     * 加载第四个布局文件
     * @param convertView
     * @param position
     */
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

        if (!TextUtils.isEmpty(mDatas.get(position).getName())) {                 //名称
            name.setText(mDatas.get(position).getName());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getNumber())) {               //证券代码
            number.setText(Helper.getStockNumber(mDatas.get(position).getNumber()));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getmTime())) {              //上市日期
            textView1.setText(mDatas.get(position).getmTime());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getNewPrice())) {             //现价

            textView2.setText(mDatas.get(position).getNewPrice());
        } else {
            textView2.setText("-");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getAmountOfIncrease())) {     //累计涨幅
            if (Helper.isDecimal(mDatas.get(position).getAmountOfIncrease())) {
                double doubleAmountOfIncrease = Double.parseDouble(mDatas.get(position).getAmountOfIncrease());
                textView3.setText(mFormat2.format(doubleAmountOfIncrease));

                if (doubleAmountOfIncrease > 0) {
                    textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (doubleAmountOfIncrease == 0) {
                    textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else if (doubleAmountOfIncrease < 0) {
                    textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }

            } else {
                if (Helper.isPersent(mDatas.get(position).getAmountOfIncrease())) {

                    Number amountNumberIncrease = Helper.persentToNumber(mDatas.get(position).getAmountOfIncrease());
                    textView3.setText(mDatas.get(position).getAmountOfIncrease());

                    if (amountNumberIncrease.doubleValue() > 0) {
                        textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (amountNumberIncrease.doubleValue() == 0) {
                        textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (amountNumberIncrease.doubleValue() < 0) {
                        textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }

                } else {
                    textView3.setText(mDatas.get(position).getAmountOfIncrease());
                    textView3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                }
            }
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getZdz())) {
            double zdz = Double.parseDouble(mDatas.get(position).getZdz());

            if (zdz > 0) {
                textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            } else if (zdz == 0) {
                textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            } else if (zdz < 0) {
                textView2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            }

        }
    }
}
