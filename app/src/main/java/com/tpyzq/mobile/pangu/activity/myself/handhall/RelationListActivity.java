package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.ProfessionAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqi on 2016/9/17.
 * 关系 选择
 */
public class RelationListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<String> list,clist;
    private ProfessionAdapter adapter;
    private ListView mListView;

    @Override
    public void initView() {
        TextView TitleName = (TextView) findViewById(R.id.TitleName);
        TitleName.setText("关系");
        findViewById(R.id.Profession_back).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.Profession_ListView);
        initData();
        Logic();
    }


    private void initData() {
        list = new ArrayList<>();
        list.add("父母");
        list.add("夫妻");
        list.add("子女");
        list.add("朋友");
        list.add("其它");
        clist = new ArrayList<>();
        clist.add("1");
        clist.add("2");
        clist.add("3");
        clist.add("4");
        clist.add("5");
    }

    private void Logic() {
        int point = getIntent().getIntExtra("point", -1);
        String RelationListView =getIntent().getStringExtra("RelationListView");
        adapter = new ProfessionAdapter(this,list,clist);
        if (point >= 0) {
            adapter.setPoint(point);
        }
        adapter.setPointVisibility(RelationListView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_professionlistview;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Profession_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("keyName", list.get(position).toString());
        intent.putExtra("keyCode",clist.get(position).toString());
        intent.putExtra("keyint", "1");
        intent.putExtra("point", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
