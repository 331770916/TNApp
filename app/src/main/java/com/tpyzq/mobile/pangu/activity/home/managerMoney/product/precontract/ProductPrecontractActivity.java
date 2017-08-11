package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.ProductPrecontractView;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2017/4/7.
 * 产品预约页面
 */

public class ProductPrecontractActivity extends BaseActivity implements View.OnClickListener , ProductPrecontractView {

    private ProductPrecontractPresenter productPrecontractPresenter = new ProductPrecontractPresenter(this);
    private EditText mInputEdittext;
    private String mProductCode = "";
    private WeakReference<Dialog> mLoadProgress;
    private ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntitys;

    @Override
    public void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        findViewById(R.id.precontractBtn).setOnClickListener(this);
        TextView titleText = (TextView) findViewById(R.id.toolbar_title);
        titleText.setText("产品预约");

        mInputEdittext = (EditText) findViewById(R.id.inputPrecontractPriceEdit);

        mInputEdittext.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "10000000")});
        initData();

    }

    private void initData() {
        Intent intent = getIntent();

        mProductCode = intent.getStringExtra("productCode");
        cleverManamgerMoneyEntitys = (ArrayList<CleverManamgerMoneyEntity>) intent.getSerializableExtra("cleverManamgerMoneyEntitys");

        if (cleverManamgerMoneyEntitys != null && cleverManamgerMoneyEntitys.size() > 0) {
            initTop(cleverManamgerMoneyEntitys.get(0));
        }

    }

    @Override
    public String getToken() {
        String token = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        return token;
    }

    @Override
    public String getFUNCTIONCODE() {
        return "HQLNG105";
    }

    @Override
    public String getFund_account() {
        return UserUtil.capitalAccount;
    }

    @Override
    public String getOrder_money() {
        return mInputEdittext.getText().toString();
    }

    @Override
    public String getOrder_prod_code() {
        return mProductCode;
    }

    @Override
    public void addPrecontractError(String error) {
        progressCancel();

        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(error,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),ProductPrecontractActivity.class.toString());
    }

    @Override
    public void addPrecontractSuccess(String code, String type, String message) {
        progressCancel();

        Intent intent = new Intent();
        intent.putExtra("cleverManamgerMoneyEntitys", cleverManamgerMoneyEntitys);
        intent.setClass(this, ReservationSuccessActivity.class);
        startActivity(intent);
        finish();
    }

    private void initTop(CleverManamgerMoneyEntity cleverManamgerMoneyEntity) {
        TextView productName = (TextView) findViewById(R.id.precontractName);
        TextView productRadio = (TextView) findViewById(R.id.precontractRadio);
        TextView startBuy = (TextView) findViewById(R.id.precontractBotom1);
        TextView limitDay = (TextView) findViewById(R.id.precontractBotom2);
        TextView leavle = (TextView) findViewById(R.id.precontractBotom3);
        TextView startBuyPrice = (TextView) findViewById(R.id.productPrecontract_warn);

        TextView fundAccount = (TextView) findViewById(R.id.precontract_fundAccount);

        TextView beginDate = (TextView) findViewById(R.id.ipo_begin_date);
        TextView endDate = (TextView) findViewById(R.id.ipo_end_date);

        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getPRODNAME())) {
            sb.append(cleverManamgerMoneyEntity.getPRODNAME());
        }

        sb.append("\n");

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getPRODCODE())) {
            sb.append("(" + cleverManamgerMoneyEntity.getPRODCODE() + ")");
        }

        productName.setText(sb.toString());

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getCOMPREF())) {
            productRadio.setText(cleverManamgerMoneyEntity.getCOMPREF() + "%");
        }

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT())) {

            if (cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT().contains("万")) {
                startBuy.setText("起购" + cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT() + "元");

            } else {
                startBuy.setText("起购" + TransitionUtils.s2million(cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT()) + "元");

            }
        }

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getINVESTDAYS())) {
            limitDay.setText("期限" + cleverManamgerMoneyEntity.getINVESTDAYS() + "天");
        }

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getRISKLEVEL())) {
            leavle.setText(cleverManamgerMoneyEntity.getRISKLEVEL());
        }

        fundAccount.setText(UserUtil.capitalAccount);

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getIPO_BEGIN_DATE())) {

            String startbeginDate = cleverManamgerMoneyEntity.getIPO_BEGIN_DATE();
            beginDate.setText(Helper.StringToDate(startbeginDate, "yyyyMMdd", "yyyy-MM-dd"));
        }

        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getIPO_END_DATE())) {

            String lastendDate = cleverManamgerMoneyEntity.getIPO_END_DATE();
            endDate.setText(Helper.StringToDate(lastendDate, "yyyyMMdd", "yyyy-MM-dd"));
        }


        if (!TextUtils.isEmpty(cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT())) {

            if (cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT().contains("万")) {
                startBuyPrice.setText("预约金额"+ cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT() +"元起");
            } else {
                startBuyPrice.setText("预约金额" + TransitionUtils.s2million(cleverManamgerMoneyEntity.getBUY_LOW_AMOUNT()) + "元起");
            }
        }




    }

//
//    private String formateDATE(String date) {
//
//
//        boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(date);
//        if (isYYYYMMdd) {
//            date = Helper.StringToDate(date, "yyyyMMdd", "yyyy-MM-dd");
//        }
//
//        boolean isYYY_MM_dd = Helper.isDateFromatYYYY_MM_dd(date);
//        if (!isYYY_MM_dd) {
//            date = "0000-00-00";
//        }
//
//        return date;
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
            case R.id.precontractBtn:

                if (TextUtils.isEmpty(mInputEdittext.getText().toString())) {
                    CentreToast.showText(ProductPrecontractActivity.this,"输入金额不能为空");
                    return;
                }

                Dialog dialog = LoadingDialog.initDialog(this, "正在加载...");
                mLoadProgress = new WeakReference<Dialog>(dialog);
                mLoadProgress.get().show();

                productPrecontractPresenter.addPrecontract();
                break;
        }
    }

    private void progressCancel() {
        if (mLoadProgress != null) {
            mLoadProgress.get().cancel();
            mLoadProgress = null;
        }

        System.gc();
        System.runFinalization();
    }

    //
    @Override
    public int getLayoutId() {
        return R.layout.activity_product_precontract;
    }

    private class InputFilterMinMax implements InputFilter {
        private double min, max;

        public InputFilterMinMax(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                double input = Double.parseDouble(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (Exception nfe) { }
            return "";
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
