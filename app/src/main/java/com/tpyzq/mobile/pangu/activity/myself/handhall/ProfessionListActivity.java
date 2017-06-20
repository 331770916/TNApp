package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.ProfessionAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqi on 2016/9/16.
 * 职业 选择
 */
public class ProfessionListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<String> namelist,codeList;
    private ProfessionAdapter adapter;

    @Override
    public void initView() {
        findViewById(R.id.Profession_back).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.Profession_ListView);
        initData();
        Logic();
    }

    private void Logic() {
        int point = getIntent().getIntExtra("point", -1);
        String Profession = getIntent().getStringExtra("Profession");
        adapter = new ProfessionAdapter(this,namelist,codeList);
        if (point >= 0) {
            adapter.setPoint(point);
        }
        adapter.setPointVisibility(Profession);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        namelist = new ArrayList<>();
        namelist.add("文教科卫专业人员");
        namelist.add("党政 ( 在职，离退休 ) 机关干部");
        namelist.add("企事业单位干部");
        namelist.add("行政企事业单位工人");
        namelist.add("农民");
        namelist.add("个体");
        namelist.add("无业");
        namelist.add("军人");
        namelist.add("学生");
        namelist.add("证券从业人员");
        namelist.add("离退休");
        namelist.add("其他");

        codeList = new ArrayList<>();
        codeList.add("01");
        codeList.add("02");
        codeList.add("03");
        codeList.add("04");
        codeList.add("05");
        codeList.add("06");
        codeList.add("07");
        codeList.add("08");
        codeList.add("09");
        codeList.add("10");
        codeList.add("11");
        codeList.add("99");
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
        intent.putExtra("keyName", namelist.get(position).toString());
        intent.putExtra("keyCode", codeList.get(position).toString());
        intent.putExtra("keyint", "1");
        intent.putExtra("point", position);
        setResult(RESULT_OK, intent);
        finish();
    }

}
