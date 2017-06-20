package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.util.TransitionUtils;


/**
 * Created by 陈新宇 on 2016/8/31.
 * 买卖基金认购页面dialog
 */
public class CommissionedBuyAndSellDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_fund_name, tv_fund_code, tv_fund_price, tv_stock_num;
    private Button bt_true, bt_false;
    private String name;
    private String code;
    private String price;
    private String num;
    private CommissionedBuyAndSell commissionedBuyAndSell;
    private String type;
    private String entrustWays;
    private TextView tv_title;

    public CommissionedBuyAndSellDialog(Context context, CommissionedBuyAndSell commissionedBuyAndSell, String name, String code, String price, String num, String type, String entrustWays) {
        super(context);
        this.name = name;
        this.code = code;
        this.price = price;
        this.num = num;
        this.commissionedBuyAndSell = commissionedBuyAndSell;
        this.entrustWays = entrustWays;
        this.type = type;
    }

    @Override
    public void setView() {
        tv_fund_name = (TextView) findViewById(R.id.tv_stock_name);
        tv_fund_code = (TextView) findViewById(R.id.tv_stock_code);
        tv_fund_price = (TextView) findViewById(R.id.tv_stock_price);
        tv_stock_num = (TextView) findViewById(R.id.tv_stock_num);
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_commissioned_buy;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
        tv_fund_name.setText(name);
        tv_fund_code.setText(code);
        if ("买".equals(type)) {
            tv_title.setText("委托买入");
        } else if ("卖".equals(type)) {
            tv_title.setText("委托卖出");
        }
        if (!TextUtils.isEmpty(entrustWays)){
            tv_fund_price.setText(entrustWays);
        }else {
            tv_fund_price.setText(TransitionUtils.string2doubleS(price));
        }
        tv_stock_num.setText(TransitionUtils.string2doubleS4(num));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                if ("买".equals(type)) {
                    commissionedBuyAndSell.setBuy(code, price, num);
                } else if ("卖".equals(type)) {
                    commissionedBuyAndSell.setSell(code, price, num);
                }
                dismiss();
                break;

            case R.id.bt_false:
                dismiss();
                break;
        }
    }

    public interface CommissionedBuyAndSell {
        void setBuy(String code, String price, String num);

        void setSell(String code, String price, String num);
    }
}