package com.tpyzq.mobile.pangu.adapter.home;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.ArrayList;



/**
 * Created by zhangwenbo on 2017/1/10.
 */

public class HomeHotAdapter extends BaseAdapter {

    ArrayList<StockInfoEntity> mDatas;
    public void setDatas(ArrayList<StockInfoEntity> data) {
        mDatas = data;
        LogUtil.e("homhotadapter","--------:"+data.size());
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

        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if (type == 0) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.head_home_hot_mor, null);
        } else if (type == 1) {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.head_home_hot, null);
        } else {
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_home_hot_item, null);
        }

        if (type == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        } else if (type == 2){

            TextView stockName = (TextView) convertView.findViewById(R.id.homeHotItemName);
            TextView stockNumber = (TextView) convertView.findViewById(R.id.homeHotItemNumber);
            ImageView holdIv = (ImageView) convertView.findViewById(R.id.homeHotItemHoldIv);
            TextView rift = (TextView) convertView.findViewById(R.id.homeItemHotRift);
            TextView searchNum = (TextView) convertView.findViewById(R.id.homeSearchNum);

            stockName.setText("");
            stockNumber.setText("");
            holdIv.setVisibility(View.GONE);
            rift.setText("");
            searchNum.setText("");

            if (!TextUtils.isEmpty(mDatas.get(position).getStockName())) {
                stockName.setText(mDatas.get(position).getStockName());
            }

            if (!TextUtils.isEmpty(mDatas.get(position).getStockNumber())) {

                String mFrl = String.valueOf(mDatas.get(position).getStockNumber().charAt(0));

                if ("1".equals(mFrl)) {
                    stockNumber.setText("SH" + mDatas.get(position).getStockNumber().substring(2));
                } else if ("2".equals(mFrl)) {
                    stockNumber.setText("SZ" + mDatas.get(position).getStockNumber().substring(2));
                } else {
                    stockNumber.setText(mDatas.get(position).getStockNumber());
                }
            }

            String rafio = mDatas.get(position).getRead();
            if (!TextUtils.isEmpty(rafio)) {

                if (!"-".equals(rafio)&&!"�".equals(rafio)) {
                    rift.setText(rafio + "%");

                    double _rafio = Double.parseDouble(rafio);
                    if (_rafio > 0) {
                        rift.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (_rafio == 0) {
                        rift.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (_rafio < 0) {
                        rift.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }

                } else {
                    rift.setText(mDatas.get(position).getRead());
                }
            }

            if (!TextUtils.isEmpty(mDatas.get(position).getViewcount())) {
                searchNum.setText(mDatas.get(position).getViewcount());
            }

            String code = HOLD_SEQ.getHoldCodes();
            String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
            if (!TextUtils.isEmpty(code)) {
                if (code.contains(",")) {
                    String [] codes = code.split(",");

                    for (String _tempCode : codes) {
                        if (_tempCode.equals(mDatas.get(position).getStockNumber()) && "true".equals(appearHold)) {
                            holdIv.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (code.equals(mDatas.get(position).getStockNumber()) && "true".equals(appearHold)) {
                        holdIv.setVisibility(View.VISIBLE);
                    }
                }
            }

        }


        return convertView;
    }

}
