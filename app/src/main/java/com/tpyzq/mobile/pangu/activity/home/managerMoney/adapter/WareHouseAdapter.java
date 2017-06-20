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
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.progress.HorizontalProgressBar;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/9/29.
 * 重仓Adapter
 */
public class WareHouseAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_ware_house, null);
            viewHodler.name = (TextView) convertView.findViewById(R.id.wareHouseName);
            viewHodler.code = (TextView) convertView.findViewById(R.id.wareHouseNumber);
            viewHodler.progressBar = (HorizontalProgressBar) convertView.findViewById(R.id.wareHouseProgress);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.name.setText(null);
        viewHodler.code.setText(null);
        viewHodler.progressBar.setProgress(0);


        if (!TextUtils.isEmpty(mEntities.get(position).getStockName())) {
            viewHodler.name.setText(mEntities.get(position).getStockName());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getStockNumber())) {
            viewHodler.code.setText(mEntities.get(position).getStockNumber());
        }

        long dRaionNv = 0;
        double d = 0d;
        if (!TextUtils.isEmpty(mEntities.get(position).getProgressPersent())) {
            d = Double.parseDouble(mEntities.get(position).getProgressPersent());
            dRaionNv = Math.round(d * 100);
        }

        viewHodler.progressBar.setProgress((int)dRaionNv);

        viewHodler.progressBar.setText(Helper.fromMateByPersent().format(d));

        return convertView;
    }


    private class ViewHodler {
        TextView name, code;
        HorizontalProgressBar progressBar;
    }
}
