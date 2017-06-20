package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/17.
 */

public class TabOther   extends BaseTabPager implements OtherAdapter2.OnItemClickListener {

    private Activity mActivity;
    private RecyclerView mGridView;

    public TabOther(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        mGridView = (RecyclerView) view.findViewById(R.id.marketOterTab);
        mGridView.setLayoutManager(new GridLayoutManager(activity,4));

        //添加分割线
        mGridView.addItemDecoration(new DividerGridItemDecoration(activity));

        ArrayList<Map<String, Object>> dataSourceList = new ArrayList<>();
        Integer [] icons = { R.mipmap.more_hushena,
                R.mipmap.more_hushenb,
                R.mipmap.more_hushenjijin,
                R.mipmap.more_hushenzq,
                R.mipmap.more_shenza,
                R.mipmap.more_sza,
                R.mipmap.more_zxb,
                R.mipmap.more_startabusiness,
                R.mipmap.tuishizhengli,
                R.mipmap.fengxianjingshi};
        String [] disribs = {"沪深A股", "沪深B股", "沪深基金", "沪深债券", "深证A股", "上证A股", "中小板", "创业板", "退市整理", "风险警示"};

        for (int i = 0 ; i < disribs.length; i++) {
            HashMap<String, Object> itemHashMap  = new HashMap<String, Object>();
            itemHashMap.put("img",icons[i]);
            itemHashMap.put("title", disribs[i]);
            dataSourceList.add(itemHashMap);
        }

        OtherAdapter2 adapter = new OtherAdapter2();
        adapter.setDatas(dataSourceList);
        mGridView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(int position) {
        StockListIntent data = new StockListIntent();
        Intent intent = new Intent();
        data.setHead1("证券名称");

        if (position == 0) {
            data.setTitle("沪深A股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "1");
            intent.putExtra("isPlateList", true);
        } else if (position == 1) {
            data.setTitle("沪深B股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "2");
            intent.putExtra("isPlateList", true);
        } else if (position == 2) {
            data.setTitle("沪深基金");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "3");
            intent.putExtra("isPlateList", true);
        } else if (position == 3) {
            data.setTitle("沪深债券");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "4");
            intent.putExtra("isPlateList", true);
        } else if (position == 4) {
            data.setTitle("深证A股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "7");
            intent.putExtra("isPlateList", true);
        } else if (position == 5) {
            data.setTitle("上证A股");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "8");
            intent.putExtra("isPlateList", true);
        } else if (position == 6) {
            data.setTitle("中小板");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "5");
            intent.putExtra("isPlateList", true);
        } else if (position == 7) {
            data.setTitle("创业板");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("type", "6");
            intent.putExtra("isPlateList", true);
        } else if (position == 8) {
            data.setTitle("退市整理");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("isRiskBackMarket", true);
            intent.putExtra("type", "2");
        } else if (position == 9) {
            data.setTitle("风险警示");
            data.setHead2("现价");
            data.setHead3("涨跌幅");
            intent.putExtra("isRiskBackMarket", true);
            intent.putExtra("type", "1");
        }

        intent.putExtra("flag", "1");
        intent.putExtra("order", "1");
        intent.putExtra("tag", "lingdie");
        intent.putExtra("stockIntent", data);
        intent.setClass(mActivity, StockListActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_other;
    }

    @Override
    public void myTabonResume() {
    }

    @Override
    public void toRunConnect() {
        if (mMarketFragmentEditIsGoneListener != null) {
            mMarketFragmentEditIsGoneListener.gone();
        }
    }

    @Override
    public void toStopConnect() {}
}
