package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.ChooseRSAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

public class ChooseRelationshipActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lv_content;
    private ChooseRSAdapter chooseRSAdapter;
    private int point;

    @Override
    public void initView() {

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("创业板");

        lv_content = (ListView) findViewById(R.id.lv_content);
        initData();
    }

    private void initData() {
        chooseRSAdapter = new ChooseRSAdapter(this);
        lv_content.setAdapter(chooseRSAdapter);
        Intent intent = getIntent();
        point = intent.getIntExtra("point", 0);
        chooseRSAdapter.setPoint(point);
        lv_content.setOnItemClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_relationship;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                setFinish();
                break;
        }
    }

    private void setFinish(){
        Intent intent = new Intent();
        intent.putExtra("point", point);
        setResult(100,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setFinish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        point = position;
        chooseRSAdapter.setPoint(point);
        setFinish();
    }
}
