package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FundCompanyAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundEntity;

import java.util.ArrayList;


/**
 * 基金公司
 */
public class FundCompanyActivity extends BaseActivity implements View.OnClickListener{
    ImageView iv_back;
    ListView lv_fund_company;
    ArrayList<FundEntity> fundBeens;
    FundCompanyAdapter fundCompanyAdapter;
    ImageView iv_kong;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        lv_fund_company = (ListView) findViewById(R.id.lv_fund_company);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        fundBeens = (ArrayList<FundEntity>) bundle.getSerializable("fundbean");
        int point = bundle.getInt("point");
        fundCompanyAdapter = new FundCompanyAdapter(this);
        fundCompanyAdapter.setData(fundBeens);
        fundCompanyAdapter.setPoint(point);
        lv_fund_company.setAdapter(fundCompanyAdapter);
        lv_fund_company.setEmptyView(iv_kong);
        lv_fund_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        return R.layout.activity_fund_company;
    }
}
