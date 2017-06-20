package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;

/**
 * Created by 陈新宇 on 2016/8/31.
 * 基金委托方式
 */
public class FundEntrustDialog extends BaseDialog implements View.OnClickListener {
    private Button bt_true, bt_false;
    private RadioGroup rg_shangzheng;
    private RadioGroup rg_shenzheng;
    private RadioButton rb_shangzheng_1;
    private RadioButton rb_shangzheng_2;
    private RadioButton rb_shangzheng_3;
    private RadioButton rb_shenzheng_1;
    private RadioButton rb_shenzheng_2;
    private RadioButton rb_shenzheng_3;
    private RadioButton rb_shenzheng_4;
    private RadioButton rb_shenzheng_5;
    private RadioButton rb_shenzheng_6;
    private FundEntrustWays fundEntrustWays;
    private String stockcode;
    private String type;
    private String backstring;
    private TextView tv_title;

    public FundEntrustDialog(Context context, FundEntrustWays fundEntrustWays, String stockcode) {
        super(context);
        this.fundEntrustWays = fundEntrustWays;
        this.stockcode = stockcode;
        this.type = "0";
        this.backstring = "限价委托";
    }

    @Override
    public void setView() {
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_shangzheng = (RadioGroup) findViewById(R.id.rg_shangzheng);
        rg_shenzheng = (RadioGroup) findViewById(R.id.rg_shenzheng);
        rb_shangzheng_1 = (RadioButton) findViewById(R.id.rb_shangzheng_1);
        rb_shangzheng_2 = (RadioButton) findViewById(R.id.rb_shangzheng_2);
        rb_shangzheng_3 = (RadioButton) findViewById(R.id.rb_shangzheng_3);
        rb_shenzheng_1 = (RadioButton) findViewById(R.id.rb_shenzheng_1);
        rb_shenzheng_2 = (RadioButton) findViewById(R.id.rb_shenzheng_2);
        rb_shenzheng_3 = (RadioButton) findViewById(R.id.rb_shenzheng_3);
        rb_shenzheng_4 = (RadioButton) findViewById(R.id.rb_shenzheng_4);
        rb_shenzheng_5 = (RadioButton) findViewById(R.id.rb_shenzheng_5);
        rb_shenzheng_6 = (RadioButton) findViewById(R.id.rb_shenzheng_6);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fund_entrust;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
        if (!TextUtils.isEmpty(stockcode) && stockcode.startsWith("SH")){
            rg_shenzheng.setVisibility(View.GONE);
            rg_shangzheng.setVisibility(View.VISIBLE);
            tv_title.setText("委托方式(上证)");
        }else if (!TextUtils.isEmpty(stockcode) && stockcode.startsWith("SZ")){
            rg_shenzheng.setVisibility(View.VISIBLE);
            rg_shangzheng.setVisibility(View.GONE);
            tv_title.setText("委托方式(深证)");
        }
        rg_shangzheng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shangzheng_1:
                        type = "0";
                        backstring = "限价委托";
                        break;
                    case R.id.rb_shangzheng_2:
                        type = "U";
                        backstring = "市价委托";
                        break;
                    case R.id.rb_shangzheng_3:
                        type = "R";
                        backstring = "市价委托";
                        break;
                }
            }
        });
        rg_shenzheng.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shenzheng_1:
                        type = "0";
                        backstring = "限价委托";
                        break;
                    case R.id.rb_shenzheng_2:
                        type = "Q";
                        backstring = "市价委托";
                        break;
                    case R.id.rb_shenzheng_3:
                        type = "S";
                        backstring = "市价委托";
                        break;
                    case R.id.rb_shenzheng_4:
                        type = "T";
                        backstring = "市价委托";
                        break;
                    case R.id.rb_shenzheng_5:
                        type = "U";
                        backstring = "市价委托";
                        break;
                    case R.id.rb_shenzheng_6:
                        type = "V";
                        backstring = "市价委托";
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                fundEntrustWays.setItem(type);
                fundEntrustWays.setText(backstring);
                dismiss();
                break;

            case R.id.bt_false:
                dismiss();
                break;
        }
    }

    public interface FundEntrustWays {
        void setItem(String type);

        void setText(String ways);
    }
}
