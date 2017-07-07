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
 * Created by 李雄 on 2017/7/5.
 * 申赎撤单适配器
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etf_revoke, null);
            viewHolder.tv_stock_name = (TextView)convertView.findViewById(R.id.tv_stock_name);//证券名称
            viewHolder.tv_stock_code = (TextView)convertView.findViewById(R.id.tv_stock_code);//证券代码
            viewHolder.tv_entrust_no = (TextView)convertView.findViewById(R.id.tv_entrust_no);//委托代码
            viewHolder.tv_amount = (TextView)convertView.findViewById(R.id.tv_amount);//数量
            viewHolder.tv_blance = (TextView)convertView.findViewById(R.id.tv_blance);//金额
            viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);//状态
            viewHolder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);//类型
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EtfDataEntity etfDataEntity = mList.get(position);
        viewHolder.tv_stock_name.setText(etfDataEntity.getStock_name());
        viewHolder.tv_stock_code.setText(etfDataEntity.getStock_code());
        viewHolder.tv_entrust_no.setText(etfDataEntity.getEntrust_no());
        viewHolder.tv_amount.setText(etfDataEntity.getEntrust_amount());
        viewHolder.tv_blance.setText(etfDataEntity.getPrev_balance());
        viewHolder.tv_status.setText(etfDataEntity.getEntrust_status_name());
        viewHolder.tv_type.setText(etfDataEntity.getExchange_type_name());
        return convertView;
    }

    class ViewHolder {
        TextView tv_stock_name, tv_stock_code, tv_entrust_no, tv_amount, tv_blance, tv_status, tv_type;
    }
}
