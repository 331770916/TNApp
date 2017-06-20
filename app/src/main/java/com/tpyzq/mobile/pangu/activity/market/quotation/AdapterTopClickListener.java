package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/10/25.
 */
public class AdapterTopClickListener implements View.OnClickListener {

    private List<StockInfoEntity> mEntitys;
    private Activity mActivity;
    private int mIndex;

    public AdapterTopClickListener(List<StockInfoEntity> entity, Activity activity, int index) {
        mEntitys = entity;
        mActivity = activity;
        mIndex = index;
    }

    @Override
    public void onClick(View v) {

        String stockName = "";
        String stockCode = "";

        if (mEntitys != null && mEntitys.size() > 0) {
            stockName = mEntitys.get(mIndex).getStockName();
            stockCode = mEntitys.get(mIndex).getStockNumber();
        }

        if (!TextUtils.isEmpty(stockCode) && !TextUtils.isEmpty(stockName)) {
            Intent intent = new Intent();
            StockDetailEntity stockDetailEntity = new StockDetailEntity();
            stockDetailEntity.setStockName(mEntitys.get(mIndex).getStockName());
            stockDetailEntity.setStockCode(mEntitys.get(mIndex).getStockNumber());
            intent.putExtra("stockIntent", stockDetailEntity);
            intent.setClass(mActivity, StockDetailActivity.class);
            mActivity.startActivity(intent);
        }
    }
}
