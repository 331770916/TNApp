package com.tpyzq.mobile.pangu.activity.trade.open_fund;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.ChangNeiSubscriptionBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ClearData;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.FundSubscriptionDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 刘泽鹏  2016.8.15
 * 基金认购界面
 */
public class FundSubscriptionActivity extends BaseActivity implements View.OnClickListener, ClearData {

    private static final String TAG = "FundSubscription";
    private Button OK = null;
    private EditText etCNFundCode, etCNFundAmount = null; //基金代码   认购金额   输入框
    private TextView tvCNFundNameValue, tvCNStockholderNumValue, tvCNExpendableFundValue = null;//基金名称  股东账号  可用资金
    private String mSession;
    private final int MAXNUM = 6; //股票代码输入框的最大值
    private HashMap<String, String> map;   //数据源

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        this.findViewById(R.id.ivFundSubscription_back).setOnClickListener(this);                     //返回按钮销毁界面
        OK = (Button) this.findViewById(R.id.tvCNFundSubscribeQueDing);                             //点击确定按钮
        etCNFundCode = (EditText) this.findViewById(R.id.etCNFundCode);                                //基金代码
        etCNFundAmount = (EditText) this.findViewById(R.id.etCNFundAmount);                            //认购金额
        tvCNFundNameValue = (TextView) this.findViewById(R.id.tvCNFundNameValue);                     //基金名称
        tvCNStockholderNumValue = (TextView) this.findViewById(R.id.tvCNStockholderNumValue);        //股东账号
        tvCNExpendableFundValue = (TextView) this.findViewById(R.id.tvCNExpendableFundValue);        //可用资金

        etCNFundCode.setFocusableInTouchMode(true);         //初始化获取焦点
//        etCNFundCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        etCNFundCode.addTextChangedListener(new TextWatcher() {
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
                    tvCNFundNameValue.setText("");
                    tvCNStockholderNumValue.setText("");
                    tvCNExpendableFundValue.setText("");
                    etCNFundAmount.setText("");
                } else if (s.length() == 0) {
                    etCNFundAmount.setFocusableInTouchMode(false);                          //使其失去焦点
                }
            }
        });

        etCNFundAmount.setFocusableInTouchMode(false);          //初始化 失去焦点
        etCNFundAmount.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
        etCNFundAmount.addTextChangedListener(new TextWatcher() {
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
        return R.layout.activity_fund_subscription;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFundSubscription_back:      //点击销毁当前界面
                this.finish();
                break;
            case R.id.tvCNFundSubscribeQueDing:     ///确定按钮

                String commitMoney = etCNFundAmount.getText().toString();       //获取输入的申认购金额
                String stockCode = etCNFundCode.getText().toString();          //获取输入的 基金代码
                //实例化PopupWindow
                FundSubscriptionDialog dialog = new FundSubscriptionDialog(this, map, commitMoney, stockCode, this, this);
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
        final HashMap map1 = new HashMap();
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

                        tvCNFundNameValue.setText(stock_name);
                        tvCNStockholderNumValue.setText(stock_account);
                        tvCNExpendableFundValue.setText(enable_balance);

                        map.put("stock_name", stock_name);
                        map.put("stock_account", stock_account);
                        map.put("exchange_type", exchange_type);
                        map.put("enable_amount", enable_amount);
                        map.put("enable_balance", enable_balance);
                        map.put("nav", nav);
                        etCNFundAmount.setFocusableInTouchMode(true);
                    }
                } else {
                    ResultDialog.getInstance().showText(bean.getMsg());
                }
            }
        });
    }

    @Override
    public void clear() {
        etCNFundCode.setText("");
        etCNFundAmount.setText("");
        tvCNFundNameValue.setText("");
        tvCNStockholderNumValue.setText("");
        tvCNExpendableFundValue.setText("");
    }
}
