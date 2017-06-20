package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FundProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundEntity;

import java.util.ArrayList;

/**
 *     持仓基金
 */
public class PositionFundActivity extends BaseActivity {
    ImageView iv_back,iv_kong;
    ListView lv_fund_product;
    ArrayList<FundEntity> fundBean;
    FundProductAdapter fundProductAdapter;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        lv_fund_product = (ListView) findViewById(R.id.lv_fund_product);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        fundBean = (ArrayList<FundEntity>) bundle.getSerializable("fundbean");
        int point = bundle.getInt("point");
        fundProductAdapter = new FundProductAdapter(this);
        fundProductAdapter.setData(fundBean);
        fundProductAdapter.setPoint(point);
        lv_fund_product.setAdapter(fundProductAdapter);
        lv_fund_product.setEmptyView(iv_kong);
        lv_fund_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("point",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_position_fund;
    }
}
