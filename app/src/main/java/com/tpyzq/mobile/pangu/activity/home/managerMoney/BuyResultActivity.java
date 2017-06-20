package com.tpyzq.mobile.pangu.activity.home.managerMoney;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;

import java.util.Date;

/**
 * Created by zhangwenbo on 2016/10/9.
 * 购买结果
 */
public class BuyResultActivity extends BaseActivity implements View.OnClickListener{
    TextView tv_product_name;
    TextView tv_product_product;
    ImageView iv_back;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_product = (TextView) findViewById(R.id.tv_product_product);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String schema_id = intent.getStringExtra("schema_id");
        String prod_code = intent.getStringExtra("prod_code");
        String price = intent.getStringExtra("price");
        String type = intent.getStringExtra("type");
        String serial_no = intent.getStringExtra("SERIAL_NO");
        BRutil.menuNewSelect("Z1-4-4", schema_id, prod_code, "3", new Date(), serial_no, price);
        if (!TextUtils.isEmpty(name)){
            tv_product_name.setText(name);
        }
        if (!TextUtils.isEmpty(price)){
            tv_product_product.setText(price+"元");
        }
        if (TextUtils.isEmpty(type)){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        finish();
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_buyresult;
    }
}
