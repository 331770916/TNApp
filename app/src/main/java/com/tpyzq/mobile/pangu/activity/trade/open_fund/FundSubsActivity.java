package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.keyboardlibrary.KeyboardTouchListener;
import com.android.keyboardlibrary.KeyboardUtil;
import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
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
import com.tpyzq.mobile.pangu.view.dialog.FundSubsDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;

/**
 * 基金认购
 */
public class FundSubsActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_fund_code/* 输入基金代码 */, et_rengou_price/* 输入认购金额 */;
    private TextView tv_fund_name/* 基金名称 */, tv_netvalue/* 基金净值 */, tv_lowest_investment/* 个人最低投资 */, tv_usable_money/* 可用资金 */, tv_choose_fund/* 选择基金产品 */,tv_fhfs;
    private Button bt_true/* 确定按钮 */;
    private FundDataEntity fundDataBean;
    private ImageView iv_back;//退出
    public static final int REQUSET = 1;//进入产品列表和签署协议界面
    public int point = -1;
    private SubsStatusEntity subsStatusBean;
    private AssessConfirmEntity assessConfirmBean;
    private String fundcode;
    private String session;
    private FundSubsListen fundSubsListen;
    private static int REQUESTCODE = 1001; //进入风险确认页面的请求码
    private static int REQAGREEMENTCODE = 1002; //进入签署协议页面的请求码

    private KeyboardUtil mKeyBoardUtil;
    private String AUTO_BUY = "";

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_fund_code = (EditText) findViewById(R.id.et_fund_code);
        et_rengou_price = (EditText) findViewById(R.id.et_rengou_price);
        tv_fund_name = (TextView) findViewById(R.id.tv_fund_name);
        tv_netvalue = (TextView) findViewById(R.id.tv_netvalue);
        tv_fhfs =  (TextView) findViewById(R.id.tv_fhfs);
        tv_lowest_investment = (TextView) findViewById(R.id.tv_lowest_investment);
        tv_usable_money = (TextView) findViewById(R.id.tv_usable_money);
        bt_true = (Button) findViewById(R.id.bt_true);
        tv_choose_fund = (TextView) findViewById(R.id.tv_choose_fund);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.fundRootLayout);
        initMoveKeyBoard(rootLayout, null, et_fund_code);

        initData();

    }


    /**
     * 初始化键盘
     *
     * @param rootLayout
     */
    private void initMoveKeyBoard(LinearLayout rootLayout, ScrollView scrollView) {
        mKeyBoardUtil = new KeyboardUtil(this, rootLayout, scrollView);
        mKeyBoardUtil.setOtherEdittext(et_fund_code);
        // monitor the KeyBarod state
//        mKeyBoardUtil.setKeyBoardStateChangeListener(new SearchActivity.KeyBoardStateListener());
        // monitor the finish or next Key
        mKeyBoardUtil.setInputOverListener(new InputOverListener());
        et_fund_code.setOnTouchListener(new KeyboardTouchListener(mKeyBoardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
    }

    private class InputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {

        }
    }


    /**
     * 获取基金数据
     */
    private void getFundData(String fundcode, String fundcompany) {
        HashMap map300431 = new HashMap();
        map300431.put("funcid", "300431");
        map300431.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap map300431_1 = new HashMap();
        map300431_1.put("SEC_ID", "tpyzq");
        map300431_1.put("FLAG", "true");
        map300431_1.put("FUND_CODE", fundcode);
        map300431_1.put("FUND_COMPANY", fundcompany);
        map300431_1.put("OPER_TYPE", "0");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(FundSubsActivity.this, ConstantUtil.NETWORK_ERROR);
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
                        fundDataBean = new Gson().fromJson(response, FundDataEntity.class);
                        setTextView(fundDataBean);
                    } else if ("-6".equals(code)) {
                        startActivity(new Intent(FundSubsActivity.this, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(FundSubsActivity.this, msg);
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
        map300439.put("secret", UserUtil.Keyboard);
        HashMap map300439_1 = new HashMap();
        map300439_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        map300439_1.put("FUND_COMPANY", encryptBySessionKey(fund_company));
        map300439_1.put("FUND_CODE", encryptBySessionKey(fundcode));
        map300439_1.put("BUY_AMOUNT", encryptBySessionKey(price));
        map300439_1.put("FLAG", encryptBySessionKey("true"));
        map300439_1.put("DO_OPEN", encryptBySessionKey(""));
        map300439_1.put("DO_CONTRACT", encryptBySessionKey(""));
        map300439_1.put("DO_PRE_CONDITION", encryptBySessionKey("1"));
        if ("份额分红".equals(tv_fhfs.getText().toString())){
            AUTO_BUY= "0";
        }else {
            AUTO_BUY= "1";
        }

        map300439_1.put("AUTO_BUY", encryptBySessionKey(AUTO_BUY));
        map300439.put("parms", map300439_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300439, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
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
                        //判断是否跳转风险评测界面
//                        if (!TextUtils.isEmpty(subsStatusBean.data.get(0).IS_ABLE) && "0".equals(subsStatusBean.data.get(0).IS_ABLE)) {
                        if ("0".equalsIgnoreCase(subsStatusBean.data.get(0).IS_OPEN) && "1".equalsIgnoreCase(subsStatusBean.data.get(0).IS_AGREEMENT)) {
                            startFinish();
                        } else {
                            assessConfirmBean = new AssessConfirmEntity();
                            assessConfirmBean.productcode = fundcode;
                            assessConfirmBean.productcompany = fund_company;
                            assessConfirmBean.productprice = price;
                            assessConfirmBean.type = "1";
                            assessConfirmBean.IS_ABLE = subsStatusBean.data.get(0).IS_ABLE;
                            String IS_AGREEMENT = subsStatusBean.data.get(0).IS_AGREEMENT;
                            assessConfirmBean.IS_AGREEMENT = IS_AGREEMENT;
                            assessConfirmBean.IS_OPEN = subsStatusBean.data.get(0).IS_OPEN;
                            assessConfirmBean.IS_VALIB_RISK_LEVEL = subsStatusBean.data.get(0).IS_VALIB_RISK_LEVEL;
                            assessConfirmBean.OFRISK_FLAG = subsStatusBean.data.get(0).OFRISK_FLAG;
                            assessConfirmBean.OFUND_RISKLEVEL_NAME = subsStatusBean.data.get(0).OFUND_RISKLEVEL_NAME;
                            assessConfirmBean.RISK_LEVEL = subsStatusBean.data.get(0).RISK_LEVEL;
                            assessConfirmBean.RISK_LEVEL_NAME = subsStatusBean.data.get(0).RISK_LEVEL_NAME;
                            assessConfirmBean.RISK_RATING = subsStatusBean.data.get(0).RISK_RATING;

                            Intent intent = new Intent();
                            intent.setClass(FundSubsActivity.this, AssessConfirmActivity.class);
                            intent.putExtra("transaction", "true");
                            intent.putExtra("assessConfirm", assessConfirmBean);
                            intent.putExtra("AUTO_BUY", AUTO_BUY);
                            startActivityForResult(intent, REQAGREEMENTCODE);
                        }
                    } else {
                        subsStatusBean = new Gson().fromJson(response, SubsStatusEntity.class);
//                        CentreToast.showText(FundSubsActivity.this, msg);
                        showDialog(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextView(FundDataEntity fundDataBean) {
        fundcode = fundDataBean.data.get(0).FUND_CODE;
        tv_fund_name.setText(fundDataBean.data.get(0).FUND_NAME);
        tv_netvalue.setText(fundDataBean.data.get(0).NAV);
        tv_lowest_investment.setText(fundDataBean.data.get(0).OPEN_SHARE + "\t元");
        tv_usable_money.setText(fundDataBean.data.get(0).ENABLE_BALANCE + "\t元");
        tv_fhfs.setEnabled(true);
        tv_fhfs.setText("份额分红");
    }

    /**
     * 初始化布局逻辑
     */
    private void initData() {
        tv_choose_fund.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_fhfs.setOnClickListener(this);
        bt_true.setClickable(false);
        et_rengou_price.setEnabled(false);
        tv_fhfs.setEnabled(false);

        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
        et_rengou_price.addTextChangedListener(new PriceWatch());


        session = SpUtils.getString(this, "mSession", null);
        fundSubsListen = new FundSubsListen() {
            String mFund_company = "";
            String mFund_code = "";

            @Override
            public void setBuy(String price, String fund_company,String fhfs) {
                if (null != fundDataBean && null != fundDataBean.data && fundDataBean.data.size() > 0) {
                    mFund_company = fundDataBean.data.get(0).FUND_COMPANY;
                    mFund_code = fundDataBean.data.get(0).FUND_CODE;
                }
                mInterface.queryProductSuitability(session, "", "", mFund_company, mFund_code, "331261", new InterfaceCollection.InterfaceCallback() {
                    @Override
                    public void callResult(ResultInfo info) {
                        String code = info.getCode();
                        String msg = info.getMsg();
                        HashMap<String, String> resultMap = null;
                        if ("0".equalsIgnoreCase(code)) {
                            resultMap = (HashMap<String, String>) info.getData();
                            if (null == resultMap) {
                                showDialog("数据异常");
                                return;
                            }
                            //弹框逻辑
                            //是否可以委托
                            if ("0".equalsIgnoreCase(resultMap.get("ENABLE_FLAG"))) {//不可委托
                                //尊敬的客户:\n       根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理
                                CancelDialog.cancleDialog(FundSubsActivity.this, "", 4000, new CancelDialog.PositiveClickListener() {
                                    @Override
                                    public void onPositiveClick() {
                                    }
                                }, null);
                                return;
                            }
//                            else {
//                                //可以委托 判断是否需要视频录制
//                                if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
//                                    CancelDialog.cancleDialog(FundSubsActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
//                                    return;
//                                }
//                            }
                            //跳转到适用性页面
                            Intent intent = new Intent(FundSubsActivity.this, RiskConfirmActivity.class);
                            intent.putExtra("resultMap", resultMap);
                            FundSubsActivity.this.startActivityForResult(intent, REQUESTCODE);
                        } else {
                            showDialog(msg);
                        }
                    }
                });
            }
        };
        et_fund_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String fundcode = s.toString();
                if (!TextUtils.isEmpty(fundcode) && s.length() == 6) {

                    getFundData(fundcode, "");
                    et_rengou_price.setText("");
                    et_rengou_price.setEnabled(true);
                } else {
                    if(fundDataBean!=null)
                        fundDataBean.data = null;
                    clearView(false);
                }
            }
        });
    }

    private void clearView(boolean is) {
        if (is) {
            et_fund_code.setText("");
        }
        et_rengou_price.setText("");
        tv_fund_name.setText("");
        tv_netvalue.setText("");
        tv_lowest_investment.setText("");
        tv_usable_money.setText("");
        tv_fhfs.setText("");
        bt_true.setClickable(false);
        et_rengou_price.setEnabled(false);
        tv_fhfs.setEnabled(false);
        bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
    }

    public void startFinish() {
//        CentreToast.showText(FundSubsActivity.this, "委托成功");
        CentreToast.showText(this, "委托已提交", true);
        clearView(true);
    }


    /**
     * 获取布局id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_subs;
    }

    /**
     * 此界面的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_choose_fund:
                intent.setClass(this, FundProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("point", point);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bt_true:
                String code = et_fund_code.getText().toString();
                if (null == fundDataBean){
                    fundDataBean = new FundDataEntity();
                    fundDataBean.data =new ArrayList();
                    FundDataEntity.Data data = fundDataBean.getData();
                    data.FUND_CODE = code;
                    fundDataBean.data.add(data);
                }else{
                    String mFund_company=fundDataBean.data.get(0).FUND_COMPANY;
                    fundDataBean.data =new ArrayList();
                    FundDataEntity.Data data = fundDataBean.getData();
                    data.FUND_CODE = code;
                    data.FUND_NAME = tv_fund_name.getText().toString();
                    data.FUND_COMPANY = mFund_company;
                    fundDataBean.data.add(data);
                }

                if (Helper.getInstance().isNeedShowRiskDialog()) {
                    Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Intent intent = new Intent(FundSubsActivity.this, RiskEvaluationActivity.class);
                            intent.putExtra("isLogin", true);
                            FundSubsActivity.this.startActivity(intent);
                        }
                    }, new CancelDialog.NagtiveClickListener() {
                        @Override
                        public void onNagtiveClick() {
                            FundSubsDialog dialog = new FundSubsDialog(FundSubsActivity.this, fundDataBean, et_rengou_price.getText().toString(), tv_fhfs.getText().toString(),fundSubsListen);
                            dialog.show();
                        }
                    });
                } else {
                    FundSubsDialog dialog = new FundSubsDialog(this, fundDataBean, et_rengou_price.getText().toString(),tv_fhfs.getText().toString(),fundSubsListen);
                    dialog.show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fhfs:
                Helper.showItemSelectDialog(this,getWidth(),tv_fhfs.getText().toString(),new Helper.OnItemSelectedListener(){
                    @Override
                    public void getSelectedItem(String content) {
                        tv_fhfs.setText(content);
                    }
                },false,new String[]{"现金分红", "份额分红"});
                break;
        }
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),FundSubsActivity.this.toString());
    }


    private int  getWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET && resultCode == RESULT_OK) {//产品列表返回
            dissmissKeyboardUtil();
            point = intent.getIntExtra("point", -1);
            et_fund_code.setText(intent.getStringExtra("FUND_CODE"));
            getFundData(intent.getStringExtra("FUND_CODE"), intent.getStringExtra("FUND_COMPANY"));
        }
        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议页面返回
            et_rengou_price.setText("");
            startFinish();
        }
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {//风险同意书签署返回
            buy_rengou(et_rengou_price.getText().toString().trim(), fundDataBean.data.get(0).FUND_COMPANY);
        }
    }

    class PriceWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                bt_true.setClickable(true);
                bt_true.setBackgroundResource(R.drawable.button_login_pitchon);
            } else {
                bt_true.setClickable(false);
                bt_true.setBackgroundResource(R.drawable.button_login_unchecked);
            }
        }
    }

    public interface FundSubsListen {
        void setBuy(String price, String fund_company,String fhfs);
    }
}
