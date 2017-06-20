package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/9/29.
 */
public class MistakeFundHistoryValueAdapter extends BaseAdapter {

    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private String mFundTypeCode;

    public void setDatas(ArrayList<CleverManamgerMoneyEntity> entities, String fundTypeCode) {
        mEntities = entities;
        mFundTypeCode = fundTypeCode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.get(position);
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_mistake_fund_historyvalue, null);
            viewHodler.historyDate = (TextView) convertView.findViewById(R.id.mistakeFundHistoryDate);
            viewHodler.historyValue = (TextView) convertView.findViewById(R.id.mistakeFundHistoryValue);
            viewHodler.historyValueTotal = (TextView) convertView.findViewById(R.id.mistakeFundHistoryValueTotal);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.historyDate.setText(null);
        viewHodler.historyValue.setText(null);
        viewHodler.historyValueTotal.setText(null);

        if (ConstantUtil.MONEYTYPE.equals(mFundTypeCode)) {//货币基金

            if (!TextUtils.isEmpty(mEntities.get(position).getHistoryNetValueDate())) {
                viewHodler.historyDate.setText(mEntities.get(position).getHistoryNetValueDate());
            }

            if (!TextUtils.isEmpty(mEntities.get(position).getDAILYPROFIT()) && Helper.isDecimal(mEntities.get(position).getDAILYPROFIT())) {

                DecimalFormat format = new DecimalFormat("#0.0000");

                viewHodler.historyValue.setText(format.format(Double.parseDouble(mEntities.get(position).getDAILYPROFIT())));
            } else {
                viewHodler.historyValue.setText("-");
            }

            if (!TextUtils.isEmpty(mEntities.get(position).getLATESTWEEKLYYIELD())) {
                String latestweeklyyield = Helper.fromMateByPersent().format(Double.parseDouble(mEntities.get(position).getLATESTWEEKLYYIELD()));
                viewHodler.historyValueTotal.setText(latestweeklyyield);
            }

        } else {

            if (!TextUtils.isEmpty(mEntities.get(position).getHistoryNetValueDate())) {
                viewHodler.historyDate.setText(mEntities.get(position).getHistoryNetValueDate());
            }

            if (!TextUtils.isEmpty(mEntities.get(position).getHistoryNetValueUnit())) {
                viewHodler.historyValue.setText(mEntities.get(position).getHistoryNetValueUnit());
            }

            if (!TextUtils.isEmpty(mEntities.get(position).getHistoryNetValueTotal())) {
                viewHodler.historyValueTotal.setText(mEntities.get(position).getHistoryNetValueTotal());
            }

        }

        return convertView;
    }


    private class ViewHodler {
        TextView historyDate, historyValue, historyValueTotal;
    }
}
