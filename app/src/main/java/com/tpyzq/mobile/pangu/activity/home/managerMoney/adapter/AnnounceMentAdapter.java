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
public class AnnounceMentAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_announcement, null);
            viewHodler.announceTitle = (TextView) convertView.findViewById(R.id.announcementTitle);
            viewHodler.announceFrom = (TextView) convertView.findViewById(R.id.announcementFrom);
            viewHodler.announceDate = (TextView) convertView.findViewById(R.id.announcementDate);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.announceTitle.setText(null);
        viewHodler.announceFrom.setText(null);
        viewHodler.announceDate.setText(null);


        if (!TextUtils.isEmpty(mEntities.get(position).getAnnounceTitle())) {
            viewHodler.announceTitle.setText(mEntities.get(position).getAnnounceTitle());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getAnnounceFrom())) {
            viewHodler.announceFrom.setText(mEntities.get(position).getAnnounceFrom());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getAnnounceDate())) {
            viewHodler.announceDate.setText(mEntities.get(position).getAnnounceDate());
        }

        return convertView;
    }


    private class ViewHodler {
        TextView announceTitle, announceFrom, announceDate;
    }
}
