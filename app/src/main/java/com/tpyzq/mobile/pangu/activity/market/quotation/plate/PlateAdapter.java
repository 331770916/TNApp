package com.tpyzq.mobile.pangu.activity.market.quotation.plate;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/20.
 * 板块Adapter
 */
public class PlateAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mDatas;
    private DecimalFormat mFormat1;

    public PlateAdapter () {
        mFormat1 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<StockInfoEntity> datas) {
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

        String type = mDatas.get(position).getType();
        int itemViewType = -1;
        if (!TextUtils.isEmpty(type) && type.equals("1")) {
            itemViewType = 0;
        } else if (!TextUtils.isEmpty(type) && type.equals("2")) {
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
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.hushen_item1, null);
            }

            if (type == 1) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.hushen_item2, null);
            }
        }

        if (type == 0) {
            TextView titleView = (TextView) convertView.findViewById(R.id.hushen_category_tv);
            titleView.setText(mDatas.get(position).getTitleTv());
        } else if (type == 1) {
            initListView(convertView, position);
        }

        return convertView;
    }

    private void initListView(View convertView, int position) {
        TextView industryName = (TextView) convertView.findViewById(R.id.hushen_stockName);
        TextView industryNumber = (TextView) convertView.findViewById(R.id.hushen_stockNumber);
        TextView price = (TextView) convertView.findViewById(R.id.hushen_stockCurrentPrice);
        TextView industryZdf = (TextView) convertView.findViewById(R.id.hushen_stockZD);

        industryName.setText("--");
        industryNumber.setText("--");
        price.setText(null);
        industryZdf.setText("--");

        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryName())) {
            industryName.setText(mDatas.get(position).getIndustryName());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryNumber())) {

            String _industryNumber = mDatas.get(position).getIndustryNumber();

            if (_industryNumber.length() > 6) {
                _industryNumber = _industryNumber.substring(2, _industryNumber.length());
            }

            industryNumber.setText(_industryNumber);
        }


        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryUpAndDown())) {

            String industryUpAndDown = mDatas.get(position).getIndustryUpAndDown();

            if (!"-".equals(industryUpAndDown)) {
                industryZdf.setText(mFormat1.format(Double.parseDouble(industryUpAndDown)));
            } else {
                industryZdf.setText(industryUpAndDown);
            }

            Double _temData = Double.parseDouble(industryUpAndDown);

            if (_temData > 0) {
                industryZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            } else if (_temData < 0) {
                industryZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            } else if (_temData == 0) {
                industryZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            }

        }
    }
}
