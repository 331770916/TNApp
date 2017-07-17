package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IsClickedListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/12/7.
 * OTC 赎回确认信息弹框界面
 */

public class OTC_RedeemDialog extends BaseDialog implements View.OnClickListener{
    private static final String TAG = "OTC_RedeemPopupWindow";
    private TextView tvOTC_SHChanPinMingCheng, tvOTC_SHChanPinDaiMa, tvOTC_SHShenGouFenE = null;
    private Context context;
    private HashMap<String, String> map;
    private String stockCode;
    private String RedeemShare;
    private Activity mActivity;
    private IsClickedListener isOk;


    public OTC_RedeemDialog(Context context, Activity activity, HashMap<String, String> map,
                            String RedeemShare, String stockCode, IsClickedListener isOk) {
        super(context);
        this.context = context;
        this.map = map;
        this.stockCode = stockCode;
        this.RedeemShare = RedeemShare;
        this.mActivity=activity;
        this.isOk = isOk;
    }

    @Override
    public void setView() {
        tvOTC_SHChanPinMingCheng = (TextView) findViewById(R.id.tvOTC_SHChanPinMingCheng);       //产品名称
        tvOTC_SHChanPinDaiMa = (TextView) findViewById(R.id.tvOTC_SHChanPinDaiMa);               //产品代码
        tvOTC_SHShenGouFenE = (TextView) findViewById(R.id.tvOTC_SHShenGouFenE);                 //赎回份额

        findViewById(R.id.tvOTC_SHQD).setOnClickListener(this);                                 //取消
        findViewById(R.id.tvOTC_SHQX).setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.otc_redeem_popupwindow;
    }

    @Override
    public void initData() {
        tvOTC_SHChanPinMingCheng.setText(map.get("prod_name"));
        tvOTC_SHChanPinDaiMa.setText(stockCode);
        tvOTC_SHShenGouFenE.setText(RedeemShare);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOTC_SHQX:
                dismiss();
                break;
            case R.id.tvOTC_SHQD:
                commit();
                break;
        }
    }



    /**
     * 赎回
     */
    private void commit() {
        String mSession = SpUtils.getString(context, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("PRODTA_NO", map.get("prodta_no"));
        map2.put("AMOUNT", RedeemShare);
        map2.put("FLAG", "true");
        map1.put("funcid", "730203");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if (code.equals("-6")) {
                        Intent intent = new Intent(context, TransactionLoginActivity.class);
                        context.startActivity(intent);
                        dismiss();
                        ((Activity)context).finish();
                    } else if (code.equals("0")) {
                        CentreToast.showText(context,"委托已提交",true);
                        isOk.callBack(true);
                        dismiss();
                    } else {
                        MistakeDialog.showDialog(res.optString("msg"), mActivity);
                        isOk.callBack(false);
                        dismiss();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                /*
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_RedeemCommit>() {}.getType();
                OTC_RedeemCommit bean = gson.fromJson(response, type);
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
                    isOk.callBack(true);
                    dismiss();
                } else {
                    MistakeDialog.showDialog(msg, mActivity);
                    isOk.callBack(false);
                    dismiss();
                }
                 */
            }
        });
    }
}
