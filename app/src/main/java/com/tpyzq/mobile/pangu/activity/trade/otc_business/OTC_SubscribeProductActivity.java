package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_SubscribeProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeEntity;

import java.util.ArrayList;

/**
 * OTC 申购选择OTC 产品界面
 * 刘泽鹏
 */
public class OTC_SubscribeProductActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvOTC_SGProduct = null;
    private OTC_SubscribeProductAdapter adapter;
    private ImageView mNull;

    @Override
    public void initView() {
        final ArrayList<OTC_SubscribeEntity> list = (ArrayList) getIntent().getSerializableExtra("list");
        int point = getIntent().getIntExtra("point", -1);
        this.lvOTC_SGProduct = (ListView) this.findViewById(R.id.lvOTC_SGProduct);       //展示数据的listView
        mNull = (ImageView) findViewById(R.id.Null);
        adapter = new OTC_SubscribeProductAdapter(this);                                  //初始化适配器
        adapter.setList(list);                                                             //添加数据
        adapter.setPoint(point);
        lvOTC_SGProduct.setEmptyView(mNull);
        this.lvOTC_SGProduct.setAdapter(adapter);                                        //适配
        this.findViewById(R.id.ivOTC_SGProduct_back).setOnClickListener(this);           //给返回按钮添加点击监听
        //item 点击监听
        this.lvOTC_SGProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_SubscribeEntity intentBean = list.get(position);
                intentBean.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                OTC_SubscribeProductActivity.this.finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__subscribe_product;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_SGProduct_back) {
            this.finish();
        }
    }
}
