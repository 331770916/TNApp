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
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeAffirmMsgBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeEntity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeListBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.OTC_SubscribeDialog;
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
    private ArrayList<OTC_SubscribeEntity> list;
    private HashMap<String, String> map;
    private int point = -1;
    private KeyboardUtil mKeyBoardUtil;
    private ScrollView mScrollView;
    private Dialog submit;

    @Override
    public void initView() {
        final String mSession = SpUtils.getString(this, "mSession", "");
        list = new ArrayList<OTC_SubscribeEntity>();
        getProductList(mSession);                                                                                    //初始化获取产品列表信息
        etOTC_SGProductCode = (EditText) this.findViewById(R.id.etOTC_SGProductCode);                             //产品代码输入框
        etOTC_SubscribeMoney = (EditText) this.findViewById(R.id.etOTC_SubscribeMoney);                           //申购金额输入框
        tvOTC_ChooseOTCSGProduct = (TextView) this.findViewById(R.id.tvOTC_ChooseOTCSGProduct);                  //选择OTC产品按钮
        tvOTC_SGProductNameValue = (TextView) this.findViewById(R.id.tvOTC_SGProductNameValue);                  //产品名称
        tvOTC_SGProductJingZhiValue = (TextView) this.findViewById(R.id.tvOTC_SGProductJingZhiValue);           //产品净值
        tvOTC_SGExpendableCapitalValue = (TextView) this.findViewById(R.id.tvOTC_SGExpendableCapitalValue);    //可用资金

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
                } else if (s.length() > 0 && s.length() < MAXNUM) {
                    //给产品名称，净值，可用资金赋值
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
     * 网络获取OTC产品列表信息
     */
    private void getProductList(String mSession) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("OPR_TYPE", "1");
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300502");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscribeListBean>() {
                }.getType();
                OTC_SubscribeListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_SubscribeListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_SubscribeActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_SubscribeActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_SubscribeListBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();         //产品名称
                        String prod_code = dataBean.getPROD_CODE();         //产品代码
                        OTC_SubscribeEntity intentBean = new OTC_SubscribeEntity();
                        intentBean.setStcokName(prod_name);
                        intentBean.setStockCode(prod_code);
                        intentBean.setFlag(false);
                        list.add(intentBean);
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
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
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
                        String last_price = dataBean.getLAST_PRICE();              //产品净值
                        String enable_balance = dataBean.getENABLE_BALANCE();      //可用资金

                        //给产品名称，净值，可用资金赋值
                        tvOTC_SGProductNameValue.setText(prod_name);
                        tvOTC_SGProductJingZhiValue.setText(last_price);
                        tvOTC_SGExpendableCapitalValue.setText(enable_balance);

                        map.put("prod_name", prod_name);
                        map.put("prodta_no", prodta_no);
                    }

                    IsMakenappointment(mSession, stockCode);        //查询是否预约

                } else {
                    //给产品名称，净值，可用资金赋值
                    tvOTC_SGProductNameValue.setText("");
                    tvOTC_SGProductJingZhiValue.setText("");
                    tvOTC_SGExpendableCapitalValue.setText("");

                    Toast toast = Toast.makeText(OTC_SubscribeActivity.this, msg, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastView = (LinearLayout) toast.getView();
                    ImageView imageCodeProject = new ImageView(OTC_SubscribeActivity.this);
                    toastView.addView(imageCodeProject, 0);
                    toast.show();
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_YY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.dismiss();
                LogUtil.e(TAG, e.toString());
                MistakeDialog.showDialog("没有请求到数据，请重新再试一次", OTC_SubscribeActivity.this, new MistakeDialog.MistakeDialgoListener() {
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
                                        MistakeDialog.showDialog("本产品需要先预约才能购买", OTC_SubscribeActivity.this, new MistakeDialog.MistakeDialgoListener() {
                                            @Override
                                            public void doPositive() {
                                                wipeData();
                                            }
                                        });
                                    } else {
                                        setFocus(false);
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
                        MistakeDialog.showDialog(code + "," + type, OTC_SubscribeActivity.this, new MistakeDialog.MistakeDialgoListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTC_SubscriptionActivity.REQUSET && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            etOTC_SGProductCode.setFocusableInTouchMode(true);
            etOTC_SGProductCode.setText(list.get(position).getStockCode());
            point = data.getIntExtra("position", -1);
            etOTC_SGProductCode.setSelection(etOTC_SGProductCode.getText().length());
        }
        if (requestCode == 100 && resultCode == 500) {
            etOTC_SubscribeMoney.setText("");  //清空认购价格
        }
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
                intent.putExtra("list", list);
                intent.putExtra("point", point);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bnOTC_SubscribeQueDing:                   //点击弹出申购确认信息

                String SubscriptionMoney = etOTC_SubscribeMoney.getText().toString();       //获取输入的认购金额
                String stockCode = etOTC_SGProductCode.getText().toString();          //获取输入的 输入代码
                //实例化PopupWindow
                OTC_SubscribeDialog dialog = new OTC_SubscribeDialog(this, this, map, SubscriptionMoney, stockCode, this);
                dialog.show();

                break;
        }
    }


    @Override
    public void callBack(boolean isOk) {
        //如果  委托成功   清空 首页数据
        if (true == isOk) {
            wipeData();
        }
    }


    /**
     * 清空数据
     */
    private void wipeData() {
        etOTC_SGProductCode.setText("");
        etOTC_SubscribeMoney.setText("");
        tvOTC_SGProductNameValue.setText("");
        tvOTC_SGProductJingZhiValue.setText("");
        tvOTC_SGExpendableCapitalValue.setText("");
        point=-1;
    }

    private void setFocus(Boolean isFocus){
        etOTC_SubscribeMoney.setFocusableInTouchMode(isFocus);
//        etOTC_SubscribeMoney.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        bnOTC_SubscribeQueDing.setEnabled(isFocus);
    }
}
