package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.ChangNeiSubscriptionCommitBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ClearData;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/12/7.
 * 基金认购弹框
 */

public class FundSubscriptionDialog extends BaseDialog implements View.OnClickListener{
    private static final String TAG = "FundSubscriptionPop";
    private View popupWindow;
    private TextView tvCNJiJinMingCheng,tvCNJiJinDaiMa,tvCNShenGouJinE;
    private Context context;
    private HashMap<String,String> map;
    private String stockCode;
    private String commitMoney;
    private Activity mActivity;
    private ClearData mClearData;

    public FundSubscriptionDialog(Context context, HashMap<String,String> map, String commitMoney, String stockCode, Activity mActivity, ClearData clearData) {
        super(context);
        this.context=context;
        this.map = map;
        this.stockCode = stockCode;
        this.commitMoney = commitMoney;
        this.mActivity = mActivity;
        this.mClearData=clearData;
    }

    @Override
    public void setView() {
        tvCNJiJinMingCheng= (TextView) findViewById(R.id.tvCNJiJinMingCheng);   //基金名称
        tvCNJiJinDaiMa= (TextView) findViewById(R.id.tvCNJiJinDaiMa);           //股东账号
        tvCNShenGouJinE= (TextView) findViewById(R.id.tvCNShenGouJinE);         //认购账号
    }

    @Override
    public int getLayoutId() {
        return R.layout.fund_subscription_popupwindow;
    }

    @Override
    public void initData() {
        findViewById(R.id.tvCNQD).setOnClickListener(this);                     //确定
        findViewById(R.id.tvCNQX).setOnClickListener(this);
        tvCNJiJinMingCheng.setText(map.get("stock_name"));
        tvCNJiJinDaiMa.setText(map.get("stock_account"));
        tvCNShenGouJinE.setText(commitMoney);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCNQD:     //点击确定
                commit();          //提交申请
                dismiss();
                break;
            case R.id.tvCNQX:     //点击取消
                dismiss();
                break;
        }
    }


    /**
     * 提交认购申请
     */
    private void commit() {
        String mSession = SpUtils.getString(context, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("SECU_CODE", stockCode);
        map2.put("ORDER_PRICE", commitMoney);
        map2.put("STOCK_ACCOUNT", map.get("stock_account"));
        map2.put("MARKET",map.get("exchange_type"));
        map1.put("funcid", "300195");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ChangNeiSubscriptionCommitBean>() {}.getType();
                ChangNeiSubscriptionCommitBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if(code.equals("-6")){
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                }else
                if (code.equals("0")) {
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    mClearData.clear();
                    dismiss();
                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                    dismiss();
                }
            }
        });
    }

}
