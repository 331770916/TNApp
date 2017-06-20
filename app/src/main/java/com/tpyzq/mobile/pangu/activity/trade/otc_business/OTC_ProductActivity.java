package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListEntity;

import java.util.ArrayList;


/**
 * 刘泽鹏
 * OTC产品Activity
 */
public class OTC_ProductActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvOTC_Product = null;        //OTC产品列表
    private OTC_ProductAdapter adapter;
    private ImageView mNUll;

    @Override
    public void initView() {
        final ArrayList<OTC_SubscriptionListEntity> list = (ArrayList) getIntent().getSerializableExtra("list");  //拿到数据源
        int point = getIntent().getIntExtra("point", -1);

        this.findViewById(R.id.ivOTC_Product_back).setOnClickListener(this);    //返回按钮
        mNUll = (ImageView) findViewById(R.id.Null);
        lvOTC_Product = (ListView) this.findViewById(R.id.lvOTC_Product);        //展示数据的listView
        adapter = new OTC_ProductAdapter(this);                                    //实例化adapter
        adapter.setList(list);                                                   //添加数据源
        adapter.setPoint(point);
        lvOTC_Product.setEmptyView(mNUll);
        lvOTC_Product.setAdapter(adapter);                                      //适配

        //点击监听
        lvOTC_Product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_SubscriptionListEntity listIntent = list.get(position);
                listIntent.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                OTC_ProductActivity.this.finish();
            }
        });
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__product;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_Product_back) {
            this.finish();
        }
    }

}
