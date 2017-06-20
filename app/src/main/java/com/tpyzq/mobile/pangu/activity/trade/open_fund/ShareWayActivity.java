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

public class ShareWayActivity extends BaseActivity {
    ImageView iv_back;
    ListView lv_huge_redemption;
    FundProductAdapter fundProductAdapter;
    String[] item = {"现金分红","份额分红"};
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_huge_redemption = (ListView) findViewById(R.id.lv_huge_redemption);
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
        int point = bundle.getInt("way");
        fundProductAdapter = new FundProductAdapter(this);
        fundProductAdapter.setWay(item);
        fundProductAdapter.setPoint(point);
        lv_huge_redemption.setAdapter(fundProductAdapter);
        lv_huge_redemption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("way",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_way;
    }
}
