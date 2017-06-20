package com.tpyzq.mobile.pangu.activity.market.quotation;

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
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/20.
 */
public class HQExponentAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mBeans;
    private DecimalFormat mFormat;
    private DecimalFormat mFormat1;

    public HQExponentAdapter() {
        mFormat = new DecimalFormat("#0.00");
        mFormat1 = new DecimalFormat("#0.00%");
    }

    public void setData(ArrayList<StockInfoEntity> beans) {
        mBeans = beans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mBeans != null && mBeans.size() > 0) {
            return mBeans.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mBeans != null && mBeans.size() > 0) {
            return mBeans.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_stocklist, null);
            viewHodler.turnoverName = (TextView) convertView.findViewById(R.id.stockItemName);
            viewHodler.turnoverNumber = (TextView) convertView.findViewById(R.id.stockItemNumber);
            viewHodler.turnoverPrice = (TextView) convertView.findViewById(R.id.stockItemPrice);
            viewHodler.turnoverRate = (TextView) convertView.findViewById(R.id.stockItemRate);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getStockName())) {
            viewHodler.turnoverName.setText(mBeans.get(position).getStockName());
        } else {
            viewHodler.turnoverName.setText("--");
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {

            String _stockNumber = mBeans.get(position).getStockNumber();

            if (_stockNumber.length() > 6) {
                _stockNumber = _stockNumber.substring(2, _stockNumber.length());
            }

            viewHodler.turnoverNumber.setText(_stockNumber);
        } else {
            viewHodler.turnoverNumber.setText("--");
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {

            if (Helper.isDecimal(mBeans.get(position).getNewPrice())
                    || Helper.isENum(mBeans.get(position).getNewPrice())) {
                viewHodler.turnoverPrice.setText(mFormat.format(Double.parseDouble(mBeans.get(position).getNewPrice())));
            } else {
                viewHodler.turnoverPrice.setText(mBeans.get(position).getNewPrice());
            }

        } else {
            viewHodler.turnoverPrice.setText("--");
        }

        if (!TextUtils.isEmpty(""+mBeans.get(position).getPriceChangeRatio())) {
                Double zdf = mBeans.get(position).getPriceChangeRatio();
                viewHodler.turnoverRate.setText(mFormat1.format(zdf));
                if (zdf > 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (zdf == 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else if (zdf < 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }

        } else {
            viewHodler.turnoverRate.setText("--");
            viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
        }

        return convertView;
    }

    private class ViewHodler {
        TextView turnoverName;
        TextView turnoverNumber;
        TextView turnoverPrice;
        TextView turnoverRate;
    }


}
