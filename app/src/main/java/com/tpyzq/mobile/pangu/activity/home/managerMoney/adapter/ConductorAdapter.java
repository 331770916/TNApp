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

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/9/29.
 */
public class ConductorAdapter extends BaseAdapter {

    private ArrayList<CleverManamgerMoneyEntity> mEntities;

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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_conductor, null);
            viewHodler.startTime = (TextView) convertView.findViewById(R.id.conductorStartDate);
            viewHodler.endTime = (TextView) convertView.findViewById(R.id.conductorEndDate);
            viewHodler.manager = (TextView) convertView.findViewById(R.id.conductorManager);
            viewHodler.period = (TextView) convertView.findViewById(R.id.conductorPeriod);
            viewHodler.earn = (TextView) convertView.findViewById(R.id.conductorEarn);

            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.startTime.setText(null);
        viewHodler.endTime.setText(null);
        viewHodler.manager.setText(null);
        viewHodler.period.setText(null);
        viewHodler.earn.setText(null);

        if (!TextUtils.isEmpty(mEntities.get(position).getStartTime())) {
            viewHodler.startTime.setText(mEntities.get(position).getStartTime());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getEndTime())) {
            viewHodler.endTime.setText(mEntities.get(position).getEndTime());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getManager())) {
            viewHodler.manager.setText(mEntities.get(position).getManager());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getPeriod())) {
            viewHodler.period.setText(mEntities.get(position).getPeriod());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getEarn())) {
            viewHodler.earn.setText(mEntities.get(position).getEarn());
        }

        return convertView;
    }


    private class ViewHodler {
        TextView startTime, endTime, manager, period, earn;
    }

}
