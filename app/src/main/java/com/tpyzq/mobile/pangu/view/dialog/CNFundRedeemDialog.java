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

import static com.tpyzq.mobile.pangu.activity.trade.stock.CNFundRedeemActivity.CNFundFragmengManager;

/**
 * Created by wangqi on 2016/12/7.
 * 基金赎回 弹窗
 */

public class CNFundRedeemDialog extends BaseDialog implements View.OnClickListener {
    private static final String TAG = "CNFundRedeemPop";
    private View popupWindow;
    private TextView tvCNRedeemJiJinName, tvCNRedeemJiJinCode, tvCNRedeemFenE, tvCNRedeemGuDongNum = null;
    private Context context;
    private String commitMoney;
    private String stockCode;
    private Activity mActivity;
    private HashMap<String, String> map;
    private ClearData mclearData;

    public CNFundRedeemDialog(Context context, HashMap<String, String> map, String stockCode, String commitMoney, Activity mActivity, ClearData clearData) {
        super(context);
        this.context = context;
        this.commitMoney = commitMoney;
        this.stockCode = stockCode;
        this.mActivity = mActivity;
        this.map = map;
        this.mclearData = clearData;
    }


    @Override
    public void setView() {
        tvCNRedeemJiJinName = (TextView) findViewById(R.id.tvCNRedeemJiJinName);      //基金名称
        tvCNRedeemJiJinCode = (TextView) findViewById(R.id.tvCNRedeemJiJinCode);     //基金代码
        tvCNRedeemFenE = (TextView) findViewById(R.id.tvCNRedeemFenE);                //赎回份额
        tvCNRedeemGuDongNum = (TextView) findViewById(R.id.tvCNRedeemGuDongNum);     //股东代码
    }

    @Override
    public int getLayoutId() {
        return R.layout.cnfund_redeem_popupwindow;
    }

    @Override
    public void initData() {
        findViewById(R.id.tvCNRedeemQD).setOnClickListener(this);                     //确定
        findViewById(R.id.tvCNRedeemQX).setOnClickListener(this);                     //取消
        tvCNRedeemJiJinName.setText(map.get("stock_name"));
        tvCNRedeemJiJinCode.setText(stockCode);
        tvCNRedeemFenE.setText(commitMoney);
        tvCNRedeemGuDongNum.setText(map.get("stock_account"));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCNRedeemQD:     //点击确定
                //赎回
                commit();          //提交申请
                dismiss();
                break;
            case R.id.tvCNRedeemQX:     //点击取消
                dismiss();
                break;
        }
    }


    /**
     * 赎回提交
     */
    private void commit() {
        String mSession = SpUtils.getString(context, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("SECU_CODE", stockCode);
        map2.put("ENTRUST_AMOUNT", commitMoney);
        map2.put("STOCK_ACCOUNT", map.get("stock_account"));
        map1.put("funcid", "300197");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
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
                Type type = new TypeToken<ChangNeiSubscriptionCommitBean>() {
                }.getType();
                ChangNeiSubscriptionCommitBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if (code.equals("-6")) {
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity) context).finish();
                } else if (code.equals("0")) {
//                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    CentreToast.showText(context,"委托已提交",true);
                    mclearData.clear();
                    dismiss();
                } else {
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(CNFundFragmengManager,CNFundRedeemDialog.class.toString());
                    dismiss();
                }
            }
        });
    }

}
