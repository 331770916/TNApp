package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;

import java.util.List;

/**
 * Created by wangqi on 2017/6/23.
 * 分级基金选择基金或网络投票
 */

public class FJFundChooseAdapter extends BaseAdapter {
    private Context mContext;
    private List<StructuredFundEntity> mList_SFE;
    private List<NetworkVotingEntity> mList_NFE;
    private int mPoint;
    private int mTag;

    public FJFundChooseAdapter(Context context, int tag) {
        this.mContext = context;
        this.mTag = tag;
    }

    public void setData(Object list) {
        switch (mTag) {
            case 0:
                this.mList_SFE = (List<StructuredFundEntity>) list;
                break;
            case 1:
                this.mList_NFE = (List<NetworkVotingEntity>) list;
                break;
        }

        notifyDataSetChanged();
    }

    public void setPoint(int point) {
        this.mPoint = point;
    }

    @Override
    public int getCount() {
        switch (mTag) {
            case 0:
                if (mList_SFE != null && mList_SFE.size() > 0) {
                    return mList_SFE.size();
                }
                break;
            case 1:
                if (mList_NFE != null && mList_NFE.size() > 0) {
                    return mList_NFE.size();
                }
                break;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        switch (mTag) {
            case 0:
                if (mList_SFE != null && mList_SFE.size() > 0) {
                    return mList_SFE.get(position);
                }
                break;
            case 1:
                if (mList_NFE != null && mList_NFE.size() > 0) {
                    return mList_NFE.get(position);
                }
                break;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fund_list, null);
            viewHolder.stockCode = (TextView) convertView.findViewById(R.id.tv_1);
            viewHolder.stockName = (TextView) convertView.findViewById(R.id.tv_2);
            viewHolder.ivDuiGou = (ImageView) convertView.findViewById(R.id.iv_3);

            switch (mTag){
                case 0:
                    break;
                case 1:
                    viewHolder.stockName.setVisibility(View.INVISIBLE);
                    break;
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (mTag) {
            case 0:
                viewHolder.stockCode.setText(mList_SFE.get(position).getStocken_code() + "");
                viewHolder.stockName.setText(mList_SFE.get(position).getStoken_name() + "");
                break;
            case 1:
                viewHolder.stockCode.setText(mList_NFE.get(position).getStock_code());
                viewHolder.stockName.setText(mList_NFE.get(position).getStock_name());
                break;
        }


        if (mPoint == position) {
            viewHolder.ivDuiGou.setVisibility(View.VISIBLE);
            viewHolder.ivDuiGou.setImageResource(R.mipmap.duigou);
        } else {
            viewHolder.ivDuiGou.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        TextView stockCode;
        TextView stockName;
        ImageView ivDuiGou;
    }
}
