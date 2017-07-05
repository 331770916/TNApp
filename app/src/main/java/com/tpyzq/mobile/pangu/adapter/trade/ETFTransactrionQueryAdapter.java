package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 *  Created by 李雄 on 2017/7/5.
 * etf申赎交易查询adapter
 */

public class ETFTransactrionQueryAdapter extends BaseAdapter {
    private Context mContext;
    private int mPoint = -1;//需要展开的条目
    private ArrayList<EtfDataEntity> mList;

    public ETFTransactrionQueryAdapter(Context context,ArrayList<EtfDataEntity> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    public void setData(ArrayList<EtfDataEntity> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setPoint(int point) {
        this.mPoint = point;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_deal_query, null);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);//证券名称
            viewHolder.tv_code = (TextView)convertView.findViewById(R.id.tv_code);//证券代码
            viewHolder.tv_tranaction_num = (TextView)convertView.findViewById(R.id.tv_tranaction_num);//成交数量
            viewHolder.tv_transaction_status = (TextView)convertView.findViewById(R.id.tv_transaction_status);//委托状态
            viewHolder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);//日期
            viewHolder.tv_entrust_code = (TextView)convertView.findViewById(R.id.tv_entrust_code);//委托编号
            viewHolder.tv_entrust_code1 = (TextView)convertView.findViewById(R.id.tv_entrust_code1);//成交编号
            viewHolder.tv_limit = (TextView)convertView.findViewById(R.id.tv_limit);//成交金额
            viewHolder.tv_direction = (TextView)convertView.findViewById(R.id.tv_direction);//买卖方向
            viewHolder.ll_show = (LinearLayout) convertView.findViewById(R.id.ll_show);//展示的LinearLayout
            viewHolder.fj_content = (LinearLayout) convertView.findViewById(R.id.fj_content);//点击展开下侧条目的linearlayout
            viewHolder.iv_top = (ImageView) convertView.findViewById(R.id.iv_top);//底部向上按钮
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EtfDataEntity etfDataEntity = mList.get(position);
        viewHolder.tv_name.setText(etfDataEntity.getStock_name());
        viewHolder.tv_code.setText(etfDataEntity.getStock_code());
        viewHolder.tv_tranaction_num.setText(etfDataEntity.getBusiness_balance());
        viewHolder.tv_transaction_status.setText(etfDataEntity.getReal_status_name());
        viewHolder.tv_date.setText(etfDataEntity.getInit_date());
        viewHolder.tv_entrust_code.setText(etfDataEntity.getEntrust_no());
        viewHolder.tv_entrust_code1.setText(etfDataEntity.getCbp_business_id());
        viewHolder.tv_limit.setText(etfDataEntity.getBusiness_balance());
        viewHolder.tv_direction.setText(etfDataEntity.getEntrust_bs());
        if (etfDataEntity.isShowRule()) {
            viewHolder.ll_show.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_show.setVisibility(View.GONE);
        }

        viewHolder.fj_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPoint!=-1) {
                    mList.get(mPoint).setShowRule(false);
                }
                /*if (etfDataEntity.isShowRule()) {
                    etfDataEntity.setShowRule(false);
                } else  {
                    etfDataEntity.setShowRule(true);
                }*/
                etfDataEntity.setShowRule(true);
                mPoint = position;
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

//        ll_show
        return convertView;
    }

    class ViewHolder {
        TextView tv_name,tv_code,tv_tranaction_num,tv_transaction_status,tv_date,tv_entrust_code,tv_entrust_code1,tv_limit,tv_direction;
        LinearLayout ll_show,fj_content;
        ImageView iv_top;
    }
}
