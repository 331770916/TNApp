package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FundProduct2Adapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundChangeEntity;

import java.util.ArrayList;


/**
 *     持仓基金
 */
public class PositionFund2Activity extends BaseActivity {
    ImageView iv_back,iv_kong;
    TextView tv_title;
    ListView lv_fund_product;
    ArrayList<FundChangeEntity> fundChangeBeans;
    FundProduct2Adapter fundProductAdapter;
    private ImageView mEmptyIv;
    @Override
    public void initView() {
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mEmptyIv = (ImageView) findViewById(R.id.iv_positionFund);
        lv_fund_product = (ListView) findViewById(R.id.lv_fund_product);
        tv_title.setText("可转换基金");
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
        fundChangeBeans = (ArrayList<FundChangeEntity>) bundle.getSerializable("fundChangeBeans");
        int point = bundle.getInt("point2");
        fundProductAdapter = new FundProduct2Adapter(this);

        if (fundChangeBeans == null || fundChangeBeans.size() <= 0) {
            mEmptyIv.setVisibility(View.VISIBLE);
        }

        fundProductAdapter.setData(fundChangeBeans);
        fundProductAdapter.setPoint(point);
        lv_fund_product.setAdapter(fundProductAdapter);
        lv_fund_product.setEmptyView(iv_kong);
        lv_fund_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("point2",position);
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
