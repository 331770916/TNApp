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
 * 基金概况Adapter
 */
public class GeneralSituationAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_generalsituation, null);
            viewHodler.title = (TextView) convertView.findViewById(R.id.generalsituationTitle);
            viewHodler.content = (TextView) convertView.findViewById(R.id.generalsituationContent);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.title.setText(null);
        viewHodler.content.setText(null);

        if (!TextUtils.isEmpty(mEntities.get(position).getGeneralSituationTitle())) {
            viewHodler.title.setText(mEntities.get(position).getGeneralSituationTitle());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getGeneralSituationContent())) {
            viewHodler.content.setText(mEntities.get(position).getGeneralSituationContent());
        } else {
            viewHodler.content.setText("-");
        }

        return convertView;
    }


    private class ViewHodler {
        TextView title, content;
    }

}
