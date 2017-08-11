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
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.activity.trade.stock.CNFundSubscribeActivity.cNFundSubscribeManager;

/**
 * Created by wangqi on 2016/12/7.
 * 基金申购弹出
 */

public class CNFundSubscribeDialog extends BaseDialog implements View.OnClickListener {
    private static final String TAG = "CNFundSubscribePop";
    private TextView tvCNJiJinName, tvCNJiJinCode, tvCNShenGouSum, tvCNGuDongNum = null;
    private Context context;
    private String commit_money;
    private String stockCode;
    private String stock_account;
    private String stock_name;
    private String stock_nav;
    private String exchange_type;
    private Activity mActivity;
    private ClearData mClearData;

    public CNFundSubscribeDialog(Context context, String stockname, String stockCode, String stockAccount, String commitMoney, String stockNav, String exchangeType, Activity mActivity, ClearData clearData) {
        super(context);
        this.context = context;
        this.mActivity = mActivity;

        this.stock_name = stockname;            //股票名称
        this.stockCode = stockCode;           //搜索股票
        this.stock_account = stockAccount;     //股东账号

        this.commit_money = commitMoney;       //申认购金额
        this.stock_nav = stockNav;             //资金净值
        this.exchange_type = exchangeType;     //市场
        this.mClearData = clearData;
    }

    @Override
    public void setView() {
        tvCNJiJinName = (TextView) findViewById(R.id.tvCNJiJinName);   //基金名称
        tvCNJiJinCode = (TextView) findViewById(R.id.tvCNJiJinCode);           //基金代码
        tvCNShenGouSum = (TextView) findViewById(R.id.tvCNShenGouSum);         //申购金额
        tvCNGuDongNum = (TextView) findViewById(R.id.tvCNGuDongNum);         //股东代码

        findViewById(R.id.tvChangNeiQD).setOnClickListener(this);                     //确定
        findViewById(R.id.tvChangNeiQX).setOnClickListener(this);                     //取消
    }

    @Override
    public int getLayoutId() {
        return R.layout.cnfund_subscribe_popupwindow;
    }

    @Override
    public void initData() {
        tvCNJiJinName.setText(stock_name);              //股票名称
        tvCNJiJinCode.setText(stock_nav);               //净额
        tvCNShenGouSum.setText(stock_account);          //账号
        tvCNGuDongNum.setText(commit_money);            //申购
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangNeiQD:     //点击确定
                //基金申购
                commit();          //提交申请
                dismiss();
                break;
            case R.id.tvChangNeiQX:     //点击取消
                dismiss();
                break;
        }
    }


    /**
     * 申购提交
     */
    private void commit() {
        String mSession = SpUtils.getString(context, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("SECU_CODE", stockCode);
        map2.put("ORDER_PRICE", commit_money);
        map2.put("STOCK_ACCOUNT", stock_account);
        map2.put("MARKET",exchange_type);
        map1.put("funcid", "300196");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
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
                    CentreToast.showText(context,"委托已提交",true);
                    mClearData.clear();
                    dismiss();
                }else {
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(cNFundSubscribeManager,CNFundSubscribeDialog.class.toString());
                    dismiss();
                }
            }
        });
    }
}