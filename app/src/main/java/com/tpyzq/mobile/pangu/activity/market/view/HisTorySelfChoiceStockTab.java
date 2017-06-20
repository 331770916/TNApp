package com.tpyzq.mobile.pangu.activity.market.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/11/30.
 * 搜索股票界面， 历史自选股
 */

public class HisTorySelfChoiceStockTab extends BaseTabPager implements View.OnClickListener, AdapterView.OnItemClickListener {

    public HisTorySelfChoiceStockTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    private HistoryChoiceAdapter mAdapter;
    private ArrayList<StockInfoEntity> mHistoryBeans;
    private Activity mActivity;


    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        view.findViewById(R.id.historySearchMoveBtn).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.historySearchListView);
        mAdapter = new HistoryChoiceAdapter(activity);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void myTabonResume() {
        mHistoryBeans = Db_PUB_SEARCHHISTORYSTOCK.queryAllDatas();
        mAdapter.setDatas(mHistoryBeans);
    }

    @Override
    public void toRunConnect() {
        mHistoryBeans = Db_PUB_SEARCHHISTORYSTOCK.queryAllDatas();
        mAdapter.setDatas(mHistoryBeans);
    }

    @Override
    public void toStopConnect() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.historySearchMoveBtn:

                Db_PUB_SEARCHHISTORYSTOCK.deleteAllDatas();
                ArrayList<StockInfoEntity> hostoryBeans = Db_PUB_SEARCHHISTORYSTOCK.queryAllDatas();
                mAdapter.setDatas(hostoryBeans);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            StockInfoEntity sie = mHistoryBeans.get(position);
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(sie.getStockName());
            data.setStockCode(sie.getStockNumber());

            intent.putExtra("stockIntent", data);

            intent.setClass(mActivity, StockDetailActivity.class);
            mActivity.startActivity(intent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.tab_search_history;
    }
}
