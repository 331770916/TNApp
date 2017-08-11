package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 刘泽鹏 on 2016/8/12.
 * 货币基金 赎回信息确认   popupWindow
 */
public class FundRedeemPopupWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "FundRedeemPopWindow";
    private String mSession;
    private View popupWindow;
    private TextView tvJiJinName, tvJiJinCode, tvShenGouMoney, tvStockHolderDaiMa = null;
    private Context context;
    private HashMap<String, String> map;
    private String fundAmount;
    private String stockCode;
    private String exchange_type;
    private Activity mActivity;

    public FundRedeemPopupWindow(Context context, Activity activity, HashMap<String, String> map, String fundAmount, String stockCode) {
        super(context);
        this.context = context;
        this.mActivity = activity;
        this.map = map;
        this.fundAmount = fundAmount;
        this.stockCode = stockCode;
        this.exchange_type = map.get("exchange_type");
        mSession=map.get("session");
//        mSession = SpUtils.getString(context, "mSession", "");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.fund_redeem_popupwindow, null);

        tvJiJinName = (TextView) popupWindow.findViewById(R.id.tvJiJinName);   //基金名称
        tvJiJinName.setText(map.get("stock_name"));

        tvJiJinCode = (TextView) popupWindow.findViewById(R.id.tvJiJinCode);           //基金代码
        tvJiJinCode.setText(stockCode);

        tvShenGouMoney = (TextView) popupWindow.findViewById(R.id.tvShenGouMoney);         //申购金额
        tvShenGouMoney.setText(fundAmount);

        tvStockHolderDaiMa = (TextView) popupWindow.findViewById(R.id.tvStockHolderDaiMa);         //股东代码
        tvStockHolderDaiMa.setText(map.get("stock_account"));

        popupWindow.findViewById(R.id.bnQD).setOnClickListener(this);                     //确定
        popupWindow.findViewById(R.id.bnQX).setOnClickListener(this);                     //取消

        this.setContentView(popupWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnQD:              //点击确定
                commit();                //基金申购
                this.dismiss();
                break;
            case R.id.bnQX:             //点击取消
                this.dismiss();
                break;
        }
    }

    /**
     * 提交申购请求
     */
    private void commit() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_CODE",stockCode );
        map2.put("ENTRUST_AMOUNT", fundAmount);
        map1.put("funcid", "300442");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(context,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        CentreToast.showText(context,"委托已提交",true);
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(context, TransactionLoginActivity.class);
                        context.startActivity(intent);
                        dismiss();
                    }else{
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(res.optString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(mActivity.getFragmentManager(),FundRedeemPopupWindow.class.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                /**
                Gson gson = new Gson();
                Type type = new TypeToken<CurrencyFundShuHuiCommit>() {}.getType();
                CurrencyFundShuHuiCommit bean = gson.fromJson(response, type);
                String code = bean.getCode();       //    0'表示成功，非'0'代表失败
                String msg = bean.getMsg();
                List<?> data = bean.getData();
                if(code.equals("-6")){
                    Intent intent = new Intent(context, TransactionLogin_First.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                }else
                if (code.equals("0")) {
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                }else {
                 MistakeDialog.showDialog(msg, mActivity);
                }
                */
            }
        });
    }
}
