package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundChangeActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundRedemptionActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.ShareFundActivity;
import com.tpyzq.mobile.pangu.data.FundShareEntity;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/10/9.
 */
public class FundShareAdapter extends BaseAdapter {

    private ArrayList<FundShareEntity> mEntities;
    private int tempPosition;
    private Context context;

    public FundShareAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(ArrayList<FundShareEntity> entities) {
        mEntities = entities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mEntities != null && mEntities.size() > 0) {
            return mEntities.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHodler viewHodler;

        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_fundshare, null);
            viewHodler.linearLayout = (LinearLayout) convertView.findViewById(R.id.adapterFundShareLayout);
            viewHodler.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.fundShareLayout2);
            viewHodler.stockName = (TextView) convertView.findViewById(R.id.fundShareItemTv1);         //基金名称和代码
            viewHodler.stockCode = (TextView) convertView.findViewById(R.id.tv_fund_code);         //基金名称和代码
            viewHodler.holdePrice = (TextView) convertView.findViewById(R.id.fundShareItemTv2);        //持有份额
            viewHodler.earn = (TextView) convertView.findViewById(R.id.fundShareItemTv3);              //市值盈亏
            viewHodler.saveBake = (TextView) convertView.findViewById(R.id.fundShareItemTv4);          //赎回
            viewHodler.transfer = (TextView) convertView.findViewById(R.id.fundShareItemTv5);          //转换
            viewHodler.setting = (TextView) convertView.findViewById(R.id.fundShareItemTv6);           //分红设置
            viewHodler.commpany = (TextView) convertView.findViewById(R.id.fundShareItemTv7);          //基金公司
            viewHodler.couldNumber = (TextView) convertView.findViewById(R.id.fundShareItemTv8);       //冻结数量
            viewHodler.commpanyCode = (TextView) convertView.findViewById(R.id.fundShareItemTv9);      //基金公司代码
            viewHodler.avabilityNumber = (TextView) convertView.findViewById(R.id.fundShareItemTv10);   //可用数量
            viewHodler.earnType = (TextView) convertView.findViewById(R.id.fundShareItemTv11);          //分红方式
            viewHodler.costPrice = (TextView) convertView.findViewById(R.id.fundShareItemTv12);         //成本价
            viewHodler.imageButton = (ImageButton) convertView.findViewById(R.id.fundShareItemTv13);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        viewHodler.stockName.setText(null);
        viewHodler.holdePrice.setText(null);
        viewHodler.earn.setText(null);
        viewHodler.commpany.setText(null);
        viewHodler.couldNumber.setText(null);
        viewHodler.commpanyCode.setText(null);
        viewHodler.avabilityNumber.setText(null);
        viewHodler.earnType.setText(null);
        viewHodler.costPrice.setText(null);


        if (!TextUtils.isEmpty(mEntities.get(position).getStockName()) && !TextUtils.isEmpty(mEntities.get(position).getStockCode())) {
            viewHodler.stockName.setText(mEntities.get(position).getStockName());
        }
        if (!TextUtils.isEmpty(mEntities.get(position).getStockName()) && !TextUtils.isEmpty(mEntities.get(position).getStockCode())) {
            viewHodler.stockCode.setText("(" + mEntities.get(position).getStockCode() + ")");
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getHoldShare()) && !TextUtils.isEmpty(mEntities.get(position).getNetWorth())) {
            viewHodler.holdePrice.setText(mEntities.get(position).getHoldShare() + "/" + mEntities.get(position).getNetWorth());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getMarketValue()) && !TextUtils.isEmpty(mEntities.get(position).getProfitLoss())) {
            viewHodler.earn.setText(mEntities.get(position).getMarketValue() + "/" + mEntities.get(position).getProfitLoss());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getCommpanyName())) {
            viewHodler.commpany.setText(mEntities.get(position).getCommpanyName());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getFreezeNumber())) {
            viewHodler.couldNumber.setText(mEntities.get(position).getFreezeNumber());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getCommpanyCode())) {
            viewHodler.commpanyCode.setText(mEntities.get(position).getCommpanyCode());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getAvailableNumber())) {
            viewHodler.avabilityNumber.setText(mEntities.get(position).getAvailableNumber());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getProfitType())) {
            viewHodler.earnType.setText(mEntities.get(position).getProfitType());
        }

        if (!TextUtils.isEmpty(mEntities.get(position).getCostPrice())) {
            viewHodler.costPrice.setText(mEntities.get(position).getCostPrice());
        }

        if (!mEntities.get(position).isUnfold()) {
            viewHodler.linearLayout2.setVisibility(View.GONE);
        } else {
            viewHodler.linearLayout2.setVisibility(View.VISIBLE);
        }

        viewHodler.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHodler.linearLayout2.setVisibility(View.VISIBLE);

                mEntities.get(tempPosition).setUnfold(false);
                mEntities.get(position).setUnfold(true);
                tempPosition = position;
                notifyDataSetChanged();
            }
        });

        viewHodler.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHodler.linearLayout2.setVisibility(View.GONE);
                mEntities.get(position).setUnfold(false);
                notifyDataSetChanged();
            }
        });
        viewHodler.saveBake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, FundRedemptionActivity.class);
                intent.putExtra("fundcode",mEntities.get(position).getStockCode());
                intent.putExtra("fundcompany",mEntities.get(position).getCommpanyCode());
                context.startActivity(intent);
            }
        });
        viewHodler.transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, FundChangeActivity.class);
                intent.putExtra("fundcode",mEntities.get(position).getStockCode());
                intent.putExtra("fundcompany",mEntities.get(position).getCommpanyCode());
                context.startActivity(intent);
            }
        });
        viewHodler.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ShareFundActivity.class);
                intent.putExtra("fundcode",mEntities.get(position).getStockCode());
                intent.putExtra("fundcompany",mEntities.get(position).getCommpanyCode());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHodler {
        LinearLayout linearLayout;
        LinearLayout linearLayout2;
        TextView stockName;         //基金名称和代码
        TextView stockCode;         //基金名称和代码
        TextView holdePrice;        //持有份额
        TextView earn;              //市值盈亏
        TextView saveBake;          //赎回
        TextView transfer;          //转换
        TextView setting;           //分红设置
        TextView commpany;          //基金公司
        TextView couldNumber;       //冻结数量
        TextView commpanyCode;      //基金公司代码
        TextView avabilityNumber;   //可用数量
        TextView earnType;          //分红方式
        TextView costPrice;         //成本价
        ImageButton imageButton;
    }
}
