package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.ETFStockListActivity;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;

import java.util.List;

/**
 * Created by zhang on 2017/7/5.
 */

public class ETFStockQueryAdapter extends BaseAdapter{
    private final String tag;
    private List<EtfDataEntity> mList;
    private Context context ;
    private LayoutInflater layoutInflater ;
    private int mPoint = -1;

    public ETFStockQueryAdapter(Context context ,List<EtfDataEntity> mList,String tag){
        this.context  = context;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);
        this.tag = tag;
    }

    public void setPoint(int point) {
        this.mPoint = point;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_constituent_query,parent,false);
            viewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_etfcode = (TextView) convertView.findViewById(R.id.tv_etf_code);
            viewHolder.fj_content = (LinearLayout) convertView.findViewById(R.id.fj_content);
            viewHolder.iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EtfDataEntity etfDataEntity = mList.get(position);
        if (ETFStockListActivity.TAG.equalsIgnoreCase(tag)) {
            //成分股列表
            viewHolder.tv_name.setText(etfDataEntity.getComponent_code());//成分股代码
            viewHolder.tv_code.setText(etfDataEntity.getStock_name());//成分股名称
            viewHolder.tv_etfcode.setText(etfDataEntity.getEnable_balance());//替代金额
            viewHolder.tv1.setText(etfDataEntity.getEntrust_amount());//单位数量
            viewHolder.tv2.setText(etfDataEntity.getStock_max());//溢价比例
            viewHolder.tv3.setText(etfDataEntity.getCash_max());//替代标记
            viewHolder.fj_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPoint == position) {
                        //如果点击的条目和展示的条目一样的话，直接进行取反
                        etfDataEntity.setShowRule(false);
                        mPoint = -1;
                    } else {
                        //如果点击条目和展示的条目不一样，将point位置的条目设置为false，将position位置的条目设置为true
                        if (mPoint != -1) {
                            mList.get(mPoint).setShowRule(false);
                        }
                        etfDataEntity.setShowRule(true);
                        mPoint = position;
                    }
                    notifyDataSetChanged();
                }
            });
            viewHolder.iv_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.get(mPoint).setShowRule(false);
                    mPoint =-1;
                    notifyDataSetChanged();
                }
            });
        } else {
            //成分股查询
            viewHolder.tv_code.setText(etfDataEntity.getStock_code());
            viewHolder.tv_name.setText(etfDataEntity.getStock_name());
            viewHolder.tv_etfcode.setText(etfDataEntity.getStock_code());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_code,tv_name,tv_etfcode,tv1,tv2,tv3;
        ImageView iv_top;
        LinearLayout fj_content;
    }
}
