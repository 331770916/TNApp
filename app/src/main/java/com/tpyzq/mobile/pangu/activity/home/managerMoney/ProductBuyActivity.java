package com.tpyzq.mobile.pangu.activity.home.managerMoney;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.AssessConfirmActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.OtcDataEntity;
import com.tpyzq.mobile.pangu.data.OtcRiskEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.SubsStatusEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ProductBuyDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;

/**
 * Created by chenxinyu on 2016/10/8.
 * 产品购买界面
 */
public class ProductBuyActivity extends BaseActivity implements View.OnClickListener, ProductBuyDialog.ProductBuyDialogPositiveListener {

    private final int REQUEST_CODE_SHEN = 1001;
    private final int REQUEST_CODE_REN = 1002;
    private final int REQUEST_CODE_OTC = 1003;

    private TextView tv_stock_name;
    private TextView tv_stock_code;
    private TextView tv_stock_info1;
    private TextView tv_stock_info2;
    private TextView et_stock_price;
    private TextView tv_fhfs;
    private LinearLayout ll_fhfs;
    private TextView tv_transaction_account;
    private TextView tv_transaction_price;
    private FundDataEntity fundDataEntity;
    private OtcDataEntity otcDataEntity;
    private ProductBuyDialog productBuyDialog;
    private DialogBean dialogBean;
    private String fundcode;
    private String type;
    private String session;
    private SubsStatusEntity subsStatusBean;
    private AssessConfirmEntity assessConfirmEntity;
    private TextView tv_sure;
    private Dialog loadingDialog;
    private int CLEARVIEW = 100;
    private String schema_id;
    private String prod_code;
    private String serial_no;

    @Override
    public void initView() {
        tv_stock_name = (TextView) findViewById(R.id.tv_stock_name);
        tv_stock_code = (TextView) findViewById(R.id.tv_stock_code);
        tv_stock_info1 = (TextView) findViewById(R.id.tv_stock_info1);
        tv_stock_info2 = (TextView) findViewById(R.id.tv_stock_info2);
        et_stock_price = (TextView) findViewById(R.id.et_stock_price);
        tv_fhfs = (TextView) findViewById(R.id.tv_fhfs);
        ll_fhfs = (LinearLayout) findViewById(R.id.ll_fhfs);
        tv_transaction_account = (TextView) findViewById(R.id.tv_transaction_account);
        tv_transaction_price = (TextView) findViewById(R.id.tv_transaction_price);
        findViewById(R.id.productBuyBack).setOnClickListener(this);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        loadingDialog = LoadingDialog.initDialog(this, "正在加载");
        loadingDialog.show();
        assessConfirmEntity = new AssessConfirmEntity();
        dialogBean = new DialogBean();
        fundDataEntity = new FundDataEntity();
        otcDataEntity = new OtcDataEntity();
        UserUtil.refrushUserInfo();
        Intent intent = getIntent();
        tv_sure.setOnClickListener(this);
        tv_fhfs.setOnClickListener(this);
        fundcode = intent.getStringExtra("productCode");
        type = intent.getStringExtra("productType");
//        type = "1";
        if ("3".equals(type)) {
            ll_fhfs.setVisibility(View.GONE);
        } else {
            ll_fhfs.setVisibility(View.VISIBLE);
        }

        schema_id = intent.getStringExtra("schema_id");
        prod_code = intent.getStringExtra("prod_code");
        session = SpUtils.getString(this, "mSession", null);
        tv_sure.setClickable(false);
        tv_sure.setBackgroundResource(R.drawable.button_login_unchecked);
        if (TextUtils.isEmpty(fundcode) || TextUtils.isEmpty(type)) {
            loadingDialog.dismiss();
            return;
        }
        switch (type) {
            case "1":
                getFundData(fundcode);
                break;
            case "2":
                getFundData(fundcode);
                break;
            case "3":
                getOtcData(session, fundcode);
                break;
        }
        tv_transaction_account.setText("保证金账户:" + UserUtil.capitalAccount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productBuyBack:
                finish();
                break;
            case R.id.tv_sure:
                if (Helper.getInstance().isNeedShowRiskDialog()) {
                    Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Intent intent = new Intent(ProductBuyActivity.this, RiskEvaluationActivity.class);
                            intent.putExtra("isLogin", true);
                            ProductBuyActivity.this.startActivity(intent);
                        }
                    }, new CancelDialog.NagtiveClickListener() {
                        @Override
                        public void onNagtiveClick() {
                            dialogBean.stockprice = et_stock_price.getText().toString();
                            productBuyDialog = new ProductBuyDialog(ProductBuyActivity.this, ProductBuyActivity.this, dialogBean, type);
                            productBuyDialog.show();
                        }
                    });
                } else {
                    dialogBean.stockprice = et_stock_price.getText().toString();
                    productBuyDialog = new ProductBuyDialog(ProductBuyActivity.this, ProductBuyActivity.this, dialogBean, type);
                    productBuyDialog.show();
                }
                break;
            case R.id.tv_fhfs:

                Helper.showItemSelectDialog(this,getWidth(),tv_fhfs.getText().toString(),new Helper.OnItemSelectedListener(){
                    @Override
                    public void getSelectedItem(String content) {
                        tv_fhfs.setText(content);
                        dialogBean.fhfs = content;
                    }
                },false,new String[]{"现金分红", "份额分红"});
                break;
        }
    }

    private int  getWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取OTC确认信息
     */
    private void getOtcData(String mSession, String stockCode) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("FLAG", "true");
        map2.put("OPER_TYPE", "1");
        map1.put("funcid", "730206");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        otcDataEntity = new Gson().fromJson(jsonArray.getString(0), OtcDataEntity.class);
                        setTextView(otcDataEntity);
                    }else{
                        showClickDialog(msg);
                    }
                    et_stock_price.addTextChangedListener(new StockPriceListen());
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLEARVIEW && resultCode == RESULT_OK) {
            et_stock_price.setText("");
        } else if (requestCode == REQUEST_CODE_REN && resultCode == RESULT_OK) {
            buy_rengou(dialogBean.stockprice, fundDataEntity.data.get(0).FUND_COMPANY);
        } else if (requestCode == REQUEST_CODE_SHEN && resultCode == RESULT_OK) {
            buy_shengou(dialogBean.stockprice, fundDataEntity.data.get(0).FUND_COMPANY);
        } else if(requestCode == REQUEST_CODE_OTC && resultCode == RESULT_OK) {
            String otc_status = otcDataEntity.PROD_STATUS;
            if (TextUtils.isEmpty(otc_status)) {
                return;
            }
            getOTCRISk(otc_status, et_stock_price.getText().toString());
        }
    }

    /**
     * 获取基金数据
     */
    private void getFundData(String fundcode) {
        HashMap map300431 = new HashMap();
        map300431.put("funcid", "300431");
        map300431.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300431_1 = new HashMap();
        map300431_1.put("SEC_ID", "tpyzq");
        map300431_1.put("FLAG", "true");
        map300431_1.put("FUND_CODE", fundcode);
        map300431_1.put("FUND_COMPANY", "");
        map300431_1.put("OPER_TYPE", "0");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if (("0").equals(code)) {
                        fundDataEntity = new Gson().fromJson(response, FundDataEntity.class);
                        setTextView(fundDataEntity);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ProductBuyActivity.this, TransactionLoginActivity.class));
                    } else {
                        showClickDialog(msg);
                    }
                    et_stock_price.addTextChangedListener(new StockPriceListen());
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基金认购购买
     */
    private void buy_rengou(final String price, final String fund_company) {
        HashMap map300439 = new HashMap();
        map300439.put("funcid", "300439");
        map300439.put("token", session);
        map300439.put("secret", UserUtil.Keyboard);   //1.加密 0.不加密
        HashMap map300439_1 = new HashMap();
        map300439_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300439_1.put("FUND_COMPANY", encryptBySessionKey(fund_company));
        map300439_1.put("FUND_CODE", encryptBySessionKey(fundcode));
        map300439_1.put("BUY_AMOUNT", encryptBySessionKey(price));
        map300439_1.put("FLAG", encryptBySessionKey("true"));
        map300439_1.put("DO_OPEN", encryptBySessionKey(""));
        map300439_1.put("DO_CONTRACT", encryptBySessionKey(""));
        map300439_1.put("DO_PRE_CONDITION", encryptBySessionKey("1"));
        if("份额分红".equals(tv_fhfs.getText().toString())){
            map300439_1.put("AUTO_BUY", encryptBySessionKey("0"));
        }else if("现金分红".equals(tv_fhfs.getText().toString())){
            map300439_1.put("AUTO_BUY", encryptBySessionKey("1"));
        }
        map300439.put("parms", map300439_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300439, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CentreToast.showText(ProductBuyActivity.this, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        serial_no = subsStatusBean.data.get(0).SERIAL_NO;
                        //判断是否跳转风险评测界面
                        if (!TextUtils.isEmpty(subsStatusBean.data.get(0).IS_ABLE) && "0".equals(subsStatusBean.data.get(0).IS_ABLE)) {
                            startFinish(fundDataEntity.data.get(0).FUND_NAME, price, "0");
                        } else {
                            assessConfirmEntity.productcode = fundcode;
                            assessConfirmEntity.productcompany = fund_company;
                            assessConfirmEntity.productprice = price;
                            assessConfirmEntity.type = "1";
                            assessConfirmEntity.IS_ABLE = subsStatusBean.data.get(0).IS_ABLE;
                            String is_agreement = subsStatusBean.data.get(0).IS_AGREEMENT;
                            assessConfirmEntity.IS_AGREEMENT = is_agreement;
                            assessConfirmEntity.IS_OPEN = subsStatusBean.data.get(0).IS_OPEN;
                            assessConfirmEntity.IS_VALIB_RISK_LEVEL = subsStatusBean.data.get(0).IS_VALIB_RISK_LEVEL;
                            assessConfirmEntity.OFRISK_FLAG = subsStatusBean.data.get(0).OFRISK_FLAG;
                            assessConfirmEntity.OFUND_RISKLEVEL_NAME = subsStatusBean.data.get(0).OFUND_RISKLEVEL_NAME;
                            assessConfirmEntity.RISK_LEVEL = subsStatusBean.data.get(0).RISK_LEVEL;
                            assessConfirmEntity.RISK_LEVEL_NAME = subsStatusBean.data.get(0).RISK_LEVEL_NAME;
                            assessConfirmEntity.RISK_RATING = subsStatusBean.data.get(0).RISK_RATING;

                            Intent intent = new Intent();
                            intent.setClass(ProductBuyActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("SERIAL_NO", serial_no);
                            intent.putExtra("assessConfirm", assessConfirmEntity);
                            startActivity(intent);
                        }
                    } else {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        CentreToast.showText(ProductBuyActivity.this, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基金申购购买
     */
    private void buy_shengou(final String price, final String fund_company) {
        HashMap map300440 = new HashMap();
        map300440.put("funcid", "300440");
        map300440.put("token", session);
        map300440.put("secret", UserUtil.Keyboard);
        HashMap map300440_1 = new HashMap();
        map300440_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300440_1.put("FUND_COMPANY", encryptBySessionKey(fund_company));
        map300440_1.put("FUND_CODE", encryptBySessionKey(fundcode));
        map300440_1.put("BUY_AMOUNT", encryptBySessionKey(price));
        map300440_1.put("FLAG", encryptBySessionKey("true"));
        map300440_1.put("DO_OPEN", encryptBySessionKey(""));
        map300440_1.put("DO_CONTRACT", encryptBySessionKey(""));
        map300440_1.put("DO_PRE_CONDITION", encryptBySessionKey("1"));
        if("份额分红".equals(tv_fhfs.getText().toString())){
            map300440_1.put("AUTO_BUY", encryptBySessionKey("0"));
        }else if("现金分红".equals(tv_fhfs.getText().toString())){
            map300440_1.put("AUTO_BUY", encryptBySessionKey("1"));
        }
        map300440.put("parms", map300440_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300440, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CentreToast.showText(ProductBuyActivity.this, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
                        serial_no = subsStatusBean.data.get(0).SERIAL_NO;
                        //判断是否跳转风险评测界面
                        if (!TextUtils.isEmpty(subsStatusBean.data.get(0).IS_ABLE) && "0".equals(subsStatusBean.data.get(0).IS_ABLE)) {
                            startFinish(fundDataEntity.data.get(0).FUND_NAME, price, "0");
                        } else {
                            assessConfirmEntity.productcode = fundcode;
                            assessConfirmEntity.productcompany = fund_company;
                            assessConfirmEntity.productprice = price;
                            assessConfirmEntity.type = "2";
                            assessConfirmEntity.IS_ABLE = subsStatusBean.data.get(0).IS_ABLE;
                            String is_agreement = subsStatusBean.data.get(0).IS_AGREEMENT;
                            assessConfirmEntity.IS_AGREEMENT = is_agreement;
                            assessConfirmEntity.IS_OPEN = subsStatusBean.data.get(0).IS_OPEN;
                            assessConfirmEntity.IS_VALIB_RISK_LEVEL = subsStatusBean.data.get(0).IS_VALIB_RISK_LEVEL;
                            assessConfirmEntity.OFRISK_FLAG = subsStatusBean.data.get(0).OFRISK_FLAG;
                            assessConfirmEntity.OFUND_RISKLEVEL_NAME = subsStatusBean.data.get(0).OFUND_RISKLEVEL_NAME;
                            assessConfirmEntity.RISK_LEVEL = subsStatusBean.data.get(0).RISK_LEVEL;
                            assessConfirmEntity.RISK_LEVEL_NAME = subsStatusBean.data.get(0).RISK_LEVEL_NAME;
                            assessConfirmEntity.RISK_RATING = subsStatusBean.data.get(0).RISK_RATING;

                            Intent intent = new Intent();
                            intent.setClass(ProductBuyActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("assessConfirm", assessConfirmEntity);
                            startActivity(intent);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ProductBuyActivity.this, TransactionLoginActivity.class));
                    } else {
                        showDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * OTC购买之前判断是否跳转到风险界面
     */
    private void getOTCRISk(final String otc_status, final String price) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", fundcode);
        map2.put("PRODTA_NO", otcDataEntity.PRODTA_NO);
        map2.put("FLAG", "true");
        map1.put("funcid", "300512");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {

                        JSONArray jsonArray = new JSONArray(data);
                        OtcRiskEntity otcRiskBean = new Gson().fromJson(jsonArray.getString(0), OtcRiskEntity.class);
                        if ("0".equals(otcRiskBean.IS_OK)) {
                            if ("1".equals(otc_status)) {
                                getOTCshengou(price);
                            } else {
                                getOTCrengou(price);
                            }
//                            if ("2".equals(otc_status) || "2".equals(otc_status))
                        } else if ("1".equals(otcRiskBean.IS_OK)) {
                            if ("2".equals(otc_status)) {
                                assessConfirmEntity.type = "3";
                            } else {
                                assessConfirmEntity.type = "4";
                            }
                            assessConfirmEntity.productcode = fundcode;
                            assessConfirmEntity.productcompany = otcDataEntity.PRODTA_NO;
                            assessConfirmEntity.productprice = price;

                            assessConfirmEntity.IS_ABLE = otcRiskBean.IS_OK;
                            assessConfirmEntity.IS_AGREEMENT = otcRiskBean.IS_AGREEMENT;
                            assessConfirmEntity.IS_OPEN = otcRiskBean.IS_OPEN;
                            assessConfirmEntity.IS_VALIB_RISK_LEVEL = otcRiskBean.IS_OUTOFDATE;
                            assessConfirmEntity.OFRISK_FLAG = otcRiskBean.OFRISK_FLAG;
                            assessConfirmEntity.OFUND_RISKLEVEL_NAME = otcRiskBean.PRODRISK_LEVEL_NAME;
                            assessConfirmEntity.RISK_LEVEL = otcRiskBean.CORP_RISK_LEVEL;
                            assessConfirmEntity.RISK_LEVEL_NAME = otcRiskBean.RISK_LEVEL_NAME;
                            assessConfirmEntity.RISK_RATING = otcRiskBean.PRODRISK_LEVEL;

                            Intent intent = new Intent();
                            intent.setClass(ProductBuyActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("assessConfirm", assessConfirmEntity);
                            startActivity(intent);
                        }
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ProductBuyActivity.this, TransactionLoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 确认otc认购
     */
    private void getOTCrengou(final String price) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", otcDataEntity.PROD_CODE);
        map2.put("PRODTA_NO", otcDataEntity.PRODTA_NO);
        map2.put("ENTRUST_BALANCE", price);
        map2.put("FLAG", "true");
        map1.put("funcid", "730201");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        serial_no = jsonArray.getJSONObject(0).getString("SERIAL_NO");
                        startFinish(otcDataEntity.PROD_NAME, price, "1");
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ProductBuyActivity.this, TransactionLoginActivity.class));
                    } else {
                        showDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 确认otc申购
     */
    private void getOTCshengou(final String price) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", otcDataEntity.PROD_CODE);
        map2.put("PRODTA_NO", otcDataEntity.PRODTA_NO);
        map2.put("ENTRUST_BALANCE", et_stock_price.getText().toString());
        map2.put("FLAG", "true");
        map1.put("funcid", "730202");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        serial_no = jsonArray.getJSONObject(0).getString("SERIAL_NO");
                        startFinish(otcDataEntity.PROD_NAME, price, "1");
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(ProductBuyActivity.this, TransactionLoginActivity.class));
                    } else {
                        showClickDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),ProductBuyActivity.class.toString());
    }

    private void showClickDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),ProductBuyActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                finish();
                customCenterDialog.dismiss();
            }
        });
    }
    /**
     * 14天以及基金类型赋值
     *
     * @param
     */
    private void setTextView(FundDataEntity fundDataEntity) {
        tv_stock_name.setText(fundDataEntity.data.get(0).FUND_NAME);
        tv_stock_code.setText("(" + fundDataEntity.data.get(0).FUND_CODE + ")");
        tv_stock_info1.setText("首次最低" + fundDataEntity.data.get(0).OPEN_SHARE + "元");
        tv_stock_info2.setText("追加最低" + fundDataEntity.data.get(0).PERSON_INVEST + "元");
        //FUND_STATUS  0申购 1认购
        tv_transaction_price.setText("可用:" + fundDataEntity.data.get(0).ENABLE_BALANCE + "元");
        dialogBean.stockname = fundDataEntity.data.get(0).FUND_NAME;
        dialogBean.stockcode = fundDataEntity.data.get(0).FUND_CODE;
        dialogBean.account = UserUtil.capitalAccount;
        tv_fhfs.setText("份额分红");
        dialogBean.fhfs = "份额分红";
    }

    /**
     * OTC类型赋值
     *
     * @param
     */
    private void setTextView(OtcDataEntity otcDataEntity) {
        tv_stock_name.setText(otcDataEntity.PROD_NAME);
        tv_stock_code.setText("(" + otcDataEntity.PROD_CODE + ")");

        if ("2".equals(otcDataEntity.PROD_STATUS)) {
            tv_stock_info1.setText("起购金额" + otcDataEntity.OPEN_SHARE + "元，按" + otcDataEntity.ALLOT_LIMITSHARE + "元整数倍数递增");
        } else {
            tv_stock_info1.setText("起购金额" + otcDataEntity.OPEN_SHARE + "元，按" + otcDataEntity.ALLOT_LIMITSHARE2 + "元整数倍数递增");
        }
        tv_transaction_price.setText("可用:" + otcDataEntity.ENABLE_BALANCE + "元");
        tv_stock_info2.setVisibility(View.GONE);
        dialogBean.stockname = otcDataEntity.PROD_NAME;
        dialogBean.stockcode = otcDataEntity.PROD_CODE;
        dialogBean.account = UserUtil.capitalAccount;
    }

    /**
     * dialog回调
     */
    @Override
    public void doPositive() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        String fund_status = null;
        if (fundDataEntity.data != null && fundDataEntity.data.size() > 0) {
            fund_status = fundDataEntity.data.get(0).FUND_STATUS;
        }
        String prodta_no ="";
        String prod_code = "";
        String fundCompany ="";
        String fundcode ="";
        int requestCode = 0;
        switch (type) {
            case "1":
                if (TextUtils.isEmpty(fund_status)) {
                    return;
                }
                if ("0".equalsIgnoreCase(fund_status)) {
                    fundCompany = fundDataEntity.data.get(0).FUND_COMPANY;
                    fundcode = fundDataEntity.data.get(0).FUND_CODE;
                    requestCode = REQUEST_CODE_SHEN;
                } else {
                    fundCompany = fundDataEntity.data.get(0).FUND_COMPANY;
                    fundcode = fundDataEntity.data.get(0).FUND_CODE;
                    requestCode = REQUEST_CODE_REN;
                }
                break;
            case "2":
                if (TextUtils.isEmpty(fund_status)) {
                    return;
                }
                switch (fund_status) {
                    case "0"://申购
                        fundCompany = fundDataEntity.data.get(0).FUND_COMPANY;
                        fundcode = fundDataEntity.data.get(0).FUND_CODE;
                        requestCode = REQUEST_CODE_SHEN;
                        break;
                    case "1"://认购
                        fundCompany = fundDataEntity.data.get(0).FUND_COMPANY;
                        fundcode = fundDataEntity.data.get(0).FUND_CODE;
                        requestCode = REQUEST_CODE_REN;
                        break;
                }
                break;
            case "3":
                prodta_no =otcDataEntity.PRODTA_NO;
                prod_code = otcDataEntity.PROD_CODE;
                requestCode = REQUEST_CODE_OTC;
                break;
        }
        queryProductSuitability(session, prodta_no,prod_code,fundCompany,fundcode,requestCode);
    }


    private void queryProductSuitability(String seeesion,String prodta_no, String prod_code,  String fundCompany, String fundcode, final int requestCode) {
        InterfaceCollection.getInstance().queryProductSuitability(seeesion, prodta_no, prod_code , fundCompany, fundcode, "331261", new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                HashMap<String,String> resultMap = null;
                if ("0".equalsIgnoreCase(code)) {
                    resultMap = (HashMap<String,String>)info.getData();
                    if (null == resultMap) {
                        showDialog("数据异常");
                        return;
                    }
                    //弹框逻辑
                    //是否可以委托
                    if ("0".equalsIgnoreCase(resultMap.get("ENABLE_FLAG"))) {//不可委托
                        //尊敬的客户:\n       根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理
                        CancelDialog.cancleDialog(ProductBuyActivity.this, "", 4000, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {}
                        },null);
                        return;
                    }
                    /*else {
                        //可以委托 判断是否需要视频录制
                        if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
                            CancelDialog.cancleDialog(ProductBuyActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
                            return;
                        }
                    }*/
                    //跳转到适用性页面
                    Intent intent = new Intent(ProductBuyActivity.this, RiskConfirmActivity.class);
                    intent.putExtra("resultMap",resultMap);
                    startActivityForResult(intent, requestCode);
                } else {
                    showDialog(msg);
                }
            }
        });
    }

    public void startFinish(String name, String price, String type) {
//        CentreToast.showText(this, "委托成功");
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("schema_id", schema_id);
        intent.putExtra("prod_code", prod_code);
        intent.putExtra("price", price);
        intent.putExtra("type", type);
        if (!TextUtils.isEmpty(serial_no)) {
            intent.putExtra("SERIAL_NO", serial_no);
        } else {
            intent.putExtra("SERIAL_NO", "-1");
        }
        intent.setClass(this, BuyResultActivity.class);
        startActivityForResult(intent, CLEARVIEW);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_buy;
    }

    public class DialogBean {
        public String stockname;
        public String stockcode;
        public String stockprice;
        public String account;
        public String fhfs;
    }

    private class StockPriceListen implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
                tv_sure.setClickable(true);
                tv_sure.setBackgroundResource(R.drawable.button_login_pitchon);
            } else {
                tv_sure.setClickable(false);
                tv_sure.setBackgroundResource(R.drawable.button_login_unchecked);
            }
        }
    }
}
