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
import com.tpyzq.mobile.pangu.activity.trade.open_fund.AssessConfirmActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.OTC_AffirmBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionCommitBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/12/7.
 * OTC 认购确认信息弹框
 */

public class OTC_SubscriptionDialog extends BaseDialog implements View.OnClickListener{
    private static final String TAG = "OTC_SubscriptionWindow";
    private TextView tvOTC_ChanPinMingCheng,tvOTC_ChanPinDaiMa,tvOTC_RenGouJinE=null;
    private Context context;
    private HashMap<String,String> map;
    private String stockCode,mSession;
    private String SubscriptionMoney;
    private Activity mActivity;
    private IsClickedListener isOk;

    public OTC_SubscriptionDialog(Context context, Activity activity, HashMap<String,String> map,
                                  String SubscriptionMoney, String stockCode, IsClickedListener isOk) {
        super(context);
        this.context=context;
        this.mActivity = activity;
        this.map=map;
        this.stockCode=stockCode;
        this.SubscriptionMoney=SubscriptionMoney;
        this.isOk = isOk;
        mSession = SpUtils.getString(context, "mSession", "");
    }

    @Override
    public void setView() {
        tvOTC_ChanPinMingCheng= (TextView) findViewById(R.id.tvOTC_ChanPinMingCheng);       //产品名称
        tvOTC_ChanPinMingCheng.setText(map.get("prod_name"));
        tvOTC_ChanPinDaiMa= (TextView) findViewById(R.id.tvOTC_ChanPinDaiMa);               //产品代码
        tvOTC_ChanPinDaiMa.setText(stockCode);
        tvOTC_RenGouJinE= (TextView) findViewById(R.id.tvOTC_RenGouJinE);                   //认购金额
        tvOTC_RenGouJinE.setText(SubscriptionMoney);


    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_subsription_popupwindow;
    }

    @Override
    public void initData() {
        findViewById(R.id.tvOTC_QX).setOnClickListener(this);                                 //取消
        findViewById(R.id.tvOTC_QD).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvOTC_QX:
                dismiss();
                break;
            case R.id.tvOTC_QD:
                getAffirm();
                break;
        }
    }


    /**
     * 获取判断是否跳转 确认书界面的 值
     */
    private void getAffirm() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("PROD_CODE",stockCode);
        map2.put("PRODTA_NO",map.get("prodta_no"));
        map2.put("FLAG","true");
        map1.put("funcid","300512");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_AffirmBean>() {}.getType();
                OTC_AffirmBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<OTC_AffirmBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                } else
                if(code.equals("0") && data != null){
                    for(int i=0;i<data.size();i++){
                        OTC_AffirmBean.DataBean dataBean = data.get(i);
                        String is_ok = dataBean.getIS_OK();
                        String is_agreement = dataBean.getIS_AGREEMENT();
                        String is_open = dataBean.getIS_OPEN();
                        String is_outofdate = dataBean.getIS_OUTOFDATE();
                        String ofrisk_flag = dataBean.getOFRISK_FLAG();
                        String prodrisk_level_name = dataBean.getPRODRISK_LEVEL_NAME();
                        String corp_risk_level = dataBean.getCORP_RISK_LEVEL();
                        String risk_level_name = dataBean.getRISK_LEVEL_NAME();
                        String prodrisk_level = dataBean.getPRODRISK_LEVEL();
                        if(is_ok.equals("0")){
                            getProductMsg();
                        }else {
                            Intent intent = new Intent();

                            AssessConfirmEntity assessConfirmBean = new AssessConfirmEntity();
                            assessConfirmBean.productcode = stockCode;
                            assessConfirmBean.productcompany = map.get("prodta_no");
                            assessConfirmBean.productprice = SubscriptionMoney;
                            assessConfirmBean.type = "3";
                            assessConfirmBean.IS_ABLE = is_ok;
                            assessConfirmBean.IS_AGREEMENT = is_agreement;
                            assessConfirmBean.IS_OPEN = is_open;
                            assessConfirmBean.IS_VALIB_RISK_LEVEL = is_outofdate;
                            assessConfirmBean.OFRISK_FLAG = ofrisk_flag;
                            assessConfirmBean.OFUND_RISKLEVEL_NAME = prodrisk_level_name;
                            assessConfirmBean.RISK_LEVEL = corp_risk_level;
                            assessConfirmBean.RISK_LEVEL_NAME = risk_level_name;
                            assessConfirmBean.RISK_RATING = prodrisk_level;

                            intent.putExtra("assessConfirm",assessConfirmBean);
                            intent.putExtra("transaction", "true");
                            intent.setClass(context,AssessConfirmActivity.class);
                            mActivity.startActivityForResult(intent,100);
                            dismiss();
                        }
                    }

                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                }
            }
        });
    }

    /**
     * 认购
     */
    private void getProductMsg(){
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("PROD_CODE",stockCode);
        map2.put("PRODTA_NO",map.get("prodta_no"));
        map2.put("ENTRUST_BALANCE",SubscriptionMoney);
        map2.put("FLAG","true");
        map1.put("funcid","730201");
        map1.put("token",mSession);
        map1.put("parms",map2);
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
                Type type = new TypeToken<OTC_SubscriptionCommitBean>() {}.getType();
                OTC_SubscriptionCommitBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if (code.equals("-6")) {
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                } else
                if (code.equals("0")) {
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    dismiss();
                    isOk.callBack(true);
                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                    dismiss();
                    isOk.callBack(false);
                }
            }
        });
    }
}
