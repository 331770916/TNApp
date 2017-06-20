package com.tpyzq.mobile.pangu.adapter.market;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/8/16.
 * 待上市新股详情
 */
public class PublishNewStockDetailAdapter extends BaseAdapter {

    private ArrayList<Map<String, String>> mDatas;

    public void setDatas(ArrayList<Map<String, String>> datas) {
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
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.newstock_item_adapter, null);

            viewHodler.typeTitleTv = (TextView) convertView.findViewById(R.id.typeTitle);

            viewHodler.titleTv1 = (TextView) convertView.findViewById(R.id.newItem_title1);
            viewHodler.titleTv2 = (TextView) convertView.findViewById(R.id.newItem_title2);
            viewHodler.titleTv3 = (TextView) convertView.findViewById(R.id.newItem_title3);
            viewHodler.titleTv4 = (TextView) convertView.findViewById(R.id.newItem_title4);
            viewHodler.titleTv5 = (TextView) convertView.findViewById(R.id.newItem_title5);
            viewHodler.titleTv6 = (TextView) convertView.findViewById(R.id.newItem_title6);
            viewHodler.titleTv7 = (TextView) convertView.findViewById(R.id.newItem_title7);

            viewHodler.valueTv1 = (TextView) convertView.findViewById(R.id.newItem_value1);
            viewHodler.valueTv2 = (TextView) convertView.findViewById(R.id.newItem_value2);
            viewHodler.valueTv3 = (TextView) convertView.findViewById(R.id.newItem_value3);
            viewHodler.valueTv4 = (TextView) convertView.findViewById(R.id.newItem_value4);
            viewHodler.valueTv5 = (TextView) convertView.findViewById(R.id.newItem_value5);
            viewHodler.valueTv6 = (TextView) convertView.findViewById(R.id.newItem_value6);
            viewHodler.valueTv7 = (TextView) convertView.findViewById(R.id.newItem_value7);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.typeTitleTv.setText(null);
        viewHodler.titleTv1.setText(null);
        viewHodler.titleTv2.setText(null);
        viewHodler.titleTv3.setText(null);
        viewHodler.titleTv4.setText(null);
        viewHodler.titleTv5.setText(null);
        viewHodler.titleTv6.setText(null);
        viewHodler.titleTv7.setText(null);

        viewHodler.valueTv1.setText(null);
        viewHodler.valueTv2.setText(null);
        viewHodler.valueTv3.setText(null);
        viewHodler.valueTv4.setText(null);
        viewHodler.valueTv5.setText(null);
        viewHodler.valueTv6.setText(null);
        viewHodler.valueTv7.setText(null);


        if (!TextUtils.isEmpty(mDatas.get(position).get("typeTitle"))) {
            viewHodler.typeTitleTv.setText(mDatas.get(position).get("typeTitle"));
        } else {
            viewHodler.typeTitleTv.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title1"))) {
            viewHodler.titleTv1.setText(mDatas.get(position).get("title1"));
        } else {
            viewHodler.titleTv1.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title2"))) {
            viewHodler.titleTv2.setText(mDatas.get(position).get("title2"));
        } else {
            viewHodler.titleTv2.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title3"))) {
            viewHodler.titleTv3.setText(mDatas.get(position).get("title3"));
        } else {
            viewHodler.titleTv3.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title4"))) {
            viewHodler.titleTv4.setText(mDatas.get(position).get("title4"));
        } else {
            viewHodler.titleTv4.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title5"))) {
            viewHodler.titleTv5.setText(mDatas.get(position).get("title5"));
        } else {
            viewHodler.titleTv5.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title6"))) {
            viewHodler.titleTv6.setText(mDatas.get(position).get("title6"));
        } else {
            viewHodler.titleTv6.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("title7"))) {
            viewHodler.titleTv7.setText(mDatas.get(position).get("title7"));
        } else {
            viewHodler.titleTv7.setText("--");
        }

        if (!"0.00".equals(mDatas.get(position).get("value1"))) {
            viewHodler.valueTv1.setText(mDatas.get(position).get("value1"));
        } else {
            viewHodler.valueTv1.setText("--");
        }

        if (!"0.00".equals(mDatas.get(position).get("value2"))) {
            viewHodler.valueTv2.setText(mDatas.get(position).get("value2"));
        } else {
            viewHodler.valueTv2.setText("--");
        }

        if (!"0.0".equals(mDatas.get(position).get("value3").substring(0,3))) {
            viewHodler.valueTv3.setText(mDatas.get(position).get("value3"));
        } else {
            viewHodler.valueTv3.setText("--");
        }

        if (!"0.0".equals(mDatas.get(position).get("value4").substring(0,3))) {
            viewHodler.valueTv4.setText(mDatas.get(position).get("value4"));
        } else {
            viewHodler.valueTv4.setText("--");
        }

        if (!TextUtils.isEmpty(mDatas.get(position).get("value5"))) {
            viewHodler.valueTv5.setText(mDatas.get(position).get("value5"));
        } else {
            viewHodler.valueTv5.setText("--");
        }

        if (!"0.00%".equals(mDatas.get(position).get("value6"))) {
            viewHodler.valueTv6.setText(mDatas.get(position).get("value6"));
        } else {
            viewHodler.valueTv6.setText("--");
        }


        if (!TextUtils.isEmpty(mDatas.get(position).get("value7"))) {
            viewHodler.valueTv7.setText(mDatas.get(position).get("value7"));
        } else {
            viewHodler.valueTv7.setText("--");
        }


        return convertView;
    }

    private class ViewHodler {
        TextView typeTitleTv;

        TextView titleTv1;
        TextView titleTv2;
        TextView titleTv3;
        TextView titleTv4;
        TextView titleTv5;
        TextView titleTv6;
        TextView titleTv7;

        TextView valueTv1;
        TextView valueTv2;
        TextView valueTv3;
        TextView valueTv4;
        TextView valueTv5;
        TextView valueTv6;
        TextView valueTv7;
    }
}
