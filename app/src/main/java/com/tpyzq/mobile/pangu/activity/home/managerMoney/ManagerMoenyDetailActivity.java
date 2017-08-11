package com.tpyzq.mobile.pangu.activity.home.managerMoney;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.BaseProductView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.FixationAndFloatEarningsView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.FortnightView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund.MoneyFundView;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FixFundListActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetProductInfoConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetProductInfoOtcConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.LoadPdfConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetProductInfoConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetProductInfoOtcConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToLoadPdfConnect;
import com.tpyzq.mobile.pangu.interfac.DoPrecontractLoadImpl;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.CounterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/24.
 * 理财详情
 *
 * 产品分三大类
 *904339
 * 1.开发式基金
 *      a.非货币基金
 *      b.货币基金
 * 2.14天理财
 *      14天理财增益
 * 3.OTC收益凭证
 *      a.固定增益
 *      b.浮动收益
 *      c.固定+浮动收益
 */
public class ManagerMoenyDetailActivity extends BaseActivity implements View.OnClickListener, ICallbackResult {

    private static final String TAG = "ManagerMoenyDetailActivity";
    private BaseProductView mBaseProductView;
    private TextView mTitle;
    private TextView mTopName;
    private TextView mTopType;
    private TextView mTopRadio;
    private TextView mTopRadioDiscrib;
    private TextView mTopContent1;
    private TextView mTopContent2;
    private TextView mTopContent3;
    private ImageView mIcShareBtn;
    private String mProductType;
    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private LinearLayout mViewGroup;
    private Dialog  mLoadingDialog;
    private FrameLayout mLoadingLayout;

    private String mProductCode;
    private String mPersent;
    private TextView mBuyBtn;
    private RelativeLayout mCounter;
    private String mDangerousLeavel;
    private String mStartBuyPrice;
    private String schema_id;
    private String prod_code;
    private String mProdType;
    public static FragmentManager ManagerMoenyfragmentManager;

    @Override
    public void initView() {
        ManagerMoenyfragmentManager = getFragmentManager();
        mLoadingDialog = LoadingDialog.initDialog(this, "正在加载");
        mLoadingLayout = (FrameLayout) findViewById(R.id.manamgerMoneyloadingLayout);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingDialog.show();
        mIcShareBtn = (ImageView) findViewById(R.id.manangerMoneyFenxiangIc);
        mIcShareBtn.setVisibility(View.GONE);
        mTopName = (TextView) findViewById(R.id.managerMoenyDetailFundName);
        mTopType = (TextView) findViewById(R.id.managerMoenyDetailfundType);
        mTopRadio = (TextView) findViewById(R.id.managerMoenyDetailFundRadio);
        mTopRadioDiscrib = (TextView)  findViewById(R.id.managerMoenyDetailFundRadioDisrib);
        mTopContent1 = (TextView) findViewById(R.id.managerMoenyDetailFundBotom1);
        mTopContent2 = (TextView) findViewById(R.id.managerMoenyDetailFundBotom2);
        mTopContent3 = (TextView) findViewById(R.id.managerMoenyDetailFundBotom3);

        findViewById(R.id.managerMoneyDetailBack).setOnClickListener(this);

        mCounter = (RelativeLayout) findViewById(R.id.counter);
        mCounter.setOnClickListener(this);

        mBuyBtn = (TextView) findViewById(R.id.fornightBuyBtn);
        mBuyBtn.setOnClickListener(this);
        mBuyBtn.setVisibility(View.VISIBLE);

        mViewGroup = (LinearLayout) findViewById(R.id.managerMoneyDetailLayout);
        mTitle = (TextView) findViewById(R.id.managerMoenyDetailTitle);

        Intent intent = getIntent();

        mPersent = intent.getStringExtra("prod_nhsy");
        schema_id = intent.getStringExtra("schema_id");
        prod_code = intent.getStringExtra("prod_code");
        mStartBuyPrice = intent.getStringExtra("prod_qgje");
        String productCode = intent.getStringExtra("productCode");
        mDangerousLeavel = intent.getStringExtra("ofund_risklevel_name");
        if (!TextUtils.isEmpty(mPersent) && "-".equals(mPersent)) {
            mPersent = "0.0%";
        }
        mProductType = intent.getStringExtra("TYPE");
        mProdType = intent.getStringExtra("prod_type");

        String target = intent.getStringExtra("target");//来自基金信息跳转
        if ("fundInfoTarget".equals(target)) {
            mBuyBtn.setVisibility(View.GONE);
            findViewById(R.id.managerDetailTargetLayout).setVisibility(View.GONE);
        } else if ("finncing".equals(target)) {
            mBuyBtn.setVisibility(View.GONE);
            findViewById(R.id.managerDetailTargetLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.targetTouBtn).setOnClickListener(this);
            findViewById(R.id.targetBuyBtn).setOnClickListener(this);

            if ("0".equals(mProdType)) {
                findViewById(R.id.targetTouBtn).setVisibility(View.GONE);
            }
        }


        if (!TextUtils.isEmpty(productCode) && !TextUtils.isEmpty(mProductType)) {
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            if ("3".equals(mProductType)) {
                mBuyBtn.setText("下一步");
                simpleRemoteControl.setCommand(new ToGetProductInfoOtcConnect(new GetProductInfoOtcConnect(TAG, "", productCode, mProductType)));
                simpleRemoteControl.startConnect();
            } else {
                simpleRemoteControl.setCommand(new ToGetProductInfoConnect(new GetProductInfoConnect(TAG, "", productCode, mProductType)));
                simpleRemoteControl.startConnect();
            }
        }

        //二期用  别删
//        String type = getProductType(intent);
//        CleverManamgerMoneyEntity entity = intent.getParcelableExtra("entity");
//        initDetailView(type, entity, linearLayout);

    }

    /**
     * 设置详情view的布局
     */
    private void initDetailView(String type, CleverManamgerMoneyEntity entity, LinearLayout linearLayout) {
        if (TextUtils.isEmpty(type)) {
            return;
        }

        entity.setFXDJ(mDangerousLeavel);
        entity.setQGJE(mStartBuyPrice);

        initTop(type, entity);


        if (!TextUtils.isEmpty(entity.getPRODCODE())) {
            mProductCode = entity.getPRODCODE();
        } else if (!TextUtils.isEmpty(entity.getSECURITYCODE())) {
            mProductCode = entity.getSECURITYCODE();
        }


        mProductType = entity.getTYPE();

        if (ConstantUtil.MANAGERMONEYTYPE_FUND.equals(type)) {         //基金

            String fundTypeCode = "";

            if (!TextUtils.isEmpty(entity.getFUNDTYPECODE())) {
                fundTypeCode = entity.getFUNDTYPECODE();
            }

            if ("2".equals(mProdType)) {//货币基金
                mTitle.setText("基金详情");
                mBaseProductView = new MoneyFundView(this, ConstantUtil.FUND_TYPE, entity);

            } else {   //非货币基金
                mTitle.setText("基金详情");
                mBaseProductView = new MoneyFundView(this, ConstantUtil.FUND_MISTAKETYPE, entity);
            }

        } else if (ConstantUtil.MANAGERMONEYTYPE_OTC.equals(type)) {  // otc

//            String incomeType = entity.getINCOMETYPE();
//
//            if (TextUtils.isEmpty(incomeType)) {
//                return;
//            }

//            if (FileUtil.OTC_L.equals(incomeType)) {   //固定
//                mTitle.setText("" + entity.getPRODNAME());
//                mBaseProductView = new FixationEarningsView(this, ManagerHelp.OTC_L_INT, entity);
//            } else if (FileUtil.OTC_F.equals(incomeType)) {    //浮动
//                mTitle.setText("" + entity.getPRODNAME());
//                mBaseProductView = new FloatEarningsView(this, ManagerHelp.OTC_F_INT, entity);
//            } else if (FileUtil.OTC_FL.equals(incomeType)) {    //固定 + 浮动
                mTitle.setText("" + entity.getPRODNAME());
                mBaseProductView = new FixationAndFloatEarningsView(this, ManagerHelp.OTC_FL_INT, entity);
//            }

        } else if (ConstantUtil.MANAGERMONEYTYPE_FOURTEEN.equals(type)) {  //14天理财
            mTitle.setText("太平洋14天现金增益");
            mBaseProductView = new FortnightView(this, ManagerHelp.MANAGERMONEYTYPE_FOURTEEN_INT, entity);
        }

        if (mBaseProductView != null) {
            linearLayout.addView(mBaseProductView.getContentView());
        }
    }

    /**
     * 设置头布局
     * @param type
     * @param entity
     */
    private void initTop(String type, CleverManamgerMoneyEntity entity) {
        if (ConstantUtil.MANAGERMONEYTYPE_FUND.equals(type)) {//基金类型
            String fundNameAndCode = "";
            if (!TextUtils.isEmpty(entity.getFUNDNAME())) {
                fundNameAndCode = entity.getFUNDNAME();

            }

            if (!TextUtils.isEmpty(entity.getSECURITYCODE())) {
                fundNameAndCode = fundNameAndCode + "(" + entity.getSECURITYCODE() + ")";
            }

            mTopName.setText(fundNameAndCode);

            if (!TextUtils.isEmpty(entity.getFUNDTYPE())) {
                mTopType.setText(entity.getFUNDTYPE());
            }

            if (ConstantUtil.MONEYTYPE.equals(entity.getFUNDTYPECODE())) {//货币基金
                mTopRadioDiscrib.setText("7日年化收益率");
                if (!TextUtils.isEmpty(entity.getLATESTWEEKLYYIELD()) && Helper.isDecimal(entity.getLATESTWEEKLYYIELD())) {

                    DecimalFormat format = new DecimalFormat("#0.00%");

                    mTopRadio.setText(format.format(Double.parseDouble(entity.getLATESTWEEKLYYIELD())));
                }

                String topContent1_temp = "万份收益(元)\n";
                if (!TextUtils.isEmpty(entity.getDAILYPROFIT()) && Helper.isDecimal(entity.getDAILYPROFIT())) {

                    DecimalFormat format = new DecimalFormat("#0.0000");

                    topContent1_temp = topContent1_temp + format.format(Double.parseDouble(entity.getDAILYPROFIT()));
                } else {
                    topContent1_temp = topContent1_temp + "-";
                }

                mTopContent1.setText(topContent1_temp);

            } else {   //非货币基金
                mTopRadioDiscrib.setText("近一月涨跌幅");

                if (!TextUtils.isEmpty(entity.getCHANGEPCTRM())) {
                    mTopRadio.setText(entity.getCHANGEPCTRM());
                }

                String topContent1_temp = "最新净值\n";
                if (!TextUtils.isEmpty(entity.getUNITNV()) && Helper.isDecimal(entity.getUNITNV())) {

                    DecimalFormat format = new DecimalFormat("#0.0000");
                    topContent1_temp = topContent1_temp + format.format(Double.parseDouble(entity.getUNITNV()));
                }

                mTopContent1.setText(topContent1_temp);
            }

            String topContent2_temp = "起购金额(元)\n";
            if (!TextUtils.isEmpty(entity.getQGJE()) && Helper.isDecimal(entity.getQGJE())) {
                topContent2_temp = topContent2_temp + TransitionUtils.long2million(entity.getQGJE());
            } else {
                topContent2_temp = topContent2_temp + "-";
            }

            mTopContent2.setText(topContent2_temp);

            String topContent3_temp = "风险等级\n";
            if (!TextUtils.isEmpty(entity.getFXDJ())) {
                topContent3_temp = topContent3_temp + entity.getFXDJ();
            } else {
                topContent3_temp = topContent3_temp + "-";
            }
            mTopContent3.setText(topContent3_temp);

        } else if (ConstantUtil.MANAGERMONEYTYPE_OTC.equals(type)) {              //otc
            mTopName.setVisibility(View.GONE);
            mTopType.setVisibility(View.GONE);
            mTopRadioDiscrib.setText("年化收益率");

            if (!TextUtils.isEmpty(entity.getCOMPREF())) {
                mTopRadio.setText(entity.getCOMPREF() + "%");
            }

            if (!TextUtils.isEmpty(entity.getBUY_LOW_AMOUNT())) {

                if (entity.getBUY_LOW_AMOUNT().contains("万")) {
                    mTopContent1.setText("起购" + entity.getBUY_LOW_AMOUNT() + "元");
                } else {
                    mTopContent1.setText("起购" + TransitionUtils.s2million(entity.getBUY_LOW_AMOUNT()) + "元");
                }

            }

            if (!TextUtils.isEmpty(entity.getINVESTDAYS())) {
                mTopContent2.setText("期限" + entity.getINVESTDAYS() + "天");
            }

//            mTopContent3.setText(ManagerHelp.getLevel(entity.getRISKLEVEL()) + "等级");
            mTopContent3.setText(entity.getRISKLEVEL());

        } else if (ConstantUtil.MANAGERMONEYTYPE_FOURTEEN.equals(type)) {      //14天
            mTopName.setVisibility(View.GONE);
            mTopType.setVisibility(View.GONE);

            mTopRadioDiscrib.setText("年化收益率");

            if (!TextUtils.isEmpty(entity.getCOMPREF())) {
                mTopRadio.setText(entity.getCOMPREF());
            }

            if (!TextUtils.isEmpty(entity.getBUY_LOW_AMOUNT())) {
                mTopContent1.setText("起购" + entity.getBUY_LOW_AMOUNT()+ "元");
            }

            if (!TextUtils.isEmpty(entity.getINVESTDAYS())) {
                mTopContent2.setText("期限" + entity.getINVESTDAYS() + "天");
            }

            mTopContent3.setText(ManagerHelp.getLevel(entity.getRISKLEVEL()));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public void getResult(Object result, String tag) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        if(result instanceof String ){
            showDialog(result + "------------"+tag,false);
            return;
        }
        mEntities = (ArrayList<CleverManamgerMoneyEntity>) result;
        if ("LoadPdfConnect".equals(tag) || "GetProductInfoConnect".equals(tag)) {
            if (mEntities == null || mEntities.size() <= 0) {
                try {
                    showDialog("暂无数据",true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return;
        }
        mLoadingLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mEntities.get(0).getMistackMsg())) {
                showDialog(mEntities.get(0).getMistackMsg(),true);
                return;
            }

        initDetailView(mProductType, mEntities.get(0), mViewGroup);
        } else if("GetProductInfoOtcConnect".equals(tag)){
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            simpleRemoteControl.setCommand(new ToLoadPdfConnect(new LoadPdfConnect(TAG, "", prod_code, mProductType, mEntities)));
            simpleRemoteControl.startConnect();
        }
    }

    private void showDialog(String msg,boolean isShowClick){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(ManagerMoenyfragmentManager,ManagerMoenyDetailActivity.class.toString());
        if (isShowClick){
            customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
                @Override
                public void confirmOnclick() {
                    finish();
                    customCenterDialog.dismiss();
                }
            });
        }
    }

    /**
     * 获取产品类型
     * @return
     */
    private String getProductType(Intent intent) {
        int intType = intent.getIntExtra("type", -1);

        return String.valueOf(intType);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_manager_money_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.managerMoneyDetailBack:
                finish();
                break;
            case R.id.manangerMoneyFenxiangIc:
                break;

            case R.id.counter:

                if (!TextUtils.isEmpty(mPersent)) {
                    try {
                        NumberFormat nf = NumberFormat.getPercentInstance();
                        Number m = null;
                        if (ConstantUtil.MANAGERMONEYTYPE_FOURTEEN.equals(mProductType)) {
                            m = nf.parse(mPersent);//mPersent 或 mPersent + "%"
                        } else {
                            m = nf.parse(mPersent);//mPersent 或 mPersent + "%"
                        }

                        CounterDialog.showDialog(ManagerMoenyDetailActivity.this,  m.floatValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.fornightBuyBtn:
            case R.id.targetBuyBtn:
                String login = Db_PUB_USERS.queryingIslogin();
                String phone = Db_PUB_USERS.queryingMobile();
                Intent intent = new Intent();
                intent.putExtra("productCode", mProductCode);
                intent.putExtra("productType", mProductType);
//                intent.putExtra("pageindex", ConstantUtil.PAGE_INDEX_PRODUCTBUY);
                intent.putExtra("schema_id", schema_id);
                intent.putExtra("prod_code", prod_code);
                if (!Db_PUB_USERS.isRegister()) {
                    intent.setClass(ManagerMoenyDetailActivity.this, ShouJiZhuCeActivity.class);
                    ManagerMoenyDetailActivity.this.startActivity(intent);
                } else {
                    if(TextUtils.isEmpty(phone)){
                        intent.setClass(ManagerMoenyDetailActivity.this, ShouJiVerificationActivity.class);
                        ManagerMoenyDetailActivity.this.startActivity(intent);
                    }else {
                        if ("true".equals(login)) {

                            if (mEntities != null && mEntities.size() > 0) {
                                intent.putExtra("cleverManamgerMoneyEntitys", mEntities);
                            }


                            if (mBuyBtn.getText().equals("下一步")) {

                                new DoPrecontractLoadImpl(ManagerMoenyDetailActivity.this, mProductCode, mProductType, schema_id, mEntities).doPrecontractLoad();

//                                intent.setClass(this, ProductPrecontractActivity.class);
//                                ManagerMoenyDetailActivity.this.startActivity(intent);
//                                finish();


                            } else {
                                intent.setClass(this, ProductBuyActivity.class);
                                ManagerMoenyDetailActivity.this.startActivity(intent);
                            }


                        } else {
                            intent.setClass(ManagerMoenyDetailActivity.this, TransactionLoginActivity.class);
                            ManagerMoenyDetailActivity.this.startActivity(intent);
                        }
                    }
                }
                break;
            case R.id.targetTouBtn:

                intent = new Intent();
                intent.setClass(ManagerMoenyDetailActivity.this, FixFundListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
