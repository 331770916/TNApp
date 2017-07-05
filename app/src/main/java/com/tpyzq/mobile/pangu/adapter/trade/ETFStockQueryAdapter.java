package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;

import java.util.List;

/**
 * Created by zhang on 2017/7/5.
 */

public class ETFStockQueryAdapter extends BaseAdapter{
    private List<EtfDataEntity> mList;
    private Context context ;
    private LayoutInflater layoutInflater ;

    public ETFStockQueryAdapter(Context context ,List<EtfDataEntity> mList,String tag){
        this.context  = context;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_constituent_query,parent,false);
            viewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_etfcode = (TextView) convertView.findViewById(R.id.tv_etf_code);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_code,tv_name,tv_etfcode;
    }
}
