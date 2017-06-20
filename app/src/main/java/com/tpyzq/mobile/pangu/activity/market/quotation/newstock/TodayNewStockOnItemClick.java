package com.tpyzq.mobile.pangu.activity.market.quotation.newstock;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.tpyzq.mobile.pangu.activity.trade.stock.PublishNewStockDetail;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21.
 */

public class TodayNewStockOnItemClick implements AdapterView.OnItemClickListener {

    private Activity mActivity;
    private ArrayList<NewStockEnitiy.DataBeanToday> mDatas;
    public TodayNewStockOnItemClick (Activity activity, ArrayList<NewStockEnitiy.DataBeanToday> datas) {
        mActivity = activity;
        mDatas = datas;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position != 0) {
            Intent intent = new Intent();
            intent.putExtra("name", mDatas.get(position).getISSUENAMEABBR_ONLINE());
            intent.putExtra("number", mDatas.get(position).getSECUCODE());
            intent.setClass(mActivity, PublishNewStockDetail.class);
            mActivity.startActivity(intent);
        }

    }
}
