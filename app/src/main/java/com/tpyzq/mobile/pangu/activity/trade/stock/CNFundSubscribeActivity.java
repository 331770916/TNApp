package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.keyboardlibrary.KeyboardUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ChangNeiSubscriptionBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ClearData;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.CNFundSubscribeDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 场内基金申购页面
 * 刘泽鹏
 */
public class CNFundSubscribeActivity extends BaseActivity implements View.OnClickListener, ClearData {

    private static final String TAG = "CNFundSubscribe";
    private String mSession;
    private Button OK = null;
    private EditText etCnFundCode, etCnFundAmount;
    private TextView tvCnFundNameValue, tvCnFundNetValueValue, tvCnStockholderNumValue, tvCnExpendableFundValue = null;
    private final int MAXNUM = 6; //股票代码输入框的最大值
    private HashMap<String, String> map;   //数据源
    private ScrollView mScrollView;
    private KeyboardUtil mKeyBoardUtil;
    private LinearLayout ll_father;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        this.findViewById(R.id.ivCnFundSubscribe_back).setOnClickListener(this);                      //返回按钮销毁界面
        OK = (Button) this.findViewById(R.id.tvCnSubscribeQueDing);                                 //点击确定按钮
        OK.setOnClickListener(this);
        etCnFundCode = (EditText) this.findViewById(R.id.etCnFundCode);                                //基金代码
        etCnFundAmount = (EditText) this.findViewById(R.id.etCnFundAmount);                            //申购金额
        tvCnFundNameValue = (TextView) this.findViewById(R.id.tvCnFundNameValue);                     //基金名称
        tvCnFundNetValueValue = (TextView) this.findViewById(R.id.tvCnFundNetValueValue);            //资金净值
        tvCnStockholderNumValue = (TextView) this.findViewById(R.id.tvCnStockholderNumValue);        //股东账号
        tvCnExpendableFundValue = (TextView) this.findViewById(R.id.tvCnExpendableFundValue);        //可用资金

        ll_father = (LinearLayout) this.findViewById(R.id.ll_father);                                         //键盘父布局
        initMoveKeyBoard(ll_father, null,etCnFundCode);
        etCnFundCode.setFocusableInTouchMode(true);             //初始化 获得焦点
        etCnFundAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        etCnFundCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == MAXNUM) {                                        //当输入的基金代码为6位数时请求数据
//                    login(s.toString());
                    getMsg(s.toString());//511880
                } else if (s.length() > 0 && s.length() < MAXNUM) {
                    //给产品名称，净值，可用资金赋值
                    tvCnFundNameValue.setText("");              //基金名称
                    tvCnFundNetValueValue.setText("");                 //资金净值
                    tvCnStockholderNumValue.setText("");    //股东账号
                    tvCnExpendableFundValue.setText("");   //可用资金
                    etCnFundAmount.setText("");
                } else if (s.length() == 0) {
                    etCnFundAmount.setFocusableInTouchMode(false);                          //使其失去焦点
                }
            }
        });

        etCnFundAmount.setFocusableInTouchMode(false);          //初始化 失去焦点
        etCnFundAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        etCnFundAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {                                           //如果申购金额输入框有数据
                    OK.setBackgroundResource(R.drawable.lonin);                 //背景亮
                    OK.setEnabled(true);                                        //可点击
                } else {
                    OK.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                    OK.setEnabled(false);                                       //不可点击
                }
            }
        });

        OK.setEnabled(false);                                                   //初始化使其不可点击
        OK.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色
        OK.setOnClickListener(this);                                            //添加监听
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cnfund_subscribe;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCnFundSubscribe_back:      //点击销毁当前界面
                this.finish();
                break;
            case R.id.tvCnSubscribeQueDing:     ///确定按钮
                String commitMoney = etCnFundAmount.getText().toString();       //获取输入的申认购金额
                String stockNav = tvCnFundNetValueValue.getText().toString().trim();             //资金净值
                String stockCode = etCnFundCode.getText().toString().trim();          //获取输入的 基金代码
                String stockName = tvCnFundNameValue.getText().toString().trim();                 //股票名称
                String stockAccount = tvCnStockholderNumValue.getText().toString().trim();      //股东账号
                String exchangeType = map.get("exchange_type");             //市场
                //实例化PopupWindow
                CNFundSubscribeDialog dialog = new CNFundSubscribeDialog(this, stockName, stockCode, stockAccount, commitMoney, stockNav, exchangeType, this, this);
                dialog.show();
                break;
        }
    }


    /**
     * 获取确认信息
     *
     * @param stockCode
     */
    private void getMsg(String stockCode) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("SECU_CODE", stockCode);
        map2.put("OPEN_TYPE", "0");
        map1.put("funcid", "300198");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ChangNeiSubscriptionBean>() {
                }.getType();
                ChangNeiSubscriptionBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<ChangNeiSubscriptionBean.DataBean> data = bean.getData();
                if (code.equals("0")) {
                    for (int i = 0; i < data.size(); i++) {
                        map = new HashMap<String, String>();
                        ChangNeiSubscriptionBean.DataBean dataBean = data.get(i);
                        String stock_name = dataBean.getSTOCK_NAME();           //基金名称
                        String stock_account = dataBean.getSTOCK_ACCOUNT();     //股东账号
                        String exchange_type = dataBean.getEXCHANGE_TYPE();     //市场
                        String enable_amount = dataBean.getENABLE_AMOUNT();     //可用份额
                        String enable_balance = dataBean.getENABLE_BALANCE();   //可用资金
                        String nav = dataBean.getNAV();                         //净值

                        DecimalFormat mFormat1 = new DecimalFormat("#0.000");
                        tvCnFundNameValue.setText(stock_name);              //基金名称
                        tvCnFundNetValueValue.setText(mFormat1.format(Double.parseDouble(nav)));                 //资金净值
                        tvCnStockholderNumValue.setText(stock_account);    //股东账号
                        tvCnExpendableFundValue.setText(enable_balance);   //可用资金

                        map.put("stock_name", stock_name);
                        map.put("stock_account", stock_account);
                        map.put("exchange_type", exchange_type);
                        map.put("enable_amount", enable_amount);
                        map.put("enable_balance", enable_balance);
                        map.put("nav", nav);
                        etCnFundAmount.setFocusableInTouchMode(true);
                    }
                } else {
                    MistakeDialog.showDialog("该基金不存在",CNFundSubscribeActivity.this);
                }
            }
        });
    }

    @Override
    public void clear() {
        etCnFundCode.setText("");
        etCnFundAmount.setText("");
        tvCnFundNameValue.setText("");
        tvCnFundNetValueValue.setText("");
        tvCnStockholderNumValue.setText("");
        tvCnExpendableFundValue.setText("");
    }
}
