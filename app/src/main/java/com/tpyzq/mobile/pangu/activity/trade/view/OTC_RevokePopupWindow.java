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
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者：刘泽鹏 on 2016/8/31 11:39
 * OTC 撤单确认信息 弹框
 */
public class OTC_RevokePopupWindow extends PopupWindow implements View.OnClickListener {

    private String TAG = "OTC_RevokeWindow";
    private Context context;
    private View popupWindow;
    private TextView tvOTC_RevokeProductName,tvOTC_RevokeEntrustTime,tvOTC_RevokeState,
            tvOTC_RevokeNum,tvOTC_EnmtrustMoney=null;
    private Map<String,String> map;
    private String prod_code,prodta_no,allot_no;
    private int position;
    private PositionListener listener;
    private Activity mActivity;

    public OTC_RevokePopupWindow(Context context, Activity activity, Map<String,String> map, final int position, final PositionListener listener) {
        super(context);
        this.context=context;
        this.map = map;
        this.position=position;
        this.listener=listener;
        this.mActivity=activity;
        prod_code = map.get("prod_code");       //产品代码
        prodta_no = map.get("prodta_no");       //产品TA编码
        allot_no = map.get("allot_no");         //申请编码
        popupWindow = LayoutInflater.from(context).inflate(R.layout.otc_revoke_popupwindow,null);
        tvOTC_RevokeProductName = (TextView) popupWindow.findViewById(R.id.tvOTC_RevokeProductName);    //产品名称
        tvOTC_RevokeProductName.setText(map.get("prod_name"));
        tvOTC_RevokeEntrustTime = (TextView) popupWindow.findViewById(R.id.tvOTC_RevokeEntrustTime);    //委托时间
        tvOTC_RevokeEntrustTime.setText(map.get("entrust_time"));
        tvOTC_RevokeState = (TextView) popupWindow.findViewById(R.id.tvOTC_RevokeState);                 //状态
        tvOTC_RevokeState.setText(map.get("business_flag"));
        tvOTC_RevokeNum = (TextView) popupWindow.findViewById(R.id.tvOTC_RevokeNum);                     //委托数量
        tvOTC_RevokeNum.setText(map.get("entrust_amount"));
        tvOTC_EnmtrustMoney = (TextView) popupWindow.findViewById(R.id.tvOTC_EnmtrustMoney);            //委托金额
        tvOTC_EnmtrustMoney.setText(map.get("entrust_amount"));

        popupWindow.findViewById(R.id.tvOTC_RevokeQD).setOnClickListener(this);                           //确定按钮
        popupWindow.findViewById(R.id.tvOTC_RevokeQX).setOnClickListener(this);                           //取消按钮

        //加载popupWindow布局
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
        switch (v.getId()){
            case R.id.tvOTC_RevokeQX:           //点击取消按钮销毁弹框
                dismiss();
                break;
            case R.id.tvOTC_RevokeQD:           //点击确定按钮提交委托
                commit();
                break;
        }
    }

    /**
     * 提交撤单委托
     */
    private void commit() {
        String mSession = SpUtils.getString(context, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("PROD_CODE",prod_code);     //产品代码
        map2.put("PRODTA_NO",prodta_no);     //产品TA编码
        map2.put("ALLOT_NO",allot_no);       //申请编码
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map1.put("funcid","300503");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)){
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if(code.equals("-6")){
                        Intent intent = new Intent(context, TransactionLoginActivity.class);
                        context.startActivity(intent);
                        dismiss();
                        ((Activity)context).finish();
                    }else if(code.equals("0")){
//                        ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                        CentreToast.showText(context,"委托已提交",true);
                        listener.callBack(position);
                    }else {
                        MistakeDialog.showDialog(res.optString("msg"), mActivity);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                /*
                Gson gson=new Gson();
                java.lang.reflect.Type type = new TypeToken<OTC_RevokeCommit>() {}.getType();
                OTC_RevokeCommit bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if(code.equals("-6")){
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                }else
                if(code.equals("0")){
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    listener.callBack(position);
                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                }
               */
            }
        });

        dismiss();
    }

    public interface PositionListener{
        void callBack(int position);
    }
}
