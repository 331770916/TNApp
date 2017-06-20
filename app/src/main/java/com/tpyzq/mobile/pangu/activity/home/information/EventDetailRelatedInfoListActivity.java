package com.tpyzq.mobile.pangu.activity.home.information;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.EventDetailRelatedInfoAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformationBean;

import java.util.ArrayList;


/**
 * 重大事件详情页面   下边的相关数据  点击更多跳转的  相关资讯列表页面
 */
public class EventDetailRelatedInfoListActivity extends BaseActivity implements View.OnClickListener{

    private ListView mListView;
    private EventDetailRelatedInfoAdapter adapter;

    @Override
    public void initView() {
        final ArrayList<InformationBean> list = (ArrayList<InformationBean>) getIntent().getSerializableExtra("list");
        mListView = (ListView) this.findViewById(R.id.lvRelatedInfo);
        adapter = new EventDetailRelatedInfoAdapter(this);
        adapter.setList(list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationBean informationBean = list.get(position);
                String requestId = informationBean.getRequestId();
                Intent intent = new Intent();
                intent.setClass(EventDetailRelatedInfoListActivity.this, NewsDetailActivity.class);
                intent.putExtra("requestId",requestId);
                startActivity(intent);
            }
        });

        this.findViewById(R.id.iv_RelatedInfoBack).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_event_detail_related_info_list;
    }

    @Override
    public void onClick(View v) {
        if(R.id.iv_RelatedInfoBack == v.getId()){
            finish();
        }
    }
}
