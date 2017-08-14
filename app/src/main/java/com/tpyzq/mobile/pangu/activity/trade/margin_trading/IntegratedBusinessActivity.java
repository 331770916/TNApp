package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.DealQueryActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.DebtGlideActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.DeliveryActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.EntrustedQueryActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.FinancingQActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.FinancingZActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.FinishedActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.FundDetailActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.QueryCollateralActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.QueryQTargetActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.QueryZTargetActivity;
import com.tpyzq.mobile.pangu.activity.trade.margin_trading.integrated.UnFinishedActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:融资融券 综合业务
 */

public class IntegratedBusinessActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.Layout_1).setOnClickListener(this);
        findViewById(R.id.Layout_2).setOnClickListener(this);
        findViewById(R.id.Layout_3).setOnClickListener(this);
        findViewById(R.id.Layout_4).setOnClickListener(this);
        findViewById(R.id.Layout_5).setOnClickListener(this);
        findViewById(R.id.Layout_6).setOnClickListener(this);
        findViewById(R.id.Layout_7).setOnClickListener(this);
        findViewById(R.id.Layout_8).setOnClickListener(this);
        findViewById(R.id.Layout_9).setOnClickListener(this);
        findViewById(R.id.Layout_10).setOnClickListener(this);
        findViewById(R.id.Layout_11).setOnClickListener(this);
        findViewById(R.id.Layout_12).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.Layout_1:
                startActivity(new Intent(this,EntrustedQueryActivity.class));
                break;
            case R.id.Layout_2:
                startActivity(new Intent(this,DealQueryActivity.class));
                break;
            case R.id.Layout_3:
                startActivity(new Intent(this,FundDetailActivity.class));
                break;
            case R.id.Layout_4:
                startActivity(new Intent(this,DebtGlideActivity.class));
                break;
            case R.id.Layout_5:
                startActivity(new Intent(this,DeliveryActivity.class));
                break;
            case R.id.Layout_6:
                startActivity(new Intent(this, FinancingZActivity.class));
                break;
            case R.id.Layout_7:
                startActivity(new Intent(this, FinancingQActivity.class));
                break;
            case R.id.Layout_8:
                startActivity(new Intent(this, UnFinishedActivity.class));
                break;
            case R.id.Layout_9:
                startActivity(new Intent(this, FinishedActivity.class));
                break;
            case R.id.Layout_10:
                startActivity(new Intent(this, QueryZTargetActivity.class));
                break;
            case R.id.Layout_11:
                startActivity(new Intent(this, QueryQTargetActivity.class));
                break;
            case R.id.Layout_12:
                startActivity(new Intent(this, QueryCollateralActivity.class));
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integrated;
    }
}
