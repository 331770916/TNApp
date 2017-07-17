package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.FundSubscribePopupWindow;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FundSubscribeQueRenBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 刘泽鹏
 * 货币基金申购
 */
public class CurrencyFundSubscribeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CurrencyFundSub";
    private String mSession;
    private EditText etFundCode, etFundAmount = null;
    private TextView tvFundNameValue, tvStockholderNumValue,tvExpendableFundValue = null;
    private Button OK = null;
    private final int MAXNUM = 6; //股票代码输入框的最大值
    private HashMap<String,String> map;   //数据源
    private LinearLayout ll_father;


    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        tvFundNameValue= (TextView) this.findViewById(R.id.tvFundNameValue);                  //基金名称
        tvStockholderNumValue= (TextView) this.findViewById(R.id.tvStockholderNumValue);    //股东账号
        tvExpendableFundValue= (TextView) this.findViewById(R.id.tvExpendableFundValue);    //可用资金

        etFundCode = (EditText) this.findViewById(R.id.etFundCode);          //基金代码 输入框
        etFundCode.setFocusableInTouchMode(true);                             //初始化   使其获得焦点
        //添加监听
        etFundCode.addTextChangedListener(new TextWatcher() {
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
                }else if(s.length()==0){
                    etFundAmount.setFocusableInTouchMode(false);                          //使其失去焦点
                }
            }
        });
        ll_father = (LinearLayout) this.findViewById(R.id.ll_father);                                         //键盘父布局
        initMoveKeyBoard(ll_father, null,etFundCode);
        etFundAmount = (EditText) this.findViewById(R.id.etFundAmount);     //申购金额 输入框
        etFundAmount.setFocusableInTouchMode(false);                          //初始化   使其失去焦点
        etFundAmount.addTextChangedListener(new TextWatcher() {
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

        this.findViewById(R.id.ivCurrencyFundSubscribe_back).setOnClickListener(this);  //返回销毁当前界面
        OK = (Button) this.findViewById(R.id.tvCurrencySubscribeQueDing);    //确定
        OK.setEnabled(false);                                                   //初始化使其不可点击
        OK.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色
        OK.setOnClickListener(this);                                            //添加监听


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund_subscribe;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvCurrencySubscribeQueDing:   //确定

                String fundAmount = etFundAmount.getText().toString();       //获取输入的申购金额
                String stockCode = etFundCode.getText().toString();          //获取输入的 基金代码
                //实例化PopupWindow
                FundSubscribePopupWindow popupWindow = new FundSubscribePopupWindow(this,this,map,fundAmount,stockCode);
                popupWindow.setFocusable(true);     //获取焦点
                ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
                popupWindow.setBackgroundDrawable(dw);      //设置背景颜色
                popupWindow.setOutsideTouchable(true);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                //消失的时候设置窗体背景变亮
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                    }
                });

                //显示窗口
                popupWindow.showAtLocation(this.findViewById(R.id.CurrencyFundActivity),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                break;

            case R.id.ivCurrencyFundSubscribe_back:     //销毁当前界面
                this.finish();
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
        map1.put("funcid", "300446");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(CurrencyFundSubscribeActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (!response.equals("") && response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<FundSubscribeQueRenBean>() {}.getType();
                    FundSubscribeQueRenBean bean = gson.fromJson(response, type);
                    String msgCode = bean.getCode();
                    List<FundSubscribeQueRenBean.DataBean> data = bean.getData();
                    if(msgCode.equals("-6")){
                        Intent intent = new Intent(CurrencyFundSubscribeActivity.this, TransactionLoginActivity.class);
                        CurrencyFundSubscribeActivity.this.startActivity(intent);
                        finish();
                    }else if(msgCode.equals("0")&& data!=null){
                        for(int i=0;i<data.size();i++){
                            map = new HashMap<String,String>();
                            FundSubscribeQueRenBean.DataBean dataBean = data.get(i);
                            String stock_name = dataBean.getSTOCK_NAME();           //基金名称
                            tvFundNameValue.setText(stock_name);
                            String stock_account = dataBean.getSTOCK_ACCOUNT();     //股东账号
                            tvStockholderNumValue.setText(stock_account);
                            String enable_balance = dataBean.getENABLE_BALANCE();   //可用资金
                            tvExpendableFundValue.setText(enable_balance);
                            String enable_amount = dataBean.getENABLE_AMOUNT();     //可用份额
                            String exchange_type = dataBean.getEXCHANGE_TYPE();     //市场
                            map.put("stock_name",stock_name);
                            map.put("stock_account",stock_account);
                            map.put("enable_balance",enable_balance);
                            map.put("enable_amount",enable_amount);
                            map.put("exchange_type",exchange_type);
                            map.put("mSession",mSession);
                            etFundAmount.setFocusableInTouchMode(true);
                        }
                    }else {
                        MistakeDialog.showDialog(bean.getMsg(),CurrencyFundSubscribeActivity.this);
                    }
                }

            }
        });
    }


}
