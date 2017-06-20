package com.tpyzq.mobile.pangu.activity.market.quotation.plate;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListIntent;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/22.
 * 各个板块的Adapter
 */
public class PlateListAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mDatas;
    private Activity mActivity;
    private String mType;

    public PlateListAdapter (Activity activity) {
        mActivity = activity;
    }

    public void setDatas(ArrayList<StockInfoEntity> datas, String type) {
        mDatas = datas;
        mType = type;
//        LogHelper.e("PlateListAdapter setDatas","type:"+type);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_platelist_item, null);
            viewHodler.turnoverName = (TextView) convertView.findViewById(R.id.stockItemName);
            viewHodler.turnoverPrice = (TextView) convertView.findViewById(R.id.stockItemPrice);
            viewHodler.turnoverRate = (TextView) convertView.findViewById(R.id.stockItemRate);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryName())) {
            viewHodler.turnoverName.setText(mDatas.get(position).getIndustryName());
        } else {
            viewHodler.turnoverName.setText("--");
        }

        viewHodler.turnoverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                StockListIntent data = new StockListIntent();

                if (!TextUtils.isEmpty(mDatas.get(position).getIndustryNumber())) {

                    String industryNumber = mDatas.get(position).getIndustryNumber();

                    if (!TextUtils.isEmpty(mDatas.get(position).getIndustryName())) {
                        data.setTitle(mDatas.get(position).getIndustryName());
                    }

                    data.setHead2("现价");
                    data.setHead3("涨跌幅");
                    data.setHead1("股票名称");

                    if (!TextUtils.isEmpty(industryNumber)) {
                        intent2.putExtra("code", industryNumber);
                    }

                    intent2.putExtra("market", "0");

                    if ("1".equals(mType)) {
                        intent2.putExtra("type", "1");
                    } else if ("2".equals(mType)) {
                        intent2.putExtra("type", "3");
                    } else if ("3".equals(mType)) {
                        intent2.putExtra("type", "2");
                        String industryName = mDatas.get(position).getIndustryName();
                        if (!TextUtils.isEmpty(industryName)) {
                            intent2.putExtra("code", industryName);
                        }
                    }

                    intent2.putExtra("tag", "lingdie");
                    intent2.putExtra("isIndustryStockList", true);
                    intent2.putExtra("stockIntent", data);

                    intent2.setClass(mActivity, StockListActivity.class);
                    mActivity.startActivity(intent2);
                }
            }
        });


        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryUpAndDown())) {
            if (Helper.isDecimal(mDatas.get(position).getIndustryUpAndDown()) || Helper.isENum(mDatas.get(position).getIndustryUpAndDown())) {
                DecimalFormat format = new DecimalFormat("#0.00%");
                double zdf = Double.parseDouble(mDatas.get(position).getIndustryUpAndDown());
                viewHodler.turnoverPrice.setText(format.format(zdf));

                if (zdf > 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                } else if (zdf == 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                } else if (zdf < 0) {
                    viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                }
            } else {
                viewHodler.turnoverPrice.setText(mDatas.get(position).getIndustryUpAndDown());
                viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            }
        } else {
            viewHodler.turnoverPrice.setText("--");
            viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
        }



        viewHodler.turnoverRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                StockDetailEntity stockDetailEntity = new StockDetailEntity();
                stockDetailEntity.setStockName(mDatas.get(position).getStockName());
                stockDetailEntity.setStockCode(mDatas.get(position).getStockNumber());
                intent.putExtra("stockIntent", stockDetailEntity);
                intent.setClass(mActivity, StockDetailActivity.class);
                mActivity.startActivity(intent);
            }
        });
        String stockName = mDatas.get(position).getStockName();

        if (!TextUtils.isEmpty(stockName)) {
            viewHodler.turnoverRate.setText(stockName);
        } else {
            viewHodler.turnoverRate.setText("--");
        }

        viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));

        return convertView;
    }

    private class ViewHodler {
        TextView turnoverName;
        TextView turnoverPrice;
        TextView turnoverRate;
    }
}
