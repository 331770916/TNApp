package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OpenFundAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;


/**
 * 开放式基金
 */
public class OpenFundActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_open_fund;
    private ImageView iv_back;

    @Override
    public void initView() {
        lv_open_fund = (ListView) findViewById(R.id.lv_open_fund);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        initData();
    }

    private void initData() {
        lv_open_fund.setAdapter(new OpenFundAdapter(this));
        iv_back.setOnClickListener(this);
        lv_open_fund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, FundEntrustActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, FundHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, ShareFundActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, FundChangeActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, AccoundSearchActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, FundContractSignActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent();
                        intent.setClass(OpenFundActivity.this, FixFundListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_fund;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
