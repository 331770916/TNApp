package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/20.
 * 定投记录
 */

public class TragetRecordItemAdapter extends BaseAdapter {

    public static final String KEY_DATE = "key_date";
    public static final String KEY_PRICE = "key_price";
    public static final String KEY_DISPOSE = "key_dispose";

    private Context mContext;
    private List<Map<String, String>> mDatas;
    public TragetRecordItemAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<Map<String, String>> datas) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_target_record, null);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.record_date);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.record_price);
            viewHolder.tvDispose = (TextView) convertView.findViewById(R.id.record_dispose);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDate.setText(mDatas.get(position).get(KEY_DATE));
        viewHolder.tvPrice.setText(mDatas.get(position).get(KEY_PRICE));

        if ("1".equals(mDatas.get(position).get(KEY_DISPOSE))) {
            viewHolder.tvDispose.setText(mDatas.get(position).get(KEY_DISPOSE));
            viewHolder.tvDispose.setTextColor(ContextCompat.getColor(mContext, R.color.color_4a4a4a));
        } else {
            viewHolder.tvDispose.setText(mDatas.get(position).get(KEY_DISPOSE));
            viewHolder.tvDispose.setTextColor(Color.RED);
        }


        return convertView;
    }

    private class ViewHolder {
        TextView tvDate;
        TextView tvPrice;
        TextView tvDispose;
    }
}
