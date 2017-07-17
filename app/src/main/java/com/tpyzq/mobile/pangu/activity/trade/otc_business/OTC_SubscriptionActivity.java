package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionAffirmMsgBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListEntity;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListMasBean;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.OTC_SubscriptionDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 刘泽鹏
 * OTC 认购界面
 */
public class OTC_SubscriptionActivity extends BaseActivity implements View.OnClickListener, IsClickedListener {

    private static final String TAG = "OTC_Subscription";
    public static final int REQUSET = 1;
    private final int MAXNUM = 6; //股票代码输入框的最大值
    private EditText etOTC_ProductCode, etOTC_SubscriptionMoney = null;        //产品代码和认购金额输入框
    private TextView tvOTC_ChooseOTCProduct, tvOTC_ProductNameValue, tvOTC_ProductJingZhiValue, tvOTC_ExpendableCapitalValue = null;                              //OTC产品选择按钮，产品名称，净值，可用资金
    private Button bnOTC_SubscriptionQueDing;
    private HashMap<String, String> map;
    private ArrayList<OTC_SubscriptionListEntity> list;                             //OTC产品列表的数据源
    private int point = -1;
    private KeyboardUtil mKeyBoardUtil;
    private ScrollView mScrollView;
    private Dialog submit;
    private String mSession ;
    private OTC_SubscriptionDialog dialog;
    private static int REQUESTCODE = 1001; //进入风险确认页面的请求码
    private static int REQAGREEMENTCODE = 1002; //进入签署协议页面的请求码

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");                                  //获取seesion
        getListMsg(mSession);                                                                               //初始化获取OTC产品列表信息
        this.etOTC_ProductCode = (EditText) this.findViewById(R.id.etOTC_ProductCode);
        this.etOTC_SubscriptionMoney = (EditText) this.findViewById(R.id.etOTC_SubscriptionMoney);
        this.tvOTC_ChooseOTCProduct = (TextView) this.findViewById(R.id.tvOTC_ChooseOTCProduct);
        tvOTC_ChooseOTCProduct.setOnClickListener(this);                                                 //点击选择OTC产品按钮
        this.tvOTC_ProductNameValue = (TextView) this.findViewById(R.id.tvOTC_ProductNameValue);
        this.tvOTC_ProductJingZhiValue = (TextView) this.findViewById(R.id.tvOTC_ProductJingZhiValue);
        this.tvOTC_ExpendableCapitalValue = (TextView) this.findViewById(R.id.tvOTC_ExpendableCapitalValue);
        this.findViewById(R.id.ivOTC_Subscription_back).setOnClickListener(this);                        //返回按钮
        bnOTC_SubscriptionQueDing = (Button) this.findViewById(R.id.bnOTC_SubscriptionQueDing);        //确定按钮
        bnOTC_SubscriptionQueDing.setOnClickListener(this);

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.fundRootLayout);
        initMoveKeyBoard(rootLayout, null,etOTC_ProductCode);

        etOTC_ProductCode.setFocusableInTouchMode(true);                                                 //初始化   使其获得焦点
        etOTC_ProductCode.addTextChangedListener(new TextWatcher() {                                     //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == MAXNUM) {                                        //当输入的代码为6位数时请求数据
                    getAffirmMsg(mSession, s.toString());                            //根据输入的代码获取确认信息
                } else if (s.length() > 0 && s.length() < MAXNUM) {
                    //给产品名称，净值，可用资金赋值
                    tvOTC_ProductNameValue.setText("");
                    tvOTC_ProductJingZhiValue.setText("");
                    tvOTC_ExpendableCapitalValue.setText("");
                } else if (s.length() == 0) {
                    etOTC_SubscriptionMoney.setFocusableInTouchMode(false);      //当代码输入框内容为空时，使金额输入框失去焦点
                }
            }
        });


        etOTC_SubscriptionMoney.setFocusableInTouchMode(false);                   //初始化   使认购金额输入框失去焦点
        etOTC_SubscriptionMoney.addTextChangedListener(new TextWatcher() {        //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {                                                                     //如果申购金额输入框有数据
                    bnOTC_SubscriptionQueDing.setBackgroundResource(R.drawable.lonin);                 //背景亮
                    bnOTC_SubscriptionQueDing.setEnabled(true);                                        //可点击
                } else {
                    bnOTC_SubscriptionQueDing.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                    bnOTC_SubscriptionQueDing.setEnabled(false);                                       //不可点击
                }
            }
        });


        bnOTC_SubscriptionQueDing.setEnabled(false);                                                   //初始化使确定按钮不可点击
        bnOTC_SubscriptionQueDing.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色
        bnOTC_SubscriptionQueDing.setOnClickListener(this);                                            //添加监听

    }

    /**
     * 获取产品列表信息
     */
    private void getListMsg(String mSession) {
        list = new ArrayList<OTC_SubscriptionListEntity>();
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("OPR_TYPE", "2");
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300502");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscriptionListMasBean>() {
                }.getType();
                OTC_SubscriptionListMasBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_SubscriptionListMasBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_SubscriptionActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_SubscriptionActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_SubscriptionListMasBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();     //产品名称
                        String prod_code = dataBean.getPROD_CODE();     //产品代码
                        OTC_SubscriptionListEntity listIntent = new OTC_SubscriptionListEntity();
                        listIntent.setStockName(prod_name);
                        listIntent.setStockCode(prod_code);
                        listIntent.setFlag(false);
                        list.add(listIntent);
                    }
                } else {
                    ResultDialog.getInstance().showText("网络异常");
                }
            }
        });

    }

    /**
     * 获取确认信息
     */
    private void getAffirmMsg(final String mSession, final String stockCode) {
        submit = LoadingDialog.initDialog(this, "正在提交...");
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
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscriptionAffirmMsgBean>() {
                }.getType();
                OTC_SubscriptionAffirmMsgBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<OTC_SubscriptionAffirmMsgBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_SubscriptionActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_SubscriptionActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        map = new HashMap<String, String>();
                        OTC_SubscriptionAffirmMsgBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();                //产品名称
                        String prod_code = dataBean.getPROD_CODE();                //产品代码
                        String prodta_no = dataBean.getPRODTA_NO();                //产品TA编号
                        String last_price = dataBean.getLAST_PRICE();              //产品净值
                        String enable_balance = dataBean.getENABLE_BALANCE();      //可用资金

                        //给产品名称，净值，可用资金赋值
                        tvOTC_ProductNameValue.setText(prod_name);
                        tvOTC_ProductJingZhiValue.setText(last_price);
                        tvOTC_ExpendableCapitalValue.setText(enable_balance);

                        map.put("prod_name", prod_name);
                        map.put("prod_code", prod_code);
                        map.put("prodta_no", prodta_no);
                    }


                    IsMakenappointment(mSession, stockCode);        //查询是否预约
                } else {
                    submit.dismiss();

                    //给产品名称，净值，可用资金赋值
                    tvOTC_ProductNameValue.setText("");
                    tvOTC_ProductJingZhiValue.setText("");
                    tvOTC_ExpendableCapitalValue.setText("");

                    Toast toast = Toast.makeText(OTC_SubscriptionActivity.this, msg, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastView = (LinearLayout) toast.getView();
                    ImageView imageCodeProject = new ImageView(OTC_SubscriptionActivity.this);
                    toastView.addView(imageCodeProject, 0);
                    toast.show();
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_UPDATE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.dismiss();
                LogUtil.e(TAG, e.toString());
                MistakeDialog.showDialog("没有请求到数据，请重新再试一次", OTC_SubscriptionActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        wipeData();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    submit.dismiss();
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
                                    setFocus(true);
                                } else {
                                    MistakeDialog.specialshowDialog("本产品需要先预约才能购买", OTC_SubscriptionActivity.this, new MistakeDialog.MistakeDialgoListener() {
                                        @Override
                                        public void doPositive() {
                                            wipeData();
                                        }
                                    });
                                }

                            } else {
                                setFocus(true);
                            }
                        }
                    } else if ("10203".equals(code)) {
                        setFocus(true);
                    } else {
                        MistakeDialog.showDialog(code + "," + type, OTC_SubscriptionActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                wipeData();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__subscription;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOTC_Subscription_back:          //销毁当前界面
                this.finish();
                break;
            case R.id.tvOTC_ChooseOTCProduct:           //点击选择OTC产品 传值跳转

                Intent intent = new Intent();
                intent.setClass(this, OTC_ProductActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("point", point);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bnOTC_SubscriptionQueDing:        //点击确定 弹出 确认信息窗口
                String SubscriptionMoney = etOTC_SubscriptionMoney.getText().toString();       //获取输入的认购金额
                String stockCode = etOTC_ProductCode.getText().toString();          //获取输入的 输入代码
                dialog = new OTC_SubscriptionDialog(this,"OTC认购", map.get("prod_name"), SubscriptionMoney, stockCode, this);
                if (Helper.getInstance().isNeedShowRiskDialog()) {
                    Helper.getInstance().showCorpDialog(this, new CancelDialog.PositiveClickListener() {
                        @Override
                        public void onPositiveClick() {
                            Intent intent = new Intent(OTC_SubscriptionActivity.this, RiskEvaluationActivity.class);
                            intent.putExtra("isLogin", true);
                            OTC_SubscriptionActivity.this.startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTC_SubscriptionActivity.REQUSET && resultCode == RESULT_OK) {//选择otc产品
            dissmissKeyboardUtil();
            int position = data.getIntExtra("position", -1);
            etOTC_ProductCode.setFocusableInTouchMode(true);
            etOTC_ProductCode.setText(list.get(position).getStockCode());
            point = data.getIntExtra("position", -1);
            etOTC_ProductCode.setSelection(etOTC_ProductCode.getText().length());
        }
        if (requestCode == REQAGREEMENTCODE && resultCode == RESULT_OK) {//签署协议成功返回
            etOTC_SubscriptionMoney.setText("");  //清空认购价格
        }
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {//风险通知书成功返回
            final String SubscriptionMoney = etOTC_SubscriptionMoney.getText().toString();       //获取输入的认购金额
            final String stockCode = etOTC_ProductCode.getText().toString();
            InterfaceCollection.getInstance().getAffirm("3",stockCode, map.get("prodta_no"), mSession, SubscriptionMoney, new InterfaceCollection.InterfaceCallback() {
                @Override
                public void callResult(ResultInfo info) {
                    String code = info.getCode();
                    String msg = info.getMsg();
                    if (code.equals("-6")) {
                        Intent intent = new Intent(OTC_SubscriptionActivity.this, TransactionLoginActivity.class);
                        OTC_SubscriptionActivity.this.startActivity(intent);
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
                            intent.setClass(OTC_SubscriptionActivity.this,AssessConfirmActivity.class);
                            OTC_SubscriptionActivity.this.startActivityForResult(intent,REQAGREEMENTCODE);
                            if (null!=dialog&&dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    } else {
                        MistakeDialog.showDialog(msg, OTC_SubscriptionActivity.this);
                    }
                }
            });
        }
    }


    @Override
    public void callBack(boolean isOk) {
        //点击按钮回调 跳转到风险通知书
        InterfaceCollection.getInstance().queryProductSuitability(mSession, map.get("prodta_no"), map.get("prod_code"), "", "", "331261", new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                HashMap<String,String> resultMap = null;
                if ("0".equalsIgnoreCase(code)) {
                    resultMap = (HashMap<String,String>)info.getData();
                    if (null == resultMap) {
                        MistakeDialog.showDialog("数据异常", OTC_SubscriptionActivity.this, null);
                        return;
                    }
                    //弹框逻辑
                    //是否可以委托
                    if ("0".equalsIgnoreCase(resultMap.get("ENABLE_FLAG"))) {//不可委托
                        //尊敬的客户:\n       根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理
                        CancelDialog.cancleDialog(OTC_SubscriptionActivity.this, "", 4000, new CancelDialog.PositiveClickListener() {
                            @Override
                            public void onPositiveClick() {}
                        },null);
                        return;
                    }
//                    else {
//                        //可以委托 判断是否需要视频录制
//                        if ("1".equalsIgnoreCase(resultMap.get("NEED_VIDEO_FLAG"))) {
//                            CancelDialog.cancleDialog(OTC_SubscriptionActivity.this,"尊敬的客户:\n根据证监会《证券期货投资者适当性管理办法》，您购买的产品在购买过程中需要通过录音录像进行确认，请到营业部办理",4000,null,null);
//                            return;
//                        }
//                    }
                    //跳转到适用性页面
                    Intent intent = new Intent(OTC_SubscriptionActivity.this, RiskConfirmActivity.class);
                    intent.putExtra("resultMap",resultMap);
                    OTC_SubscriptionActivity.this.startActivityForResult(intent, REQUESTCODE);
                } else {
                    MistakeDialog.showDialog(msg, OTC_SubscriptionActivity.this, null);
                }
            }
        });

        if (null!=dialog&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private void getProductMsg(String stockCode, String subscriptionMoney) {
        InterfaceCollection.getInstance().getProductMsg("730201",mSession,stockCode,map.get("prodta_no") , subscriptionMoney, new InterfaceCollection.InterfaceCallback() {
            @Override
            public void callResult(ResultInfo info) {
                String code = info.getCode();
                String msg = info.getMsg();
                if ("-6".equalsIgnoreCase(code)) {
                    Intent intent = new Intent(OTC_SubscriptionActivity.this, TransactionLoginActivity.class);
                    OTC_SubscriptionActivity.this.startActivity(intent);
                    if (null!=dialog&&dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }
                    finish();
                } else
                if (("0").equalsIgnoreCase(code)) {
                    CentreToast.showText(OTC_SubscriptionActivity.this,"委托已提交",true);
                    wipeData();
                }else {
                    MistakeDialog.showDialog(msg, OTC_SubscriptionActivity.this);
                    if (null!=dialog&&dialog.isShowing()) {
                        dialog.dismiss();
                        finish();
                    }
                }
            }
        });
    }

    /**
     * 清空数据
     */
    private void wipeData() {
        etOTC_ProductCode.setText("");
        etOTC_SubscriptionMoney.setText("");
        tvOTC_ProductNameValue.setText("");
        tvOTC_ProductJingZhiValue.setText("");
        tvOTC_ExpendableCapitalValue.setText("");
        point=-1;
    }

    private void setFocus(Boolean isFocus) {
        etOTC_SubscriptionMoney.setFocusableInTouchMode(isFocus);
//        etOTC_SubscriptionMoney.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        bnOTC_SubscriptionQueDing.setEnabled(isFocus);
    }
}
