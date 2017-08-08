package com.tpyzq.mobile.pangu.activity.trade.otc_business;

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
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_RedeemEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.OTC_RedeemDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * OTC 赎回界面
 * 刘泽鹏
 */
public class OTC_RedeemActivity extends BaseActivity implements View.OnClickListener, IsClickedListener {

    private static final String TAG = "OTC_Redeem";
    public static final int REQUSET = 1;
    private final int MAXNUM = 6;                         //股票代码输入框的最大值
    private EditText etOTC_SHProductCode, etOTC_RedeemShare;
    private TextView tvOTC_ChooseOTCSHProduct, tvOTC_SHProductNameValue,
            tvOTC_SHProductJingZhiValue, tvOTC_SHExpendableCapitalValue;
    private Button bnOTC_RedeemQueDing;
    private HashMap<String, String> map;
    private int point = -1;
    private KeyboardUtil mKeyBoardUtil;
    private ScrollView mScrollView;

    @Override
    public void initView() {
        Intent intent = getIntent();
        String prod_code = intent.getStringExtra("prod_code");//从 份额页面传过来的  赎回 股票代码

        final String mSession = SpUtils.getString(this, "mSession", "");
        etOTC_SHProductCode = (EditText) this.findViewById(R.id.etOTC_SHProductCode);                    //产品代码输入框
        etOTC_RedeemShare = (EditText) this.findViewById(R.id.etOTC_RedeemShare);                        //赎回份额输入框
        tvOTC_ChooseOTCSHProduct = (TextView) this.findViewById(R.id.tvOTC_ChooseOTCSHProduct);         //选择持仓OTC按钮
        tvOTC_SHProductNameValue = (TextView) this.findViewById(R.id.tvOTC_SHProductNameValue);         //产品名称
        tvOTC_SHProductJingZhiValue = (TextView) this.findViewById(R.id.tvOTC_SHProductJingZhiValue);   //产品净值
        tvOTC_SHExpendableCapitalValue = (TextView) this.findViewById(R.id.tvOTC_SHExpendableCapitalValue); //可赎

        tvOTC_ChooseOTCSHProduct.setOnClickListener(this);                                                //选择OTC产品添加监听
        this.findViewById(R.id.ivOTC_Reddem_back).setOnClickListener(this);                               //返回按钮
        bnOTC_RedeemQueDing = (Button) this.findViewById(R.id.bnOTC_RedeemQueDing);                     //确定按钮
        bnOTC_RedeemQueDing.setOnClickListener(this);                                                     //添加监听

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.fundRootLayout);
        initMoveKeyBoard(rootLayout, null,etOTC_SHProductCode);

        if(prod_code != null && prod_code.length()>0){
            etOTC_SHProductCode.setText(prod_code);
            getAffirmMsg(mSession, prod_code);
        }

        etOTC_SHProductCode.setFocusableInTouchMode(true);                                                //初始化使获得焦点
        etOTC_SHProductCode.addTextChangedListener(new TextWatcher() {                                    //添加监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == MAXNUM) {                                                           //当输入的代码为6位数时请求数据
                    etOTC_RedeemShare.setFocusableInTouchMode(true);
                    getAffirmMsg(mSession, s.toString());                                              //根据输入的代码获取确认信息
                } else {
                    map = null;
                    etOTC_RedeemShare.setFocusableInTouchMode(false);
                    //给产品名称，净值，可用资金赋值
                    etOTC_RedeemShare.setText("");
                    tvOTC_SHProductNameValue.setText("");
                    tvOTC_SHProductJingZhiValue.setText("");
                    tvOTC_SHExpendableCapitalValue.setText("");
                }
            }
        });


        etOTC_RedeemShare.setFocusableInTouchMode(false);                                        //初始化使其失去焦点
        etOTC_RedeemShare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {                                                              //如果申购金额输入框有数据
                    bnOTC_RedeemQueDing.setBackgroundResource(R.drawable.lonin);                 //背景亮
                    bnOTC_RedeemQueDing.setEnabled(true);                                        //可点击
                } else {
                    bnOTC_RedeemQueDing.setBackgroundResource(R.drawable.lonin4);                //背景灰色
                    bnOTC_RedeemQueDing.setEnabled(false);                                       //不可点击
                }
            }
        });

        bnOTC_RedeemQueDing.setEnabled(false);                                                   //初始化使确定按钮不可点击
        bnOTC_RedeemQueDing.setBackgroundResource(R.drawable.lonin4);                            //初始化使其颜色为灰色
    }





    /**
     * 获取确认信息
     */
    private void getAffirmMsg(final String mSession, String stockCode) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("FLAG", "true");
        map2.put("OPER_TYPE", "2");
        map1.put("funcid", "730206");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(OTC_RedeemActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.i(TAG, response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("-6".equals(code)) {
                        Intent intent = new Intent(OTC_RedeemActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        OTC_RedeemActivity.this.finish();
                    } else if ("0".equals(code)) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            map = new HashMap<String, String>();
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String prod_name = jsonObject1.getString("PROD_NAME");          //产品名称
                            String prodta_no = jsonObject1.getString("PRODTA_NO");          //产品TA编号
                            String last_price = jsonObject1.getString("LAST_PRICE");        //产品净值
                            String enable_amount = jsonObject1.getString("ENABLE_AMOUNT");  //可赎
                            //给产品名称，净值，可用资金赋值
                            tvOTC_SHProductNameValue.setText(prod_name);
                            tvOTC_SHProductJingZhiValue.setText(last_price);
                            tvOTC_SHExpendableCapitalValue.setText(enable_amount);

                            map.put("prod_name", prod_name);
                            map.put("prodta_no", prodta_no);
                        }
                        etOTC_RedeemShare.setFocusableInTouchMode(true);          //数据请求完毕 使认购金额输入框获取焦点
//                        etOTC_RedeemShare.setInputType(EditorInfo.TYPE_CLASS_PHONE);       //调  数字键盘
                    } else {

//                        MistakeDialog.showDialog(jsonObject.getString("msg"), OTC_RedeemActivity.this);
                        CentreToast.showText(OTC_RedeemActivity.this,jsonObject.getString("msg"));
                        //给产品名称，净值，可用资金赋值
                        tvOTC_SHProductNameValue.setText("");
                        tvOTC_SHProductJingZhiValue.setText("");
                        tvOTC_SHExpendableCapitalValue.setText("");
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
            int position = data.getIntExtra("position", -1);
            etOTC_SHProductCode.setFocusableInTouchMode(true);
            etOTC_SHProductCode.setText(data.getStringExtra("stockcode"));
            point = data.getIntExtra("position", -1);
            etOTC_SHProductCode.setSelection(etOTC_SHProductCode.getText().length());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__redeem;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOTC_Reddem_back:                         //点击返回销毁当前界面
                this.finish();
                break;
            case R.id.tvOTC_ChooseOTCSHProduct:                 //跳转 选择OTC产品界面
                Intent intent = new Intent(this, OTC_ChiCangActivity.class);
                intent.putExtra("point", point);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bnOTC_RedeemQueDing:                      //点击弹出赎回确认信息
                if (map == null)
                    map = new HashMap<>();
                String RedeemShare = etOTC_RedeemShare.getText().toString();       //获取输入的赎回份额
                String stockCode = etOTC_SHProductCode.getText().toString();          //获取输入的 输入代码
                //实例化PopupWindow
                OTC_RedeemDialog dialog = new OTC_RedeemDialog(this, this, map, RedeemShare, stockCode, this);
                dialog.show();
                break;
        }
    }

    @Override
    public void callBack(boolean isOk) {
        //如果 委托成功 清空首页数据
        if (true == isOk) {
            etOTC_SHProductCode.setText("");
            etOTC_RedeemShare.setText("");
            tvOTC_SHProductNameValue.setText("");
            tvOTC_SHProductJingZhiValue.setText("");
            tvOTC_SHExpendableCapitalValue.setText("");
        }
    }
}
