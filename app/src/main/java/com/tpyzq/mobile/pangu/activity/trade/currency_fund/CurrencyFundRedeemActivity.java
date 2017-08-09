package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
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
import com.tpyzq.mobile.pangu.activity.trade.view.FundRedeemPopupWindow;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.CurrencyFundShuHuiBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 刘泽鹏
 * 货币基金赎回界面
 */
public class CurrencyFundRedeemActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CurrencyFundRedeem";
    private EditText etRedeemFundCode,etRedeemFundAmount=null;
    private TextView tvRedeemFundNameValue,tvRedeemStockholderNumValue,tvRedeemExpendableFundValue=null;
    private Button OK = null;
    private String session;
    private final int MAXNUM = 6; //股票代码输入框的最大值
    private HashMap<String,String> map;   //数据源
    private LinearLayout ll_father;

    @Override
    public void initView() {
        session = SpUtils.getString(this, "mSession", "");
        tvRedeemFundNameValue= (TextView) this.findViewById(R.id.tvRedeemFundNameValue);                //基金名称
        tvRedeemStockholderNumValue= (TextView) this.findViewById(R.id.tvRedeemStockholderNumValue);    //股东账号
        tvRedeemExpendableFundValue= (TextView) this.findViewById(R.id.tvRedeemExpendableFundValue);    //可用份额

        this.etRedeemFundCode= (EditText) this.findViewById(R.id.etRedeemFundCode);     //基金代码 输入框
        this.etRedeemFundCode.setFocusableInTouchMode(true);                              //初始化使其获取焦点
        this.etRedeemFundCode.addTextChangedListener(new TextWatcher() {                  //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.length()==MAXNUM){
//                        etRedeemFundCode.setFocusableInTouchMode(false);
//                        login(s.toString());
                        getMsg(s.toString());
                        etRedeemFundAmount.setFocusableInTouchMode(true);
                    }else if(s.length()==0){
                        etRedeemFundAmount.setFocusableInTouchMode(false);                          //使其失去焦点
                    }else {
                        etRedeemFundAmount.setText("");
                        map = null ;
                    }
            }
        });

        ll_father = (LinearLayout) this.findViewById(R.id.ll_father);                                         //键盘父布局
        initMoveKeyBoard(ll_father, null,etRedeemFundCode);
        this.etRedeemFundAmount= (EditText) this.findViewById(R.id.etRedeemFundAmount);     //赎回份额 输入框
        this.etRedeemFundAmount.setFocusableInTouchMode(false);                               //初始化   使其失去焦点
        this.etRedeemFundAmount.addTextChangedListener(new TextWatcher() {                    //添加监听
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
        this.findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);  //返回销毁当前界面
        OK= (Button) this.findViewById(R.id.tvCurrencyRedeemQueDing);         //确定
        OK.setEnabled(false);                                                   //初始化使其不可点击
        OK.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色
        OK.setOnClickListener(this);                                            //添加监听
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund_redeem;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCurrencyRedeemQueDing:   //确定
                if (map==null){
                    map = new HashMap<String, String>();
                    map.put("exchange_type","");
                    map.put("stock_name","");    // 防止弹窗数据借用借用上一个数据
                    map.put("stock_account","");
                    map.put("session",session);
                }
                String fundAmount = etRedeemFundAmount.getText().toString();       //获取输入的申购金额
                String stockCode = etRedeemFundCode.getText().toString();          //获取输入的 基金代码

                //实例化PopupWindow
                FundRedeemPopupWindow popupWindow= new FundRedeemPopupWindow(this,this,map,fundAmount,stockCode);
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
                popupWindow.showAtLocation(this.findViewById(R.id.CurrencyFundRedeemActivity), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;

            case R.id.ivCurrencyFundRedeem_back:     //销毁当前界面
                this.finish();
                break;
        }
    }


    /*private void login(final String s){
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("LOGIN_CODE", "120015079");//610002680     101000913 //用户账号
        map2.put("USER_PWD", "111111");                       //密码
        map1.put("funcid", "300010");
        map1.put("token", "");
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, FileUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);

                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<LogIn>() {}.getType();
                LogIn bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<LogIn.DataBean> data = bean.getData();
                if(code.equals("0") && data!=null){
                    for(int i=0;i<data.size();i++){
                        LogIn.DataBean dataBean = data.get(i);
                        session = dataBean.getSESSION();
                    }
                }
                getMsg(s);
            }
        });
    }*/

    private void getMsg(String stockCode) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("SECU_CODE", stockCode);
        map2.put("OPEN_TYPE", "1");
        map1.put("funcid", "300446");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(CurrencyFundRedeemActivity.this,ConstantUtil.NETWORK_ERROR);
                e.toString();
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<CurrencyFundShuHuiBean>() {}.getType();
                CurrencyFundShuHuiBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<CurrencyFundShuHuiBean.DataBean> data = bean.getData();
                if("-6".equals(code)){
                    Intent intent = new Intent(CurrencyFundRedeemActivity.this, TransactionLoginActivity.class);
                    CurrencyFundRedeemActivity.this.startActivity(intent);
                    finish();
                }else
                if("0".equals(code) && data != null){
                    map = new HashMap<String, String>();
                    for(int i=0;i<data.size();i++){
                        CurrencyFundShuHuiBean.DataBean dataBean = data.get(i);
                        String stock_name = dataBean.getSTOCK_NAME();           //基金名称
                        String stock_account = dataBean.getSTOCK_ACCOUNT();     //股东账号
                        String enable_balance = dataBean.getENABLE_BALANCE();   //可用资金
                        String enable_amount = dataBean.getENABLE_AMOUNT();     //可用份额
                        String exchange_type = dataBean.getEXCHANGE_TYPE();     //市场
                        map.put("stock_name",stock_name);
                        map.put("stock_account",stock_account);
                        map.put("exchange_type",exchange_type);
                        map.put("session",session);
                        tvRedeemFundNameValue.setText(stock_name);           //基金名称
                        tvRedeemStockholderNumValue.setText(stock_account);    //股东账号
                        tvRedeemExpendableFundValue.setText(enable_amount);   //可用份额
                    }
                }else {
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(bean.getMsg(),CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(getFragmentManager(),CurrencyFundRedeemActivity.class.toString());
                }

            }
        });
    }
}
