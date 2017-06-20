package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_OpenAccountProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_OpenAccountEntity;

import java.util.ArrayList;

/**
 * OTC 开户 选择产品公司的界面
 * 刘泽鹏
 */
public class OTC_OpenAccountProductActivity extends BaseActivity implements View.OnClickListener{

    private ListView mListView=null;
    private OTC_OpenAccountProductAdapter adapter;
    private ImageView tvOpenAccountKong;

    @Override
    public void initView() {
        final ArrayList<OTC_OpenAccountEntity> list = (ArrayList) getIntent().getSerializableExtra("list");
        int point = getIntent().getIntExtra("point",-1);
        this.findViewById(R.id.ivOTC_OpenAccountProduct_back).setOnClickListener(this);
        tvOpenAccountKong = (ImageView) this.findViewById(R.id.tvOpenAccountKong);  //空图片
        mListView = (ListView) this.findViewById(R.id.lvOTC_OpenAccountProduct);

        if(list.size() == 0){
            tvOpenAccountKong.setVisibility(View.VISIBLE);      //如果 没有  显示 空
        }

        adapter = new OTC_OpenAccountProductAdapter(this);
        adapter.setPoint(point);
        adapter.setList(list);
        mListView.setAdapter(adapter);
        //给listView 添加条目点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_OpenAccountEntity intentBean = list.get(position);
                intentBean.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                OTC_OpenAccountProductActivity.this.finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__open_account_product;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivOTC_OpenAccountProduct_back){
            this.finish();
        }
    }
}
