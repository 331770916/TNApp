package com.tpyzq.mobile.pangu.adapter.trade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.BankBusinessIndexActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;

/**
 * Created by zhangwemnbo on 2016/8/22.
 * 银证业务Adapter
 */
public class BuinessIndexAdapter extends BaseAdapter {

    private ArrayList<BankBusinessIndexActivity.BuinessBankIndex> mDatas;

    public void setDatas(ArrayList<BankBusinessIndexActivity.BuinessBankIndex> datas) {
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
    public int getItemViewType(int position) {

        String type = mDatas.get(position).getType();

        int flag = -1;

        if ("0".equals(type)) {
            flag = 0;
        } else if ("1".equals(type)) {
            flag = 1;
        }

        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if (convertView == null) {

            if (type == 0) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.buiness_index_adpater_sub1, null);
            } else if (type == 1) {
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.buiness_index_adapter, null);
            }
        }

        if (type == 0) {
            TextView textView = (TextView) convertView.findViewById(R.id.buiness_item_index_subtv1);
            textView.setText(mDatas.get(position).getTitle().substring(1));
        } else if (type == 1) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.buiness_item_iv1);
            imageView.setImageDrawable(mDatas.get(position).getDrawable());

            TextView title = (TextView) convertView.findViewById(R.id.buiness_item_tv1);
            title.setText(mDatas.get(position).getTitle().substring(1));

            TextView discrib = (TextView) convertView.findViewById(R.id.buiness_item_tv2);
            discrib.setText(mDatas.get(position).getDisribtion());
        }

        return convertView;
    }
}
