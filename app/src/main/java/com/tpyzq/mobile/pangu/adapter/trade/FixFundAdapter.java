package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseListAdapter;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.view.CentreToast;

import java.util.ArrayList;

/**
 * Created by 李雄 on 2017/7/5.
 * 申赎撤单适配器
 */

public class FixFundAdapter extends BaseListAdapter {
    public static final int TAG_REVOKE = 1000001;
    public static final int TAG_MODIFY = 1000002;
    private Context mContext;
    private ArrayList<FixFundEntity> mList;
    private boolean isAll = false;
    public FixFundAdapter(Context context, ArrayList<FixFundEntity> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    public void setData(ArrayList<FixFundEntity> mList,boolean isAll) {
        this.mList = mList;
        this.isAll = isAll;
        notifyDataSetChanged();
    }
    public void setData(ArrayList<FixFundEntity> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fix_fund, null);
            viewHolder.tv_fund_name = (TextView)convertView.findViewById(R.id.tv_fund_name);//基金名称
            viewHolder.tv_fund_code = (TextView)convertView.findViewById(R.id.tv_fund_code);//基金代码
            viewHolder.tv_accumulated_money = (TextView)convertView.findViewById(R.id.tv_accumulated_money);//累计定投金额
            viewHolder.tv_money = (TextView)convertView.findViewById(R.id.tv_money);//每期定投
            viewHolder.tv_start_date = (TextView)convertView.findViewById(R.id.tv_start_date);//起投时间
            viewHolder.tv_en_date = (TextView)convertView.findViewById(R.id.tv_en_date);//定投日
            viewHolder.tv_end_date = (TextView)convertView.findViewById(R.id.tv_end_date);//终止日期
            viewHolder.tv_modify = (TextView)convertView.findViewById(R.id.tv_modify);//修改
            viewHolder.tv_revoke = (TextView)convertView.findViewById(R.id.tv_revoke);//撤销
            viewHolder.tv_history = (TextView)convertView.findViewById(R.id.tv_history);//定投记录
            viewHolder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);//底部添加定投条目
            viewHolder.ll_add = (LinearLayout) convertView.findViewById(R.id.ll_add);//底部添加定投点击条目
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FixFundEntity fixFundEntity = mList.get(position);
        viewHolder.tv_fund_name.setText(fixFundEntity.getFUND_NAME());
        viewHolder.tv_fund_code.setText(fixFundEntity.getFUND_CODE());
        viewHolder.tv_accumulated_money.setText(fixFundEntity.getSEND_BALANCE());
        viewHolder.tv_money.setText(fixFundEntity.getBALANCE());
        viewHolder.tv_start_date.setText(fixFundEntity.getSTART_DATE());
        viewHolder.tv_en_date.setText(fixFundEntity.getEN_FUND_DATE());
        viewHolder.tv_end_date.setText(fixFundEntity.getEND_DATE());
        if (position == mList.size()-1 && isAll) {
            viewHolder.ll_bottom.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_bottom.setVisibility(View.GONE);
        }
        viewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CentreToast.showText(mContext,"跳转修改页面");
            }
        });
        viewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=itemOnClickListener)
                    itemOnClickListener.onItemClick(TAG_REVOKE, position);
            }
        });
        viewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CentreToast.showText(mContext,"跳转定投记录页面");
            }
        });
        viewHolder.ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=itemOnClickListener)
                    itemOnClickListener.onItemClick(TAG_MODIFY, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_fund_name, tv_fund_code, tv_accumulated_money, tv_money, tv_start_date,
                tv_en_date, tv_end_date,tv_modify,tv_revoke,tv_history;
        LinearLayout ll_add, ll_bottom;
    }
}
