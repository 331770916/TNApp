package com.tpyzq.mobile.pangu.adapter.detail;

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
 * Created by zhangwenbo on 2016/10/26.
 */
public class ExponentAdapter extends BaseAdapter {


    private ArrayList<StockInfoEntity> mDatas;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;

    public ExponentAdapter() {
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setData(ArrayList<StockInfoEntity> datas) {
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
    public long getItemId(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
           convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.hushen_item, null);
        }

        initBottomView(convertView, position, mDatas);

        return convertView;
    }


    /**
     * 初始化底部布局
     *
     * @param convertView
     * @param position
     */
    private void initBottomView(final View convertView, int position, ArrayList<StockInfoEntity> mBeans) {

        final TextView stockName = (TextView) convertView.findViewById(R.id.hushen_stockName);
        TextView stockNumber = (TextView) convertView.findViewById(R.id.hushen_stockNumber);
        TextView currentPrice = (TextView) convertView.findViewById(R.id.hushen_stockCurrentPrice);
        TextView stockZdf = (TextView) convertView.findViewById(R.id.hushen_stockZD);

        //解决错乱问题
        stockName.setText(null);
        stockNumber.setText(null);
        currentPrice.setText(null);
        stockZdf.setText(null);

        if (!TextUtils.isEmpty(mBeans.get(position).getStockName())) {
            stockName.setText(mBeans.get(position).getStockName());
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {

            String _stockNumber = Helper.getStockNumber(mBeans.get(position).getStockNumber());
            stockNumber.setText(_stockNumber);
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {
            if ("-".equals(mBeans.get(position).getNewPrice())) {
                currentPrice.setText(mBeans.get(position).getNewPrice());
            } else {

//                TransitionUtils.

                currentPrice.setText(mFormat1.format(Double.parseDouble(mBeans.get(position).getNewPrice())));

//                if (!TextUtils.isEmpty(mBeans.get(position).getPriceChangeRatio())) {
                    double zdf = mBeans.get(position).getPriceChangeRatio();

                    if (zdf > 0f) {
                        currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (zdf == 0f) {
                        currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
                    } else {
                        currentPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }
//                }

            }
        }


        if (!TextUtils.isEmpty(mBeans.get(position).getType()) && "3000".equals(mBeans.get(position).getType())) {

            double zdf = Double.parseDouble(mBeans.get(position).getTurnover());
            stockZdf.setText(mFormat2.format(zdf));

            stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));

        } else if (!TextUtils.isEmpty(mBeans.get(position).getType()) && "2000".equals(mBeans.get(position).getType())
                || !TextUtils.isEmpty(mBeans.get(position).getType()) && "1000".equals(mBeans.get(position).getType())) {

            double zdf = mBeans.get(position).getPriceChangeRatio();
            stockZdf.setText(mFormat2.format(zdf));

            if (zdf > 0f) {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            } else if (zdf == 0f) {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
            } else {
                stockZdf.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            }

        }

    }
}
