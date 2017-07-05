package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;

import java.util.ArrayList;

/**
 * Created by 33920_000 on 2017/7/5.
 */

public class ETFRevokeAdapter  extends BaseAdapter {
    private Context mContext;
    private ArrayList<EtfDataEntity> mList;

    public ETFRevokeAdapter(Context context,ArrayList<EtfDataEntity> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    public void setData(ArrayList<EtfDataEntity> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null==mList) {
            return 0;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (null == mList) {
            return null;
        } else {
            return mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etf_revoke_order, null);
            viewHolder.tv1 = (TextView)convertView.findViewById(R.id.tv1);//证券名称
            viewHolder.tv2 = (TextView)convertView.findViewById(R.id.tv2);//证券代码
            viewHolder.tv3 = (TextView)convertView.findViewById(R.id.tv3);//委托代码
            viewHolder.tv4 = (TextView)convertView.findViewById(R.id.tv4);//数量
            viewHolder.tv5 = (TextView)convertView.findViewById(R.id.tv5);//金额
            viewHolder.tv6 = (TextView)convertView.findViewById(R.id.tv6);//状态
            viewHolder.tv7 = (TextView)convertView.findViewById(R.id.tv7);//类型
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EtfDataEntity etfDataEntity = mList.get(position);
        viewHolder.tv1.setText(etfDataEntity.getStock_name());
        viewHolder.tv2.setText(etfDataEntity.getStock_code());
        viewHolder.tv3.setText(etfDataEntity.getEntrust_no());
        viewHolder.tv4.setText(etfDataEntity.getEntrust_amount());
        viewHolder.tv5.setText(etfDataEntity.getEntrust_balance());
        viewHolder.tv6.setText(etfDataEntity.getEntrust_status_name());
        viewHolder.tv7.setText(etfDataEntity.getEntrust_prop());
        return convertView;
    }

    class ViewHolder {
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    }
}
