package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.presenter.ReverseRepoActivityPresenter;
import com.tpyzq.mobile.pangu.adapter.trade.BuySellAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoBean;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CommissionedBuyAndSellDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.tpyzq.mobile.pangu.util.TransitionUtils.fundPirce;
import static com.tpyzq.mobile.pangu.util.TransitionUtils.string2doubleS3;

public class ReverseRepoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tv_title, tv_using_money, tv_price, tv_chedan, tv_search, tv_sub_price, tv_add_price;
    private ImageView iv_sub_price, iv_add_price, iv_sub_sum, iv_add_sum, iv_back;
    public String[] price_buy = new String[5];
    public String[] price_sell = new String[5];
    public String[] sum_buy = new String[5];
    public String[] sum_sell = new String[5];
    private ListView lv_buy, lv_sell;
    private BuySellAdapter buyAdapter;
    private BuySellAdapter sellAdapter;
    private StockInfoBean stockInfoBean;
    private EditText et_income, et_price;
    private TextView mSyTv;
    private double price;
    private long amount;
    private long finalamount;
    private TimeCount timeCount;
    private int addsub;

    ReverseRepoActivityPresenter presenter;
    private LinearLayout ll_mKeyboard;

    private LinearLayout mRepoLayout;
    private TextView     mRepoTextView;
    private DecimalFormat mFormat2 = new DecimalFormat("#0.00");

    @Override
    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_using_money = (TextView) findViewById(R.id.tv_using_money);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_sub_price = (TextView) findViewById(R.id.tv_sub_price);
        tv_add_price = (TextView) findViewById(R.id.tv_add_price);
        tv_chedan = (TextView) findViewById(R.id.tv_chedan);
        tv_search = (TextView) findViewById(R.id.tv_search);
        findViewById(R.id.bt_loan).setOnClickListener(this);
        lv_buy = (ListView) findViewById(R.id.lv_buy);
        lv_sell = (ListView) findViewById(R.id.lv_sell);
        et_income = (EditText) findViewById(R.id.et_income);
        et_price = (EditText) findViewById(R.id.et_price);
        iv_sub_price = (ImageView) findViewById(R.id.iv_sub_price);
        iv_add_price = (ImageView) findViewById(R.id.iv_add_price);
        iv_sub_sum = (ImageView) findViewById(R.id.iv_sub_sum);
        iv_add_sum = (ImageView) findViewById(R.id.iv_add_sum);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_mKeyboard = (LinearLayout) findViewById(R.id.Keyboard_LinearLayout);
        mSyTv = (TextView) findViewById(R.id.syTv);

        mRepoLayout = (LinearLayout) findViewById(R.id.reverse_repoLayout);
        mRepoTextView = (TextView) findViewById(R.id.reverse_daytv);

        initData();
        testData();
    }

    private void testData() {

        boolean occupied = false;
        boolean sqtime = false;
        boolean dqtime = false;
        boolean djtime = false;

        if (!TextUtils.isEmpty(stockInfoBean.occupied_days)) {
            occupied = true;
            mRepoTextView.setText(stockInfoBean.occupied_days);

            String day = stockInfoBean.occupied_days + "天";
            String repoText = "占款天数\n";
            String strRepoText = repoText + day;
            SpannableString ss = new SpannableString(strRepoText);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.title_list)), 0, repoText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.red)), repoText.length(), strRepoText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(12, true), 0, repoText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(14, true), repoText.length(), strRepoText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRepoTextView.setGravity(Gravity.CENTER);
            mRepoTextView.setText(ss);

        }

        CheckedTextView jkTitleTv = (CheckedTextView) findViewById(R.id.jktitle);
        CheckedTextView jktime = (CheckedTextView)findViewById(R.id.jktime);

        if (!TextUtils.isEmpty(stockInfoBean.sq_time)) {
            sqtime = true;
            jkTitleTv.setText("借款");
            jktime.setText(stockInfoBean.sq_time);
        }

        CheckedTextView kyTitleTv = (CheckedTextView) findViewById(R.id.kytitle);
        CheckedTextView kytime = (CheckedTextView)findViewById(R.id.kytime);
        if (!TextUtils.isEmpty(stockInfoBean.dq_time)) {
            dqtime = true;
            kyTitleTv.setText("可用");
            kytime.setText(stockInfoBean.dq_time);
        }

        CheckedTextView kqTitleTv = (CheckedTextView) findViewById(R.id.kqtitle);
        CheckedTextView kqtime = (CheckedTextView)findViewById(R.id.kqtime);

        if (!TextUtils.isEmpty(stockInfoBean.dj_time)) {
            djtime = true;
            kqTitleTv.setText("可取");
            kqtime.setText(stockInfoBean.dj_time);
        }

        if (occupied && sqtime && dqtime && djtime) {
            mRepoLayout.setVisibility(View.VISIBLE);
        } else {
            mRepoLayout.setVisibility(View.GONE);
        }
    }

    private void initData() {

        presenter = new ReverseRepoActivityPresenter(this);

        Intent intent = getIntent();
        stockInfoBean = (StockInfoBean) intent.getSerializableExtra("data");
        if (stockInfoBean == null) {
            return;
        }

        tv_title.setText(stockInfoBean.stockName2 + "天期(" + stockInfoBean.stockName1 + ")");
        buyAdapter = new BuySellAdapter(this, price_buy, sum_buy, "0");
        sellAdapter = new BuySellAdapter(this, price_sell, sum_sell, "1");
        lv_buy.setAdapter(buyAdapter);
        lv_sell.setAdapter(sellAdapter);
        timeCount = new TimeCount(Helper.getTime(), 1000);
        presenter.getHeaderView(stockInfoBean.stockCode);
        presenter.getAmount(stockInfoBean.stockCode);
        if (stockInfoBean.stockCode.startsWith("1")) {
            addsub = 100000;
        } else if (stockInfoBean.stockCode.startsWith("2")) {
            addsub = 1000;
        }
        tv_sub_price.setText(addsub + "");
        tv_add_price.setText(addsub + "");
        timeCount.start();
        tv_chedan.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_sub_price.setOnClickListener(this);
        iv_add_price.setOnClickListener(this);
        iv_sub_sum.setOnClickListener(this);
        iv_add_sum.setOnClickListener(this);
        et_income.addTextChangedListener(stockPriceWatch);
        et_price.addTextChangedListener(stockNumWatch);
        lv_buy.setOnItemClickListener(this);
        lv_sell.setOnItemClickListener(this);



    }

    /**
     * 价格变化监听器
     */
    private TextWatcher stockPriceWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();
            if (Helper.isDecimal(numString)) {
                if (TextUtils.isEmpty(numString) || "0.000".equals(numString) || ".".equals(numString) || "- -".equals(numString)) {
                    price = 0;
                    mSyTv.setText("0.00");
                } else {
                    double numInt = Double.parseDouble(numString);
                    if (numInt < 0) {
                        CentreToast.showText(getApplication(), "请输入一个大于0的数字");
                        et_income.setText("");
                        mSyTv.setText("0.00");
                    } else {
                        price = numInt;
                        getYqsy();
                    }
                }
            } else {
                mSyTv.setText("0.00");
            }
        }
    };

    /**
     * 获取预期收益
     */
    private void getYqsy() {
        String numString = et_income.getText().toString();
        String outPrice = et_price.getText().toString();
        String day = stockInfoBean.occupied_days;
        if (TextUtils.isEmpty(numString)) {
            numString = "0";
        }

        if (TextUtils.isEmpty(outPrice)) {
            outPrice = "0";
        }

        if (TextUtils.isEmpty(day)) {
            day = "0";
        }


        BigDecimal bigDecima0 = new BigDecimal((Double.parseDouble(numString)/ 100));
        BigDecimal bigDecimal = new BigDecimal(outPrice);
        BigDecimal bigDecima2 = new BigDecimal(day);

        Double result = (bigDecima0.doubleValue() / 365) * bigDecimal.doubleValue() * bigDecima2.doubleValue();
        String value = TransitionUtils.string2doubleS(result+"");
        String unit = "元";

        SpannableString ss1 = new SpannableString(value + unit);
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomApplication.getContext(), R.color.title_list)), value.length(), (value + unit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSyTv.setText(ss1);
    }

    /**
     * 数量变化监听器
     */
    private TextWatcher stockNumWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();
            if (numString.startsWith("0")) {
                CentreToast.showText(ReverseRepoActivity.this, "交易数量首位不能为0");
                et_price.setText(TransitionUtils.delHeaderZero(numString));
                mSyTv.setText("0.00");
                return;
            }
            if (numString == null || numString.equals("")) {
                amount = 0;
                mSyTv.setText("0.00");
            } else {

                getYqsy();

                long numInt = Long.parseLong(numString);
                if (numInt < 0) {
                    CentreToast.showText(getApplication(), "请输入一个大于0的数字");
                    mSyTv.setText("0.00");
                    et_price.setText("");
                } else {
                    amount = numInt;
                }
            }
        }
    };

    public void setAdapter(JSONArray jsArray) {
        try {
            String code = stockInfoBean.stockCode;
            price_sell[0] = fundPirce(code, jsArray.getString(29));
            price_sell[1] = fundPirce(code, jsArray.getString(28));
            price_sell[2] = fundPirce(code, jsArray.getString(27));
            price_sell[3] = fundPirce(code, jsArray.getString(26));
            price_sell[4] = fundPirce(code, jsArray.getString(25));
            sum_sell[0] = fundPirce(code, jsArray.getString(39));
            sum_sell[1] = fundPirce(code, jsArray.getString(38));
            sum_sell[2] = fundPirce(code, jsArray.getString(37));
            sum_sell[3] = fundPirce(code, jsArray.getString(36));
            sum_sell[4] = fundPirce(code, jsArray.getString(35));
            price_buy[0] = fundPirce(code, jsArray.getString(20));
            price_buy[1] = fundPirce(code, jsArray.getString(21));
            price_buy[2] = fundPirce(code, jsArray.getString(22));
            price_buy[3] = fundPirce(code, jsArray.getString(23));
            price_buy[4] = fundPirce(code, jsArray.getString(24));
            sum_buy[0] = fundPirce(code, jsArray.getString(30));
            sum_buy[1] = fundPirce(code, jsArray.getString(31));
            sum_buy[2] = fundPirce(code, jsArray.getString(32));
            sum_buy[3] = fundPirce(code, jsArray.getString(33));
            sum_buy[4] = fundPirce(code, jsArray.getString(34));
            sellAdapter.setYesterdayPrice(jsArray.getString(2), code);
            buyAdapter.setYesterdayPrice(jsArray.getString(2), code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    boolean flag = false;

    public void setTextView(String a, String b) {
        tv_using_money.setText(b);
        if (!flag) {
            price = Double.valueOf(a);
            et_income.setText(a);
            flag = true;
        }
    }

    public void setPrice(int price) {
        finalamount = price;
        tv_price.setText(price + "");
    }

    public void setClearView() {
        et_price.setText("");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reverse_repo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sub_price:
                price -= 0.001;
                et_income.setText(string2doubleS3(price + ""));
                break;
            case R.id.iv_add_price:
                price += 0.001;
                et_income.setText(string2doubleS3(price + ""));
                break;
            case R.id.iv_sub_sum:
                amount -= addsub;
                et_price.setText(amount + "");
                break;
            case R.id.bt_loan:
                if (!TextUtils.isEmpty(et_price.getText().toString()) && !TextUtils.isEmpty(et_income.getText().toString())) {
//                    //获取最大可借出金额
                    String tempPrice = tv_price.getText().toString();
                    if (Helper.isNumberDimc(tempPrice)) {//判断文本是否为数字
                        long maxPrice = Long.parseLong(tempPrice);
                        if (amount > maxPrice) {
                            //输入的钱大于最大可借出
                            CentreToast.showText(this, "你的最大可借出数量为" + maxPrice + "\n\r请确认后重新输入");
                        } else {
//                输入钱小于最大可借出
                            String result = stockInfoBean.stockCode.substring(2, stockInfoBean.stockCode.length());
                            CommissionedBuyAndSellDialog commissionedBuyAndSellDialog = new CommissionedBuyAndSellDialog(this, commissionedBuyAndSell, stockInfoBean.stockName1, result, price + "", et_price.getText().toString(),"","", "卖", et_income.getText().toString());
                            commissionedBuyAndSellDialog.show();
                        }

                    } else {//最大可借出为"- -"
                        CentreToast.showText(this, "你的最大可借出数量为0\n\r请确认后重新输入");
                    }
                } else {
                    CentreToast.showText(this, "年利率或金额不能为空");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_chedan:
                startActivity(new Intent(this, RevokeActivity.class));
                break;
            case R.id.tv_search:
                startActivity(new Intent(this, ReferActivity.class));
                break;
            case R.id.iv_add_sum:
//                if (Helper.isNumberDimc(tv_price.getText().toString()) && amount < Integer.valueOf(tv_price.getText().toString())) {
                if (Helper.isNumberDimc(tv_price.getText().toString())) {
                    amount += addsub;
                    et_price.setText(amount + "");
                } else {
                    CentreToast.showText(this, "请确认可借出数量为有效数值");
                }
                break;
            case R.id.Keyboard_LinearLayout:
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    hideInputWindow(this);
                }
                break;
        }
    }

    public void hideInputWindow(Activity context) {
        if (context == null) {
            return;
        }
        final View v = ((Activity) context).getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            presenter.getHeaderView(stockInfoBean.stockCode);
            timeCount.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_buy:
                if (!TextUtils.isEmpty(price_buy[position]) && !"- -".equals(price_buy[position])) {
                    et_income.setText(price_buy[position]);
                }

                break;
            case R.id.lv_sell:
                if (!TextUtils.isEmpty(price_sell[position]) && !"- -".equals(price_sell[position])) {
                    et_income.setText(price_sell[position]);
                }
                break;
        }
    }

    /**
     * 买卖回调
     */
    CommissionedBuyAndSellDialog.CommissionedBuyAndSell commissionedBuyAndSell = new CommissionedBuyAndSellDialog.CommissionedBuyAndSell() {

        @Override
        public void setBuy(String code, String price, String num) {
        }

        @Override
        public void setSell(String code, String price, String num) {
            presenter.getBuy(stockInfoBean.stockCode, price + "", amount / 100 + "");
        }

        @Override
        public void showSeccDialog() {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }
}
