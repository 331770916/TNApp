package com.tpyzq.mobile.pangu.activity.market.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.adapter.market.LimitLookStockAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_OPTIONALHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/11/30.
 * 最近浏览过的股票
 */

public class LimitLookStockTab extends BaseTabPager implements View.OnClickListener, AdapterView.OnItemClickListener{

    private LimitLookStockAdapter mAdapter;
    private ArrayList<StockInfoEntity> mSeeBeans;
    private Activity mActivity;

    public LimitLookStockTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        view.findViewById(R.id.limitLookMoveBtn).setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.limitLookListView);
        mAdapter = new LimitLookStockAdapter(activity);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void myTabonResume() {
        mSeeBeans = Db_PUB_OPTIONALHISTORYSTOCK.queryAllDatasByDesc();
        settingSelfChoice();
        mAdapter.setDatas(mSeeBeans);
    }

    @Override
    public void toRunConnect() {
        mSeeBeans = Db_PUB_OPTIONALHISTORYSTOCK.queryAllDatasByDesc();
        settingSelfChoice();
        mAdapter.setDatas(mSeeBeans);
    }

    private void settingSelfChoice() {
        if (mSeeBeans != null && mSeeBeans.size() > 0) {

            for (StockInfoEntity stockInfoEntity : mSeeBeans) {

                if (!TextUtils.isEmpty(stockInfoEntity.getStockNumber())) {
                    //判断是不是自选股
                    StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(stockInfoEntity.getStockNumber());
                    String code = HOLD_SEQ.getHoldCodes();
                    if (!TextUtils.isEmpty(code)) {
                        if (code.contains(",")) {
                            String [] codes = code.split(",");

                            for (String _tempCode : codes) {
                                if (_tempCode.equals(stockInfoEntity.getStockNumber())) {
                                    stockInfoEntity.setIsHoldStock("true");
                                    String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
                                    stockInfoEntity.setApperHoldStock(appearHold);
                                    stockInfoEntity.setStockholdon(appearHold);
                                }
                            }
                        } else {
                            if (code.equals(stockInfoEntity.getStockNumber())) {
                                stockInfoEntity.setIsHoldStock("true");
                                String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
                                stockInfoEntity.setApperHoldStock(appearHold);
                                stockInfoEntity.setStockholdon(appearHold);
                            }
                        }
                    }

                    if (tempBean != null) {
                        stockInfoEntity.setSelfChoicStock(true);

                        if (!TextUtils.isEmpty(tempBean.getIsHoldStock())) {
                            stockInfoEntity.setIsHoldStock("true");
                        }

                        if (!TextUtils.isEmpty(tempBean.getApperHoldStock())) {
                            stockInfoEntity.setApperHoldStock(tempBean.getApperHoldStock());
                        }

                        if (!TextUtils.isEmpty(tempBean.getStockholdon())) {
                            stockInfoEntity.setStockholdon(tempBean.getStockholdon());
                        }

                    } else {
                        stockInfoEntity.setSelfChoicStock(false);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.limitLookMoveBtn:
                Db_PUB_OPTIONALHISTORYSTOCK.deleteAllDatas();
                ArrayList<StockInfoEntity> seeBeans = Db_PUB_OPTIONALHISTORYSTOCK.queryAllDatasByDesc();
                mAdapter.setDatas(seeBeans);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            StockInfoEntity sie = mSeeBeans.get(position);
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
    public void toStopConnect() {

    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.tab_limit_look_stock;
    }
}
