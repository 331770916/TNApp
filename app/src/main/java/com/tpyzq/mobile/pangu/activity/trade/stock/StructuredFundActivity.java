package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.StockPageListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;

/**
 * Created by wangqi on 2017/6/22.
 * 盘后分级基金
 */

public class StructuredFundActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_stock_list;
    private TextView tv_title;
    private ImageView iv_back;

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        lv_stock_list = (ListView) findViewById(R.id.lv_stock_list);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        initData();
    }

    private void initData() {
        tv_title.setText(DataUtils.stock_morelist_name[2]);
        lv_stock_list.setAdapter(new StockPageListAdapter(this, DataUtils.structuredfunda_name, DataUtils.structuredfunda_icon));
        lv_stock_list.setOnItemClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tran_more;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
//                Helper.getInstance().showToast(this,"正在开发中");
                intent.setClass(this, FJFundGradingMergerActivity.class);
                break;
            case 1:
//                Helper.getInstance().showToast(this,"正在开发中");
                intent.setClass(this, FJFundSplitActivity.class);
                break;
            case 2:
                Helper.getInstance().showToast(this, "正在开发中");
                break;
            case 3:
                Helper.getInstance().showToast(this, "正在开发中");
                break;
            case 4:
                Helper.getInstance().showToast(this, "正在开发中");
                break;
        }
        startActivity(intent);
    }
}
