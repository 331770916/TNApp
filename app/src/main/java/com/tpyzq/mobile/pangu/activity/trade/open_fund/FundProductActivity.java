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
 * 基金产品
 */
public class FundProductActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    ListView lv_fund_product;
    ArrayList<FundEntity> fundBeens;
    FundProductAdapter fundProductAdapter;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_fund_product = (ListView) findViewById(R.id.lv_fund_product);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        fundBeens = (ArrayList<FundEntity>) bundle.getSerializable("fundbean");
        int point = bundle.getInt("point");
        fundProductAdapter = new FundProductAdapter(this);
        fundProductAdapter.setData(fundBeens);
        fundProductAdapter.setPoint(point);
        lv_fund_product.setAdapter(fundProductAdapter);
        lv_fund_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("point", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_product;
    }


}
