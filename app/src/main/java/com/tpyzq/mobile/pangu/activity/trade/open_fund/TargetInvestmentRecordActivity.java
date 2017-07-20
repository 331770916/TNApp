package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.TragetRecordItemAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/20.
 * 定投记录
 */

public class TargetInvestmentRecordActivity extends BaseActivity implements View.OnClickListener{

    private TragetRecordItemAdapter mAdapter;
    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("定投记录");

        ListView listView = (ListView) findViewById(R.id.targetRecordLv);
        mAdapter = new TragetRecordItemAdapter(this);
        listView.setAdapter(mAdapter);

        mAdapter.setDatas(getData());

    }

    private List<Map<String, String>> getData() {
        ArrayList<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, String> data = new HashMap<>();
            data.put(TragetRecordItemAdapter.KEY_DATE, "2017-7-20");
            data.put(TragetRecordItemAdapter.KEY_PRICE, "100.00");
            if (i % 3 == 0) {
                data.put(TragetRecordItemAdapter.KEY_DISPOSE, "1");
            } else {
                data.put(TragetRecordItemAdapter.KEY_DISPOSE, "2");
            }
            datas.add(data);
        }
        return datas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_target_record;
    }
}
