package com.tpyzq.mobile.pangu.activity.home.information;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.HotAnalysisListViewAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;


/**
 * 热点公告解析 列表  界面
 */
public class HotAnalysisListActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG="HotAnalysisListActivity";
    private ListView mListView;
    private HotAnalysisListViewAdapter adapter;
    private ArrayList<InformationBean> analysisList;
    private ArrayList<InformationBean> beans;
    private ImageView iv_kong;

    @Override
    public void initView() {
        this.findViewById(R.id.iv_HotAnalysisListBack).setOnClickListener(this);        //返回按钮
        mListView = (ListView) this.findViewById(R.id.lvHotAnalysisList);
        iv_kong = (ImageView) this.findViewById(R.id.iv_kong);
        initData();
    }

    private void initData() {

        analysisList = (ArrayList<InformationBean>) getIntent().getSerializableExtra("analysisList");
        beans = (ArrayList<InformationBean>) getIntent().getSerializableExtra("beans");

        adapter = new HotAnalysisListViewAdapter(this);     //实例化适配器
        adapter.setList(analysisList);
        adapter.setPirceList(beans);
        mListView.setAdapter(adapter);                      //适配
        mListView.setEmptyView(iv_kong);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationBean informationBean = analysisList.get(position);
                InformationBean informationBeanPrice = beans.get(position);
                Intent intent = new Intent();
                intent.putExtra("informationBean",informationBean);
                intent.putExtra("informationBeanPrice",informationBeanPrice);
                intent.setClass(HotAnalysisListActivity.this,HotAnalysisDetailActivity.class);
                HotAnalysisListActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_analysis_list;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_HotAnalysisListBack){       //点击返回按钮销毁当前界面
            finish();
        }
    }
}
