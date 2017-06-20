package com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/28.
 * 智选理财Adapter
 */
public class ManagerMoneyAdapter extends BaseAdapter {

    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private DecimalFormat mFormat2;

    public ManagerMoneyAdapter() {
        mFormat2 = new DecimalFormat("#0.00%");
    }

    public void setDatas(ArrayList<CleverManamgerMoneyEntity> entities) {
        mEntities = entities;
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_managermoney, null);
            viewHodler.fundName = (TextView) convertView.findViewById(R.id.managerMoney_adapter_fundName);
            viewHodler.fundLevel = (TextView) convertView.findViewById(R.id.managerMoney_adapter_fundLevel);
            viewHodler.fundSave = (TextView) convertView.findViewById(R.id.managerMoney_adapter_save);
            viewHodler.fundLimit = (TextView) convertView.findViewById(R.id.managerMoney_adapter_day);
            viewHodler.fundStartBuy = (TextView) convertView.findViewById(R.id.managerMoney_adapter_start);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.fundName.setText(null);
        viewHodler.fundLevel.setText(null);
        viewHodler.fundSave.setText(null);
        viewHodler.fundLimit.setText(null);
        viewHodler.fundStartBuy.setText(null);

        if (!TextUtils.isEmpty(mEntities.get(position).getPRODNAME())) {
            viewHodler.fundName.setText(mEntities.get(position).getPRODNAME());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getRISKLEVEL())) {
            String level = mEntities.get(position).getRISKLEVEL();
            String strLevel = "";
            if ("0".equals(level)) {
                strLevel = "默认";
                viewHodler.fundLevel.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.text));
            } else if ("1".equals(level)) {
                strLevel = "保本";
                viewHodler.fundLevel.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
            } else if ("2".equals(level)) {
                strLevel = "低风险";
                viewHodler.fundLevel.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
            } else if ("3".equals(level)) {
                strLevel = "中风险";
                viewHodler.fundLevel.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.calendarBtnColor));
            } else if ("4".equals(level)) {
                strLevel = "高风险";
                viewHodler.fundLevel.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
            }

            viewHodler.fundLevel.setText(strLevel);
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getPRODRATIO())) {

            String radio = mEntities.get(position).getPRODRATIO();

//            mFormat2.format()
            String str = "预期年收益率：";
            SpannableString ss = new SpannableString(str + radio);

            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.text)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.red)), str.length(), str.length() + radio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            viewHodler.fundSave.setText(ss);
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getINVESTDAYS())) {

            String date = mEntities.get(position).getINVESTDAYS() + "天";

            String str = "期限：";
            SpannableString ss = new SpannableString(str + date);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.text)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.black)), str.length(), str.length() + date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            viewHodler.fundLimit.setText(ss);
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getBUY_LOW_AMOUNT())) {

            String strBuy = mEntities.get(position).getBUY_LOW_AMOUNT() + "元";

            String str = "起购：";

            SpannableString ss = new SpannableString(str + strBuy);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.text)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.black)), str.length(), str.length() + strBuy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            viewHodler.fundStartBuy.setText(ss);
        }




        return convertView;
    }


    private class ViewHodler {
        TextView fundName, fundLevel, fundSave, fundLimit, fundStartBuy;
    }
}
