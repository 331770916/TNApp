package com.tpyzq.mobile.pangu.activity.detail.exponetTab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 2017/8/10.
 */

public class ExpontentAdapter extends RecyclerView.Adapter {

    private ArrayList<StockInfoEntity> mBeans;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;

    private Context mContext;
    public ExpontentAdapter(Context context) {
        mContext = context;
        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<StockInfoEntity> beans) {
        mBeans = beans;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (mBeans != null && mBeans.size() > 0) {
            return mBeans.size();
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        if (!TextUtils.isEmpty(mBeans.get(position).getStockName())) {
            viewHolder.mName.setText(mBeans.get(position).getStockName());
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getStockNumber())) {

            String _stockNumber = Helper.getStockNumber(mBeans.get(position).getStockNumber());
            viewHolder.mCode.setText(_stockNumber);
        }

        if (!TextUtils.isEmpty(mBeans.get(position).getNewPrice())) {
            if ("-".equals(mBeans.get(position).getNewPrice())) {
                viewHolder.mPrice.setText(mBeans.get(position).getNewPrice());
            } else {

//                TransitionUtils.

                viewHolder.mPrice.setText(mFormat1.format(Double.parseDouble(mBeans.get(position).getNewPrice())));

//                if (!TextUtils.isEmpty(mBeans.get(position).getPriceChangeRatio())) {
                double zdf = mBeans.get(position).getPriceChangeRatio();

                if (zdf > 0f) {
                    viewHolder.mPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (zdf == 0f) {
                    viewHolder.mPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
                } else {
                    viewHolder.mPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }
//                }

            }
        }


        if (!TextUtils.isEmpty(mBeans.get(position).getType()) && "3000".equals(mBeans.get(position).getType())) {

            double zdf = Double.parseDouble(mBeans.get(position).getTurnover());
            viewHolder.mStockZd.setText(mFormat2.format(zdf));

            viewHolder.mStockZd.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));

        } else if (!TextUtils.isEmpty(mBeans.get(position).getType()) && "2000".equals(mBeans.get(position).getType())
                || !TextUtils.isEmpty(mBeans.get(position).getType()) && "1000".equals(mBeans.get(position).getType())) {

            double zdf = mBeans.get(position).getPriceChangeRatio();
            viewHolder.mStockZd.setText(mFormat2.format(zdf));

            if (zdf > 0f) {
                viewHolder.mStockZd.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            } else if (zdf == 0f) {
                viewHolder.mStockZd.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
            } else {
                viewHolder.mStockZd.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hushen_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mCode;
        TextView mPrice;
        TextView mStockZd;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.hushen_stockName);
            mCode = (TextView) itemView.findViewById(R.id.hushen_stockNumber);
            mPrice = (TextView) itemView.findViewById(R.id.hushen_stockCurrentPrice);
            mStockZd = (TextView) itemView.findViewById(R.id.hushen_stockZD);
        }
    }
}
