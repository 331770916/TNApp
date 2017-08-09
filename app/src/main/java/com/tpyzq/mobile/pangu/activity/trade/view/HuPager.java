package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.BaseTraPager;
import com.tpyzq.mobile.pangu.activity.trade.presenter.TraChooseBreedActivityPresenter;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReverseRepoActivity;
import com.tpyzq.mobile.pangu.adapter.trade.PagerHuAdapter;
import com.tpyzq.mobile.pangu.data.StockInfoBean;

import java.util.List;

/**
 * anthor:Created by tianchen on 2017/4/1.
 * email:963181974@qq.com
 */

public class HuPager extends BaseTraPager implements AdapterView.OnItemClickListener {

    private ListView lv_hushen;
    private PagerHuAdapter pagerHuAdapter;
    private ImageView iv_kong;
    private TraChooseBreedActivityPresenter presenter;
    private List<StockInfoBean> datas;

    private HuPager(Context context) {
        super(context);
    }

    public static HuPager newInstance(Context context, TraChooseBreedActivityPresenter presenter) {
        HuPager huPager = new HuPager(context);
        huPager.presenter = presenter;
        return huPager;
    }

    @Override
    public void setView() {
        lv_hushen = (ListView) rootView.findViewById(R.id.lv_hushen);
        TextView tv_text1 = (TextView) rootView.findViewById(R.id.tv_text1);
        TextView tv_text2 = (TextView) rootView.findViewById(R.id.tv_text2);
        TextView tv_text3 = (TextView) rootView.findViewById(R.id.tv_text3);
        TextView tv_text4 = (TextView) rootView.findViewById(R.id.tv_text4);
        iv_kong = (ImageView) rootView.findViewById(R.id.iv_kong);
        tv_text1.setText("品种");
        tv_text2.setText("年收益率");
        tv_text3.setText("万元日收益");
        tv_text4.setText("10万元收益");
    }

    @Override
    public void setData(List<StockInfoBean> data) {
        datas = data;
        pagerHuAdapter.setData(datas);
    }

    @Override
    public void initData() {
        pagerHuAdapter = PagerHuAdapter.getPagerHuAdapter(mContext);
        lv_hushen.setAdapter(pagerHuAdapter);
        lv_hushen.setEmptyView(iv_kong);
        lv_hushen.setOnItemClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_hu_shen;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(mContext, ReverseRepoActivity.class);
        intent.putExtra("data", datas.get(position));
        presenter.getActivity().startActivity(intent);
    }
}
