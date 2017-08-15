package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.util.TransitionUtils;


/**
 * Created by 陈新宇 on 2016/8/31.
 * 买卖基金认购页面dialog
 */
public class CommissionedBuyAndSellDialog extends BaseDialog implements View.OnClickListener {
    private String secc_code;
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
    private TextView tv_secc_code;
    private LinearLayout ll_secc;
    private String MARKET_NAME;

    public CommissionedBuyAndSellDialog(Context context, CommissionedBuyAndSell commissionedBuyAndSell,
                                        String name, String code, String price, String num, String MARKET_NAME, String secc_code, String type, String entrustWays) {
        super(context);
        this.name = name;
        this.code = code;
        this.price = price;
        this.num = num;
        this.secc_code = secc_code;
        this.MARKET_NAME = MARKET_NAME;
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
        tv_secc_code = (TextView) findViewById(R.id.tv_secc_code);
        ll_secc = (LinearLayout) findViewById(R.id.ll_secc);
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
        if (TextUtils.isEmpty(secc_code)){
            ll_secc.setVisibility(View.GONE);
        } else {
            showSeccAccount();
        }
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

    private void showSeccAccount() {
        ll_secc.setVisibility(View.VISIBLE);
        String desc = MARKET_NAME + " " +secc_code;
        SpannableString spannableString = new SpannableString(desc);
        MyClickableSpan clickableSpan = new MyClickableSpan(desc);
        spannableString.setSpan(clickableSpan, 0, desc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_secc_code.setText(spannableString);
        tv_secc_code.setMovementMethod(LinkMovementMethod.getInstance());
        tv_secc_code.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
    }
    /*// 通过继承UnderlineSpan重写updateDrawState方法setUnderlineText(false)取消下划线
    class MyUnderlineSpan extends UnderlineSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }*/
    public void setSecc_code(String MARKET_NAME,String secc_code) {
        this.secc_code = secc_code;
        this.MARKET_NAME = MARKET_NAME;
        showSeccAccount();
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
        void showSeccDialog();
    }
    class MyClickableSpan extends ClickableSpan {

        private String content;

        public MyClickableSpan(String content) {
            this.content = content;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#1C86EE"));
            ds.setUnderlineText(true);      //设置下划线
        }

        @Override
        public void onClick(View widget) {
            commissionedBuyAndSell.showSeccDialog();
        }
    }
}