package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ContractFlowWaterAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;

import java.util.ArrayList;


/**
 * OTC 产品合同流水 界面
 */
public class OTC_ContractFlowWaterActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private OTC_ContractFlowWaterAdapter adapter;
    private ImageView ivOtcContractKong;        //空图片

    @Override
    public void initView() {
        ArrayList<OTC_ElectronicContractEntity> list = (ArrayList<OTC_ElectronicContractEntity>) getIntent().getSerializableExtra("list");
        this.findViewById(R.id.ivOTC_ContractFlowWater_back).setOnClickListener(this);
        ivOtcContractKong = (ImageView) this.findViewById(R.id.ivOtcContractKong);
        mListView = (ListView) this.findViewById(R.id.lvContractFlowWater);

        if(list.size() == 0){
            ivOtcContractKong.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else if(list.size()>0){
            ivOtcContractKong.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        adapter = new OTC_ContractFlowWaterAdapter(this);
        adapter.setList(list);
        mListView.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__contract_flow_water;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivOTC_ContractFlowWater_back){
            this.finish();
        }
    }
}
