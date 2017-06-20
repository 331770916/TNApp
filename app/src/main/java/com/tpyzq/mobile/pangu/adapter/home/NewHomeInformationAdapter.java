package com.tpyzq.mobile.pangu.adapter.home;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.InformationEntity;

import java.util.ArrayList;



/**
 * Created by wangqi on 2017/2/13.
 * 首页资讯 Adapter
 */

public class NewHomeInformationAdapter extends BaseAdapter {

    private ArrayList<InformationEntity> mDatas;
    private Activity mActivity;
    private View mview;

    public NewHomeInformationAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(ArrayList<InformationEntity> datas) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_item_newhomeinformation, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.homeInfomationTitle2);
            viewHolder.date = (TextView) convertView.findViewById(R.id.homeInfomationDate2);
            viewHolder.aboutStock = (TextView) convertView.findViewById(R.id.fromNews2);
            mview = convertView.findViewById(R.id.mview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(null);
        viewHolder.date.setText(null);
        viewHolder.aboutStock.setText(null);

        if (!TextUtils.isEmpty(mDatas.get(position).getPublishTitle())) {
            viewHolder.title.setText(mDatas.get(position).getPublishTitle());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getPublishTime())) {
            viewHolder.date.setText(mDatas.get(position).getPublishTime());
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getPublishAboutStock())) {
            viewHolder.aboutStock.setText(mDatas.get(position).getPublishAboutStock());
        }


        if (position == mDatas.size() - 1) {
            mview.setVisibility(View.GONE);
        } else {
            mview.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView aboutStock;

    }
}
