package com.tpyzq.mobile.pangu.adapter.trade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.List;

/**
 * Created by zhangwenbo on 2017/3/2.
 */

public class OTC_ContractAdapter extends BaseAdapter {

    private List<String> mData;

    public void setData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData != null && mData.size() > 0) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null && mData.size() > 0) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.item_agreement, null);
            viewHodler.textView = (TextView) convertView.findViewById(R.id.tv_text1);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.textView.setText("");
        viewHodler.textView.setText(mData.get(position));
        return convertView;
    }

    class ViewHodler {
        TextView textView;
    }
}
