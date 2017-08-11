package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.keyboardlibrary.KeyboardUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskConfirmActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.AssessConfirmActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeAffirmMsgBean;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.OTC_SubscriptionDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * OTC 申购界面
 * 刘泽鹏
 */
public class OTC_SubscribeActivity extends BaseActivity implements View.OnClickListener, IsClickedListener {

    private static final String TAG = "OTC_Subscribe";
    public static final int REQUSET = 1;
    private final int MAXNUM = 6;                                                                                   //股票代码输入框的最大值
    private EditText etOTC_SGProductCode, etOTC_SubscribeMoney;
    private TextView tvOTC_ChooseOTCSGProduct, tvOTC_SGProductNameValue, tvOTC_SGProductJingZhiValue, tvOTC_SGExpendableCapitalValue;
    private Button bnOTC_SubscribeQueDing;
    private HashMap<String, String> map;
    private KeyboardUtil mKeyBoardUtil;
    private ScrollView mScrollView;
    private Dialog submit;
    private OTC_SubscriptionDialog dialog;

    private static int REQUESTCODE = 1001; //进入风险确认页面的请求码
    private static int REQAGREEMENTCODE = 1002; //进入签署协议页面的请求码
    private String mSession;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");                                                                //初始化获取产品列表信息
        etOTC_SGProductCode = (EditText) this.findViewById(R.id.etOTC_SGProductCode);                             //产品代码输入框
        etOTC_SubscribeMoney = (EditText) this.findViewById(R.id.etOTC_SubscribeMoney);                           //申购金额输入框
        tvOTC_ChooseOTCSGProduct = (TextView) this.findViewById(R.id.tvOTC_ChooseOTCSGProduct);                  //选择OTC产品按钮
        tvOTC_SGProductNameValue = (TextView) this.findViewById(R.id.tvOTC_SGProductNameValue);                  //产品名称
        tvOTC_SGProductJingZhiValue = (TextView) this.findViewById(R.id.tvOTC_SGProductJingZhiValue);           //产品净值
        tvOTC_SGExpendableCapitalValue = (TextView) this.findViewById(R.id.tvOTC_SGExpendableCapitalValue);    //可用资金

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.fundRootLayout);
        initMoveKeyBoard(rootLayout, null,etOTC_SGProductCode);

        tvOTC_ChooseOTCSGProduct.setOnClickListener(this);                                                        //选择OTC产品添加监听
        this.findViewById(R.id.ivOTC_Subscribe_back).setOnClickListener(this);                                    //返回按钮
        bnOTC_SubscribeQueDing = (Button) this.findViewById(R.id.bnOTC_SubscribeQueDing);                        //确定按钮
        bnOTC_SubscribeQueDing.setOnClickListener(this);                                                          //添加监听

        etOTC_SGProductCode.setFocusableInTouchMode(true);                                   //初始化产品代码输入框获取焦点
        etOTC_SGProductCode.addTextChangedListener(new TextWatcher() {                       //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == MAXNUM) {                                                           //当输入的代码为6位数时请求数据
                    getAffirmMsg(mSession, s.toString());                                              //根据输入的代码获取确认信息
                    etOTC_SubscribeMoney.setFocusableInTouchMode(true);
                } else if (s.length() > 0 && s.length() < MAXNUM) {
                    //给产品名称，净值，
                    map =null;
                    etOTC_SubscribeMoney.setFocusableInTouchMode(false);
                    tvOTC_SGProductNameValue.setText("");
                    tvOTC_SGProductJingZhiValue.setText("");
                    tvOTC_SGExpendableCapitalValue.setText("");
                } else if (s.length() == 0) {
                    etOTC_SubscribeMoney.setFocusableInTouchMode(false);                            //当代码输入框内容为空时，使金额输入框失去焦点
                }
            }
        });

        etOTC_SubscribeMoney.setFocusableInTouchMode(false);                                        //初始化使申购金额代码框失去焦点
        etOTC_SubscribeMoney.addTextChangedListener(new TextWatcher() {                             //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {                                                                  //如果申购金额输入框有数据
                    bnOTC_SubscribeQueDing.setBackgroundResource(R.drawable.lonin);                 //背景亮
                    bnOTC_SubscribeQueDing.setEnabled(true);                                        //可点击
                } else {
                    bnOTC_SubscribeQueDing.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                    bnOTC_SubscribeQueDing.setEnabled(false);                                       //不可点击
                }
            }
        });

        bnOTC_SubscribeQueDing.setEnabled(false);                                                   //初始化使确定按钮不可点击
        bnOTC_SubscribeQueDing.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色

    }


    /**
     * 获取确认信息
     */
    private void getAffirmMsg(final String mSession, final String stockCode) {
        submit = LoadingDialog.initDialog(this, "加载中...");
        submit.show();

        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("FLAG", "true");
        map2.put("OPER_TYPE", "1");
        map1.put("funcid", "730206");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscribeAffirmMsgBean>() {
                }.getType();
                OTC_SubscribeAffirmMsgBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<OTC_SubscribeAffirmMsgBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_SubscribeActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_SubscribeActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        map = new HashMap<String, String>();
                        OTC_SubscribeAffirmMsgBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();                //产品名称
                        String prodta_no = dataBean.getPRODTA_NO();                //产品TA编号
                        String prod_no = dataBean.getPROD_CODE();                //产品编号
                        String last_price = dataBean.getLAST_PRICE();              //产品净值
                        String enable_balance = dataBean.getENABLE_BALANCE();      //可用资金

                        //给产品名称，净值，可用资金赋值
                        tvOTC_SGProductNameValue.setText(prod_name);
                        tvOTC_SGProductJingZhiValue.setText(last_price);
                        tvOTC_SGExpendableCapitalValue.setText(enable_balance);

                        map.put("prod_name", prod_name);
                        map.put("prodta_no", prodta_no);
                        map.put("prod_no", prod_no);
                    }

                    IsMakenappointment(mSession, stockCode);        //查询是否预约

                } else {
                    //给产品名称，净值，可用资金赋值
                    tvOTC_SGProductNameValue.setText("");
                    tvOTC_SGProductJingZhiValue.setText("");
                    tvOTC_SGExpendableCapitalValue.setText("");

//                    Toast toast = Toast.makeText(OTC_SubscribeActivity.this, msg, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    LinearLayout toastView = (LinearLayout) toast.getView();
//                    ImageView imageCodeProject = new ImageView(OTC_SubscribeActivity.this);
//                    toastView.addView(imageCodeProject, 0);
//                    toast.show();
                    CentreToast.showText(OTC_SubscribeActivity.this,msg);
                    if(submit != null){
                        submit.dismiss();
                    }
                }

            }
        });
    }


    private void IsMakenappointment(String mSession, String stockCode) {
        HashMap map = new HashMap();
        map.put("TOKEN", mSession);
        map.put("FUNCTIONCODE", "HQLNG106");
        HashMap hashMap = new HashMap();
        hashMap.put("prod_code", stockCode);
        hashMap.put("fund_account", UserUtil.capitalAccount);
        map.put("PARAMS", hashMap);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_WA(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.dismiss();
                LogUtil.e(TAG, e.toString());
                showCenterDialog("没有请求到数据，请重新再试一次");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    if(submit != null){
                        submit.dismiss();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");
                    if ("200".equals(code)) {
                        JSONObject message = jsonObject.getJSONObject("message");
                        if (message != null && message.length() > 0) {
                            String order = message.getString("order");
                            String force = message.getString("forceprod");
                            String isorder = message.getString("isorder");
                            //（200：成功，10201：入参错误，10203：没有数据或产品未发布，500：系统异常）
                            if ("0".equals(force)) {         //是否强制预约(0:是 1:否)
                                if ("0".equals(isorder)) {    //是否已经预约(0:是 1:否)
                                    setFocus(true);//是否已经预约(0:是 1:否)
                                }else if ("0".equals(order)) { //是否可以预约(0:可以预约 1:无法预约 )
                                    showCenterDialog("本产品需要先预约才能购买");

                                    } else {
                                        setFocus(true);
                                        bnOTC_SubscribeQueDing.setText("已售罄");
                                        bnOTC_SubscribeQueDing.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                                    }

                                } else {
                                    setFocus(true);
                                }

                            } else {
                                setFocus(true);
                            }
                    } else if ("10203".equals(code)) {
                        setFocus(true);
                    } else {
                        showCenterDialog(code + "," + type);
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
        if (requestCode == OTC_SubscriptionActivity.REQUSET && resultCode == RESULT_OK) {
            dissmissKeyboardUtil();
            etOTC_SGProductCode.setFocusableInTouchMode(true);
            etOTC_SGProductCode.setText(data.getStringExtra("getStockCode"));
            etOTC_SGProductCode.setSelection(etOTC_SGProductCode.getText().length());
        }
        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议成功返回
            etOTC_SubscribeMoney.setText("");  //清空认购价格
        }
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {//风险通知书成功返回
            final String SubscriptionMoney = etOTC_SubscribeMoney.getText().toString();       //获取输入的认购金额
            final String stockCode = etOTC_SGProductCode.getText().toString();
            InterfaceCollection.getInstance().getAffirm("4",stockCode, map.get("prod_name"), mSession, SubscriptionMoney, new InterfaceCollection.InterfaceCallback() {
                @Override
                public void callResult(ResultInfo info) {
                    String code = info.getCode();
                    String msg = info.getMsg();
                    if (code.equals("-6")) {
                        Intent intent = new Intent(OTC_SubscribeActivity.this, TransactionLoginActivity.class);
                        OTC_SubscribeActivity.this.startActivity(intent);
                        if (null!=dialog&&dialog.isShowing()) {
                            dialog.dismiss();
                            finish();
                        }
                    } else if ("0".equalsIgnoreCase(code)) {
                        AssessConfirmEntity assessConfirmBean = (AssessConfirmEntity)info.getData();
                        if("0".equalsIgnoreCase(assessConfirmBean.IS_OPEN)&&"0".equalsIgnoreCase(assessConfirmBean.IS_AGREEMENT)){
                            getProductMsg(stockCode, SubscriptionMoney);
                        }else {
                            Intent intent = new Intent();
                            intent.putExtra("assessConfirm",assessConfirmBean);
                            intent.putExtra("transaction", "true");
                            intent.setClass(OTC_SubscribeActivity.this,AssessConfirmActivity.class);
                            OTC_SubscribeActivity.this.startActivityForResult(intent,REQAGREEMENTCODE);
                            if (null!=dialog&&dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    } else {
                        showDialog(msg);
                    }
                }
            });
        }
    }
    private void getProductMsg(String stockCode, String subscriptionMoney) {
        InterfaceCollection.getInstance().getProductMsg("730202",mSession,stockCode,map.get("prodta_no") , subscriptionMoney, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                if ("-6".equalsIgnoreCase(code)) {
                    Intent intent = new Intent(OTC_SubscribeActivity.this, TransactionLoginActivity.class);
                    OTC_SubscribeActivity.this.startActivity(intent);
                    if (null!=dialog&&dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }
                    finish();
                } else
                if (("0").equalsIgnoreCase(code)) {
                    CentreToast.showText(OTC_SubscribeActivity.this,"委托已提交",true);
                    wipeData();
                }else {
                    showDialog(msg);
                    if (null!=dialog&&dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }
                }
            }
        });
    }

    private void showCenterDialog(String msg){
        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),OTC_SubscribeActivity.class.toString());
        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
            @Override
            public void confirmOnclick() {
                wipeData();
                customCenterDialog.dismiss();
            }
        });
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),OTC_SubscribeActivity.class.toString());
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__subscribe;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOTC_Subscribe_back:                     //点击返回销毁当前界面
                this.finish();
                break;
            case R.id.tvOTC_ChooseOTCSGProduct:                 //跳转 选择OTC产品界面
                Intent intent = new Intent(this, OTC_SubscribeProductActivity.class);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bnOTC_SubscribeQueDing:                   //点击弹出申购确认信息
                String SubscriptionMoney = etOTC_SubscribeMoney.getText().toString();       //获取输入的认购金额
                String stockCode = etOTC_SGProductCode.getText().toString();          //获取输入的 输入代码
                if (map==null){
                    map =new HashMap<>();
                    map.put("prodta_no","");
                }
                //实例化PopupWindow
                dialog = new OTC_SubscriptionDialog(this,"OTC申购",map.get("prodta_no"),SubscriptionMoney, stockCode, this);
                if (Helper.getInstance().isNeedShowRiskDialog()) {
                    Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Intent intent = new Intent(OTC_SubscribeActivity.this, RiskEvaluationActivity.class);
                            intent.putExtra("isLogin", true);
                            OTC_SubscribeActivity.this.startActivity(intent);
                        }
                    }, new CancelDialog.NagtiveClickListener() {
                        @Override
                        public void onNagtiveClick() {
                            dialog.show();
                        }
                    });
                } else {
                    dialog.show();
                }
                break;
        }
    }


    @Override
    public void callBack(boolean isOk) {
        InterfaceCollection.getInstance().queryProductSuitability(mSession, map.get("prodta_no"), map.get("prod_no"), "", "", "331261", new InterfaceCollection.InterfaceCallback() {
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
                        CancelDialog.cancleDialog(OTC_SubscribeActivity.this, "", 4000, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {}
                        },null);
                        return;
                    }
                    //双录也暂时不做判断
//                    else {
//                        //可以委托 判断是否需要视频录制
//                        if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
//                            CancelDialog.cancleDialog(OTC_SubscribeActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
//                            return;
//                        }
//                    }
                    //跳转到适用性页面
                    Intent intent = new Intent(OTC_SubscribeActivity.this, RiskConfirmActivity.class);
                    intent.putExtra("resultMap",resultMap);
                    OTC_SubscribeActivity.this.startActivityForResult(intent, REQUESTCODE);
                } else {
                    showDialog(msg);
                }
            }
        });
        if (null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
    }


    /**
     * 清空数据
     */
    private void wipeData() {
        etOTC_SubscribeMoney.setText("");
        tvOTC_SGProductNameValue.setText("");
        tvOTC_SGProductJingZhiValue.setText("");
        tvOTC_SGExpendableCapitalValue.setText("");
    }

    private void setFocus(Boolean isFocus){
        etOTC_SubscribeMoney.setFocusableInTouchMode(isFocus);
//        etOTC_SubscribeMoney.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        bnOTC_SubscribeQueDing.setEnabled(isFocus);
    }
}
