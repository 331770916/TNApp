package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/8/31.
 * 基金认购页面dialog
 */
public class FundWithDrawDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_fund_name, tv_fund_code, tv_fund_price, tv_stock_num, tv_fund_money;
    private Button bt_true, bt_false;
    private String name;
    private String code;
    private String way;
    private String num;
    private String money;
    private String time;
    private String number;
    private  FundWithDrawListen fundWithDrawListen;

    public FundWithDrawDialog(Context context, FundWithDrawListen fundWithDrawListen, String name, String code, String way, String num, String money, String time, String number) {
        super(context);
        this.name = name;
        this.code = code;
        this.way = way;
        this.num = num;
        this.money = money;
        this.time = time;
        this.number = number;
        this.fundWithDrawListen = fundWithDrawListen;
    }

    @Override
    public void setView() {
        tv_fund_name = (TextView) findViewById(R.id.tv_stock_name);
        tv_fund_code = (TextView) findViewById(R.id.tv_stock_code);
        tv_fund_price = (TextView) findViewById(R.id.tv_stock_price);
        tv_stock_num = (TextView) findViewById(R.id.tv_stock_num);
        tv_fund_money = (TextView) findViewById(R.id.tv_fund_money);
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fund_withdraw;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
        tv_fund_name.setText(name);
        tv_fund_code.setText(time);
        tv_fund_price.setText(way);
        tv_stock_num.setText(num);
        tv_fund_money.setText(money);
    }


    /**
     * 撤单
     */
    private void setBuy(String FUND_CODE, String ORDER_DATE, String ENTRUST_NO) {
//        Toast.makeText(BuyAndSellActivity.this, "网络访问开始", Toast.LENGTH_SHORT).show();
        HashMap map720204 = new HashMap();
        map720204.put("funcid", "720204");
        map720204.put("token", SpUtils.getString(context, "mSession", null));
        HashMap map720204_1 = new HashMap();
        map720204_1.put("SEC_ID", "tpyzq");
        map720204_1.put("ENTRUST_NO", ENTRUST_NO);
        map720204_1.put("ORDER_DATE", ORDER_DATE);
        map720204_1.put("FUND_CODE", FUND_CODE);
        map720204_1.put("FLAG", true);
        map720204.put("parms", map720204_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720204, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");

                    if ("0".equals(code)) {
                        CentreToast.showText(context,"委托已提交",true);
                        fundWithDrawListen.setClear();
                    } else {
                        MistakeDialog.showDialog(msg, (Activity) context);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                setBuy(code, time, number);
                dismiss();
                break;

            case R.id.bt_false:
                dismiss();
                break;
        }
    }
    public interface FundWithDrawListen{
        void setClear();
    }
}
