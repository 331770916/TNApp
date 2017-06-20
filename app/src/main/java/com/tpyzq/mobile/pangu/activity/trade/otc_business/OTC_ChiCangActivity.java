package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_RedeemChiCangAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_RedeemEntity;

import java.util.ArrayList;

/**
 * OTC 我的持仓界面
 * 刘泽鹏
 */
public class OTC_ChiCangActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvOTC_RedeemProduct=null;
    private OTC_RedeemChiCangAdapter adapter;
    private ImageView iv_empty;

    @Override
    public void initView() {
        final ArrayList<OTC_RedeemEntity> list = (ArrayList) getIntent().getSerializableExtra("list");
        int point = getIntent().getIntExtra("point",-1);
        lvOTC_RedeemProduct= (ListView) this.findViewById(R.id.lvOTC_RedeemProduct);       //展示数据的listView
        iv_empty= (ImageView) this.findViewById(R.id.iv_empty);       //展示数据的listView
        adapter = new OTC_RedeemChiCangAdapter(this);                                           //初始化适配器
        adapter.setList(list);                                                                     //添加数据
        adapter.setPoint(point);
        this.lvOTC_RedeemProduct.setAdapter(adapter);                                           //适配
        lvOTC_RedeemProduct.setEmptyView(iv_empty);
        this.findViewById(R.id.ivOTC_Redeem_back).setOnClickListener(this);                  //给返回按钮添加点击监听
        //item 点击监听
        this.lvOTC_RedeemProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_RedeemEntity intentBean = list.get(position);
                intentBean.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                OTC_ChiCangActivity.this.finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__chi_cang;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivOTC_Redeem_back){
            this.finish();
        }
    }
}
